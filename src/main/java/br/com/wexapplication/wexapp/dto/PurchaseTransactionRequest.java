package br.com.wexapplication.wexapp.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
public class PurchaseTransactionRequest {

    @NotNull(message = "Field description must be filled.")
    @Size(max = 50, message = "Field description must not exceed 50 characters.")
    private String description;

    @NotNull(message = "Field transactionDate must be filled.")
    private LocalDate transactionDate;

    @NotNull(message = "Field purchaseAmount must be filled.")
    @Digits(integer = 10, message = "Purchase amount must be rounded to the nearest cent.", fraction = 9)
    private BigDecimal purchaseAmount;

}
