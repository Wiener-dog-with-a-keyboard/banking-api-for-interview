package org.mkl;

import java.math.BigDecimal;

public class AccountResponse {
    public String accountNumber;
    public BigDecimal balance;
    public String firstName;
    public String lastName;
    public String currency;

    public AccountResponse() {
    }

    public AccountResponse(String accountNumber, BigDecimal balance, String firstName, String lastName) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.firstName = firstName;
        this.lastName = lastName;
        this.currency = "DKK";
    }
}