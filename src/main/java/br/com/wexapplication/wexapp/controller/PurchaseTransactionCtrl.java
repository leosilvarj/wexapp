package br.com.wexapplication.wexapp.controller;

import br.com.wexapplication.wexapp.dto.PurchaseTransactionConvertedResponse;
import br.com.wexapplication.wexapp.dto.PurchaseTransactionRequest;
import br.com.wexapplication.wexapp.dto.PurchaseTransactionResponse;
import br.com.wexapplication.wexapp.exceptions.ResourceNotFoundException;
import br.com.wexapplication.wexapp.model.PurchaseTransaction;
import br.com.wexapplication.wexapp.service.CurrencyConversionService;
import br.com.wexapplication.wexapp.service.PurchaseTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/purchase-transaction")
public class PurchaseTransactionCtrl {
    private final PurchaseTransactionService purchaseTransactionService;
    private final CurrencyConversionService currencyConversionService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<PurchaseTransactionResponse> savePurchaseTransaction(@Valid @RequestBody PurchaseTransactionRequest purchaseTransactionRequest) {
        log.info("Called savePurchaseTransaction with request: {}", purchaseTransactionRequest);
        PurchaseTransaction savedTransaction = purchaseTransactionService.createPurchaseTransaction(purchaseTransactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(savedTransaction, PurchaseTransactionResponse.class));
    }

    @GetMapping("/{id}/converted")
    public ResponseEntity<PurchaseTransactionConvertedResponse> getConvertedPurchaseTransaction(@PathVariable Long id, @RequestParam String targetCountryCurrency) throws ResourceNotFoundException {
        log.info("Called getConvertedPurchaseTransaction with id: {} and targetCountryCurrency: {}", id, targetCountryCurrency);

        PurchaseTransactionConvertedResponse purchaseTransaction = purchaseTransactionService.findById(id)
                .map(purchase -> modelMapper.map(purchase, PurchaseTransactionConvertedResponse.class))
                .orElseThrow(ResourceNotFoundException::new);

        BigDecimal convertedAmount = currencyConversionService.convertUsdToTargetCurrency(targetCountryCurrency, purchaseTransaction.getTransactionDate(), purchaseTransaction.getPurchaseAmount());
        log.info("Transaction retrieved: {} and converted amount is {}", purchaseTransaction, convertedAmount);

        purchaseTransaction.setConvertedAmount(convertedAmount);
        return ResponseEntity.ok(purchaseTransaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseTransactionResponse> getPurchaseTransaction(@PathVariable Long id) throws ResourceNotFoundException {
        log.info("Called getPurchaseTransaction with id: {}", id);
        PurchaseTransactionResponse purchaseTransaction = purchaseTransactionService.findById(id)
                .map(purchase -> modelMapper.map(purchase, PurchaseTransactionResponse.class))
                .orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(purchaseTransaction);
    }
}
