package br.com.wexapplication.wexapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "purchase_transaction")
public class PurchaseTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    @Size(max = 50, message = "Field description must not exceed 50 characters.")
    private String description;

    @Column(name = "transaction_date", nullable = false)
    @NotNull(message = "Transaction date must be a valid date.")
    private LocalDate transactionDate;

    @Column(name = "purchase_amount", nullable = false)
    @NotNull(message = "Purchase amount must be a valid amount.")
    @Digits(integer = 10, message = "Purchase amount must be rounded to the nearest cent.", fraction = 9)
    private BigDecimal purchaseAmount;


}
