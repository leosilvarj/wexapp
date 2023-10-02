package br.com.wexapplication.wexapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PurchaseTransactionConvertedResponse {

    private Long id;

    private String description;

    private LocalDate transactionDate;

    private BigDecimal purchaseAmount;

    private BigDecimal convertedAmount;

}
