package br.com.wexapplication.wexapp.repository;

import br.com.wexapplication.wexapp.model.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction, Long>,
        JpaSpecificationExecutor<PurchaseTransaction> {

}
