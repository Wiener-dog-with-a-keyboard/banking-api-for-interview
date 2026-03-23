package org.mkl;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    @Inject
    AccountService accountService;

    @POST
    public Response createAccount(CreateAccountRequest request) {
        Account account = accountService.create(request);
        AccountResponse response = new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getFirstName(),
                account.getLastName()
        );
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @POST
    @Path("/{accountNumber}/deposit")
    public AccountResponse deposit(@PathParam("accountNumber") String accountNumber, DepositRequest request) {
        Account account = accountService.deposit(accountNumber, request);
        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getFirstName(),
                account.getLastName()
        );
    }

    @POST
    @Path("/transfer")
    public Response transfer(TransferRequest request) {
        accountService.transfer(request);
        return Response.ok("transfer successful").build();
    }

    @GET
    @Path("/{accountNumber}/balance")
    public BalanceResponse getBalance(@PathParam("accountNumber") String accountNumber) {
        java.math.BigDecimal balance = accountService.getBalance(accountNumber);
        return new BalanceResponse(accountNumber, balance);
    }

    @GET
    @Path("/exchange-rate/{dkkAmount}")
    public ExchangeRateResponse getExchangeRate(@PathParam("dkkAmount") BigDecimal dkkAmount) {
        return accountService.convertDkkToUsd(dkkAmount);
    }
}