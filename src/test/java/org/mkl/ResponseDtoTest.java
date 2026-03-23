package org.mkl;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseDtoTest {

    @Test
    void accountResponseConstructor_setsFieldsAndCurrency() {
        // Arrange
        BigDecimal balance = new BigDecimal("150.00");

        // Act
        AccountResponse response = new AccountResponse("A1", balance, "Peter", "Petersen");

        // Assert
        assertEquals("A1", response.accountNumber);
        assertEquals(balance, response.balance);
        assertEquals("Peter", response.firstName);
        assertEquals("Petersen", response.lastName);
        assertEquals("DKK", response.currency);
    }

    @Test
    void balanceResponseConstructor_setsFieldsAndCurrency() {
        // Arrange
        BigDecimal balance = new BigDecimal("300.00");

        // Act
        BalanceResponse response = new BalanceResponse("A2", balance);

        // Assert
        assertEquals("A2", response.accountNumber);
        assertEquals(balance, response.balance);
        assertEquals("DKK", response.currency);
    }
}
