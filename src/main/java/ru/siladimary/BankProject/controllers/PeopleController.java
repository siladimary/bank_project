package ru.siladimary.BankProject.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.siladimary.BankProject.dto.PersonDTO;
import ru.siladimary.BankProject.dto.PersonResponse;
import ru.siladimary.BankProject.exceptions.ErrorResponse;
import ru.siladimary.BankProject.exceptions.PersonNotFoundException;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.security.PersonDetails;
import ru.siladimary.BankProject.services.PeopleService;
import ru.siladimary.BankProject.util.UsernameValidator;

import java.util.Arrays;

@Tag(name = "people_controller")
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
            Person person = peopleService.findByUsername(username).orElseThrow(PersonNotFoundException::new);
            return ResponseEntity.ok(convertToPersonResponse(person));
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(500).body("Произошла ошибка при получении данных о человеке");
        }
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> personNotFoundException(PersonNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("Человека с таким id нет");

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private PersonResponse convertToPersonResponse(Person person) {
        return mapper.map(person, PersonResponse.class);
    }

}
