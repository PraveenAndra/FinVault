package web.app.finvault.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.app.finvault.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
