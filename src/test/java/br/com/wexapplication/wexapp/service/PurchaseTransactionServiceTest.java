package br.com.wexapplication.wexapp.service;

import br.com.wexapplication.wexapp.dto.PurchaseTransactionRequest;
import br.com.wexapplication.wexapp.model.PurchaseTransaction;
import br.com.wexapplication.wexapp.repository.PurchaseTransactionRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@SpringBootTest
public class PurchaseTransactionServiceTest {

    @InjectMocks
    private PurchaseTransactionService purchaseTransactionService;

    @Mock
    private PurchaseTransactionRepository purchaseTransactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePurchaseTransaction() {
        PurchaseTransactionRequest request = PurchaseTransactionRequest
                .builder()
                .purchaseAmount(new BigDecimal("10"))
                .transactionDate(LocalDate.now())
                .description("Test Description")
                .build();

        PurchaseTransaction simulatedTransaction = new PurchaseTransaction();
        simulatedTransaction.setId(1L);

        when(purchaseTransactionRepository.save(any())).thenReturn(simulatedTransaction);

        PurchaseTransaction createdTransaction = purchaseTransactionService.createPurchaseTransaction(request);

        verify(purchaseTransactionRepository, times(1)).save(any());

        assertEquals(Optional.of(1L), Optional.ofNullable(createdTransaction.getId()));
    }

    @Test
    public void testFindById() {
        Long id = 1L;

        PurchaseTransaction simulatedTransaction = new PurchaseTransaction();
        simulatedTransaction.setId(id);

        when(purchaseTransactionRepository.findById(id)).thenReturn(Optional.of(simulatedTransaction));

        Optional<PurchaseTransaction> foundTransaction = purchaseTransactionService.findById(id);

        assertEquals(id, foundTransaction.get().getId());
    }
}
