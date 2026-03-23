package org.mkl;

import java.math.BigDecimal;

public class BalanceResponse {
    public String accountNumber;
    public BigDecimal balance;
    public String currency;

    public BalanceResponse() {
    }

    public BalanceResponse(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = "DKK";
    }
}