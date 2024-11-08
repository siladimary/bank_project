package ru.siladimary.BankProject.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import ru.siladimary.BankProject.dto.PersonResponse;
import ru.siladimary.BankProject.exceptions.ErrorResponse;
import ru.siladimary.BankProject.exceptions.PersonNotFoundException;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.services.PeopleService;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class PeopleController {
    private final PeopleService peopleService;
    private final ModelMapper mapper;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper mapper) {
        this.peopleService = peopleService;
        this.mapper = mapper;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getPerson(@PathVariable("username") String username) {
        try {
            Optional<Person> person = peopleService.findByUsername(username);
            if (person.isEmpty())
                throw new PersonNotFoundException("Человека с таким логином не существует");
            return ResponseEntity.ok(convertToPersonResponse(person.get()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Произошла ошибка при получении данных о человеке");
        }
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> personNotFoundException(PersonNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private PersonResponse convertToPersonResponse(Person person) {
        return mapper.map(person, PersonResponse.class);
    }

}
