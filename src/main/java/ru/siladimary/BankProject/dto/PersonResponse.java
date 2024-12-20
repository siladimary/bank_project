package ru.siladimary.BankProject.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PersonResponse {
    private String firstName;
    private String lastName;
    private Integer age;
    private String username;
    private BigDecimal totalBalance;
    @JsonManagedReference
    private List<AccountDTO> accounts;
}
