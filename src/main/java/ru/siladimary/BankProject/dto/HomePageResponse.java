package ru.siladimary.BankProject.dto;

import lombok.Getter;
import java.math.BigDecimal;

import java.util.Map;

@Getter
public class HomePageResponse {
    private String greeting;
    private BigDecimal totalBalance;
    private Map<Integer, BigDecimal> accountInfo;

    public HomePageResponse(String greeting, BigDecimal totalBalance, Map<Integer, BigDecimal> accountInfo) {
        this.greeting = greeting;
        this.totalBalance = totalBalance;
        this.accountInfo = accountInfo;
    }
}

