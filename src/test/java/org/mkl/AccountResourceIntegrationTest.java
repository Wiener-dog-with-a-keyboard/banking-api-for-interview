package org.mkl;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class AccountResourceIntegrationTest {

    @Test
    void createAccount_returnsCreatedAccount() {
        // Arrange
        String accountNumber = uniqueAccount("acc-create");
        String body = "{\"accountNumber\":\"" + accountNumber + "\",\"firstName\":\"Peter\",\"lastName\":\"Petersen\",\"initialBalance\":500}";

        // Act
        Response response = given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/accounts");

        // Assert
        response.then().statusCode(201);
        assertEquals(accountNumber, response.jsonPath().getString("accountNumber"));
        assertEquals("DKK", response.jsonPath().getString("currency"));
    }

    @Test
    void deposit_updatesBalance() {
        // Arrange
        String accountNumber = uniqueAccount("acc-deposit");
        createAccount(accountNumber, 1000);

        // Act
        Response response = given()
                .contentType("application/json")
                .body("{\"amount\":250}")
                .when()
                .post("/accounts/" + accountNumber + "/deposit");

        // Assert
        response.then().statusCode(200);
        BigDecimal balance = new BigDecimal(response.jsonPath().getString("balance"));
        assertEquals(0, balance.compareTo(new BigDecimal("1250")));
    }

    @Test
    void transfer_movesMoneyBetweenAccounts() {
        // Arrange
        String fromAccount = uniqueAccount("acc-from");
        String toAccount = uniqueAccount("acc-to");
        createAccount(fromAccount, 500);
        createAccount(toAccount, 100);

        // Act
        given()
                .contentType("application/json")
                .body("{\"fromAccountNumber\":\"" + fromAccount + "\",\"toAccountNumber\":\"" + toAccount + "\",\"amount\":200}")
                .when()
                .post("/accounts/transfer")
                .then()
                .statusCode(200);

        Response fromBalanceResponse = given()
                .when()
                .get("/accounts/" + fromAccount + "/balance");

        Response toBalanceResponse = given()
                .when()
                .get("/accounts/" + toAccount + "/balance");

        // Assert
        BigDecimal fromBalance = new BigDecimal(fromBalanceResponse.jsonPath().getString("balance"));
        BigDecimal toBalance = new BigDecimal(toBalanceResponse.jsonPath().getString("balance"));

        assertEquals(0, fromBalance.compareTo(new BigDecimal("300")));
        assertEquals(0, toBalance.compareTo(new BigDecimal("300")));
    }

    private void createAccount(String accountNumber, int initialBalance) {
        String body = "{\"accountNumber\":\"" + accountNumber + "\",\"firstName\":\"Test\",\"lastName\":\"User\",\"initialBalance\":" + initialBalance + "}";

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/accounts")
                .then()
                .statusCode(201);
    }

    private String uniqueAccount(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
