package ru.siladimary.BankProject.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.siladimary.BankProject.dto.AuthenticationDTO;
import ru.siladimary.BankProject.dto.PersonDTO;
import ru.siladimary.BankProject.exceptions.ErrorConstructUtil;
import ru.siladimary.BankProject.exceptions.ErrorResponse;
import ru.siladimary.BankProject.exceptions.PersonNotCreatedException;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.security.JWTUtil;
import ru.siladimary.BankProject.services.RegistrationService;
import ru.siladimary.BankProject.util.UsernameValidator;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsernameValidator usernameValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper mapper;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(UsernameValidator usernameValidator, RegistrationService registrationService,
                          JWTUtil jwtUtil, ModelMapper mapper, AuthenticationManager authenticationManager) {
        this.usernameValidator = usernameValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid PersonDTO personDTO,
                                                        BindingResult bindingResult) {
        if (!personDTO.getPassword().equals(personDTO.getConfirmPassword()))
            bindingResult.rejectValue("confirmPassword", "",
                    "Пароли не совпадают");

        usernameValidator.validate(convertToPerson(personDTO), bindingResult);
        if (bindingResult.hasErrors())
            throw new PersonNotCreatedException(ErrorConstructUtil.constructErrorMessageToClient(bindingResult));

        registrationService.register(convertToPerson(personDTO));

        String token = jwtUtil.generateToken(convertToPerson(personDTO).getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Пользователь успешно зарегистрирован");
        response.put("jwt-token", token);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody @Valid AuthenticationDTO authenticationDTO,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("error",
                    ErrorConstructUtil.constructErrorMessageToClient(bindingResult)));
        }

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Неправильный логин или пароль"));
        }

        String newToken = jwtUtil.generateToken(authenticationDTO.getUsername());
        return ResponseEntity.ok().body(Map.of("jwt-token", newToken));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> personNotCreatedException(PersonNotCreatedException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return mapper.map(personDTO, Person.class);
    }
}
