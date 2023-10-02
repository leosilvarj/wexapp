package br.com.wexapplication.wexapp.controller;

import br.com.wexapplication.wexapp.dto.PurchaseTransactionConvertedResponse;
import br.com.wexapplication.wexapp.dto.PurchaseTransactionRequest;
import br.com.wexapplication.wexapp.dto.PurchaseTransactionResponse;
import br.com.wexapplication.wexapp.model.PurchaseTransaction;
import br.com.wexapplication.wexapp.service.PurchaseTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PurchaseTransactionCtrlTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PurchaseTransactionService purchaseTransactionService;

    @LocalServerPort
    private int port;

    @Test
    public void testSaveTransactionWithRoundedDecimalPlaces() {
        LocalDate localDate = LocalDate.now();

        PurchaseTransactionRequest purchaseTransactionRequest = PurchaseTransactionRequest.builder()
                .description("Test Description")
                .transactionDate(localDate)
                .purchaseAmount(new BigDecimal("10.1587"))
                .build();

        ResponseEntity<PurchaseTransactionResponse> response = restTemplate.postForEntity("http://localhost:"+port+"/api/purchase-transaction", purchaseTransactionRequest, PurchaseTransactionResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("Test Description", response.getBody().getDescription());
        assertEquals(localDate, response.getBody().getTransactionDate());
        assertEquals(new BigDecimal("10.16"), response.getBody().getPurchaseAmount());
    }

    @Test
    public void testGetPurchaseTransaction()  {
        PurchaseTransactionRequest purchaseTransactionRequest = PurchaseTransactionRequest.builder()
                .description("Test Description")
                .transactionDate(LocalDate.now())
                .purchaseAmount(new BigDecimal("10"))
                .build();

        PurchaseTransaction transaction = purchaseTransactionService.createPurchaseTransaction(purchaseTransactionRequest);

        ResponseEntity<PurchaseTransactionResponse> response = restTemplate.getForEntity("http://localhost:"+port+"/api/purchase-transaction/" + transaction.getId(), PurchaseTransactionResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Description", response.getBody().getDescription());
    }

    @Test
    public void testGetPurchaseTransactionNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:"+port+"/api/purchase-transaction/0", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSaveTransactionWithError() {
        PurchaseTransactionRequest request = PurchaseTransactionRequest
                .builder()
                .transactionDate(LocalDate.now())
                .purchaseAmount(new BigDecimal("10"))
                .description("This is a description with more than 50 characters, which should be considered invalid.")
                .build();
        ResponseEntity<ArrayList> response = restTemplate.postForEntity("http://localhost:"+port+"/api/purchase-transaction", request, ArrayList.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Field description must not exceed 50 characters.", response.getBody().get(0));
    }


    @Test
    public void testRetrieveConvertedPurchaseTransaction() {
        PurchaseTransactionRequest request = PurchaseTransactionRequest
                .builder()
                .transactionDate(LocalDate.now())
                .purchaseAmount(new BigDecimal("10"))
                .description("Test Description")
                .build();
        PurchaseTransaction purchaseTransaction = purchaseTransactionService.createPurchaseTransaction(request);

        String targetCountryCurrency = "Brazil-Real";
        ResponseEntity<PurchaseTransactionConvertedResponse> response = restTemplate.getForEntity("http://localhost:"+port+"/api/purchase-transaction/"
                + purchaseTransaction.getId() + "/converted?targetCountryCurrency=" + targetCountryCurrency, PurchaseTransactionConvertedResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(purchaseTransaction.getId(), response.getBody().getId());
        assertEquals(purchaseTransaction.getDescription(), response.getBody().getDescription());
        assertNotNull(response.getBody().getConvertedAmount());
        assertEquals(1, response.getBody().getConvertedAmount().compareTo(purchaseTransaction.getPurchaseAmount()));
    }

    @Test
    public void testRetrieveConvertedPurchaseTransactionWithTargetCountryCurrencyNotFound() {
        PurchaseTransactionRequest request = PurchaseTransactionRequest
                .builder()
                .transactionDate(LocalDate.of(2020, 1, 1))
                .purchaseAmount(new BigDecimal("10"))
                .description("Test Description")
                .build();
        PurchaseTransaction purchaseTransaction = purchaseTransactionService.createPurchaseTransaction(request);

        String targetCountryCurrency = "Brazil-Dollar";
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:"+port+"/api/purchase-transaction/"
                + purchaseTransaction.getId() + "/converted?targetCountryCurrency=" + targetCountryCurrency, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There are no exchange rates accessible for the 6 months leading up to or on the purchase date.", response.getBody());
    }

}





