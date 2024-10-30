package ru.siladimary.BankProject.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.siladimary.BankProject.models.Transaction;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAll(Pageable pageable);
}
