package ru.siladimary.BankProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.siladimary.BankProject.models.Transaction;
import ru.siladimary.BankProject.repositories.TransactionsRepository;


@Service
@Transactional(readOnly = true)
public class TransactionsService {
    private final TransactionsRepository transactionsRepository;

    @Autowired
    public TransactionsService(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public ResponseEntity<?> findAll(Pageable pageable) {
        Page<Transaction> transactionsPage = transactionsRepository.findAll(PageRequest.of(pageable.getPageNumber(),
                10, Sort.by("timestamp").descending())); //по убыванию

        if (transactionsPage.isEmpty()) {
            return ResponseEntity.ok("Пока что не было выполнено ни одной транзакции");
        } else {
            return ResponseEntity.ok(transactionsPage);
        }
    }
}
