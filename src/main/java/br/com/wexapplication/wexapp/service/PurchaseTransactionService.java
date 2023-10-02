package br.com.wexapplication.wexapp.service;

import br.com.wexapplication.wexapp.dto.PurchaseTransactionRequest;
import br.com.wexapplication.wexapp.dto.PurchaseTransactionResponse;
import br.com.wexapplication.wexapp.model.PurchaseTransaction;
import br.com.wexapplication.wexapp.repository.PurchaseTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class PurchaseTransactionService {

    private final PurchaseTransactionRepository purchaseTransactionRepository;
    private final ModelMapper modelMapper;

    public PurchaseTransaction createPurchaseTransaction(PurchaseTransactionRequest purchaseTransactionRequest) {
        purchaseTransactionRequest.setPurchaseAmount(purchaseTransactionRequest.getPurchaseAmount().setScale(2, RoundingMode.HALF_UP));
        PurchaseTransaction purchaseTransaction = modelMapper.map(purchaseTransactionRequest, PurchaseTransaction.class);
        return purchaseTransactionRepository.save(purchaseTransaction);
    }

    public Optional<PurchaseTransaction> findById(Long id) {
        return purchaseTransactionRepository.findById(id);
    }
}
