package ru.siladimary.BankProject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.siladimary.BankProject.models.Account;

import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
}
