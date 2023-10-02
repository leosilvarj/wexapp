package br.com.wexapplication.wexapp.service;

import br.com.wexapplication.wexapp.dto.ExchangeData;
import br.com.wexapplication.wexapp.dto.ExchangeResponse;
import br.com.wexapplication.wexapp.exceptions.InternalErrorException;
import br.com.wexapplication.wexapp.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CurrencyConversionService {

    private final WebClient.Builder webClientBuilder;

    @Value("${exchange.api.url}")
    private String exchangeApiUrl;

    public BigDecimal convertUsdToTargetCurrency(String countryCurrency, LocalDate purchaseDate, BigDecimal purchaseAmount) throws ResourceNotFoundException {
        ExchangeResponse exchangeResponse = getExchangeForCountryCurrency(countryCurrency);

        List<ExchangeData> exchanges = exchangeResponse.getData().stream()
                .filter(rate -> !rate.getRecordDate().isAfter(purchaseDate) &&
                        !rate.getRecordDate().isBefore(purchaseDate.minusMonths(6)))
                .toList();

        if (exchanges.isEmpty()) {
            throw new ResourceNotFoundException("There are no exchange rates accessible for the 6 months leading up to or on the purchase date.");
        }

        BigDecimal exchange = exchanges.get(0).getExchangeRate();

        return purchaseAmount.multiply(exchange).setScale(2, RoundingMode.HALF_UP);
    }

    private ExchangeResponse getExchangeForCountryCurrency(String countryCurrency) {
        WebClient webClient = webClientBuilder.baseUrl(exchangeApiUrl).build();

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("fields", "record_date,country,currency,country_currency_desc,exchange_rate")
                        .queryParam("filter", "country_currency_desc:eq:" + countryCurrency + "")
                        .queryParam("sort", "-record_date")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(response, ExchangeResponse.class);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Error parsing response.", e);
        }
    }
}
