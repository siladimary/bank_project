package ru.siladimary.BankProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.siladimary.BankProject.exceptions.PersonNotFoundException;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.repositories.PeopleRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Optional<Person> findByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    public Person findById(long id) {
        return peopleRepository.findById(id).orElseThrow(PersonNotFoundException::new);
    }
}
