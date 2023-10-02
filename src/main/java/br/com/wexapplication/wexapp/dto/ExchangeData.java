package br.com.wexapplication.wexapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExchangeData {
    @JsonProperty("record_date")
    private LocalDate recordDate;
    @JsonProperty("country")
    private String country;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("country_currency_desc")
    private String countryCurrencyDesc;
    @JsonProperty("exchange_rate")
    private BigDecimal exchangeRate;

}
