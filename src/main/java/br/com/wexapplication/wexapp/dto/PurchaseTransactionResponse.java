package br.com.wexapplication.wexapp.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PurchaseTransactionResponse {

    private Long id;

    private String description;

    private LocalDate transactionDate;

    private BigDecimal purchaseAmount;

}
