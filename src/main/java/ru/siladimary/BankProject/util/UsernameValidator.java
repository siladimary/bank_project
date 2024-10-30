package ru.siladimary.BankProject.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.services.PeopleService;

@Component
public class UsernameValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public UsernameValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleService.findByUsername(person.getUsername()).isPresent()) {
            errors.rejectValue("username", "",
                    "Такой логин уже существует");
        }
    }
}
