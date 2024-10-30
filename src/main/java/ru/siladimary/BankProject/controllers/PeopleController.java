package ru.siladimary.BankProject.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.siladimary.BankProject.dto.PersonDTO;
import ru.siladimary.BankProject.exceptions.ErrorConstructUtil;
import ru.siladimary.BankProject.exceptions.PersonErrorResponse;
import ru.siladimary.BankProject.exceptions.PersonNotCreatedException;
import ru.siladimary.BankProject.exceptions.PersonNotFoundException;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.services.PeopleService;
import ru.siladimary.BankProject.util.UsernameValidator;

@Tag(name = "people_controller")
@RestController
@RequestMapping("/user")
public class PeopleController {
    private final PeopleService peopleService;
    private final ModelMapper mapper;
    private final UsernameValidator usernameValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper mapper, UsernameValidator usernameValidator) {
        this.peopleService = peopleService;
        this.mapper = mapper;
        this.usernameValidator = usernameValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable("id") Long id) {
        return ResponseEntity.ok(convertToPersonDTO(peopleService.findById(id)));
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {

        PersonErrorResponse errorResponse = new PersonErrorResponse("Человека с таким id нет");

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return mapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return mapper.map(person, PersonDTO.class);
    }

}
