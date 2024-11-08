package ru.siladimary.BankProject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.siladimary.BankProject.models.Account;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(Integer number);

    boolean existsByAccountNumber(Integer number);
}
