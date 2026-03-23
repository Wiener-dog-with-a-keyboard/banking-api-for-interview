package org.mkl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@ApplicationScoped
public class AccountService {

    @Inject
    EntityManager entityManager;

    @Inject
    ExchangeRateProvider exchangeRateProvider;

    @Transactional
    public Account create(CreateAccountRequest request) {
        if (request == null) {
            throw new WebApplicationException("request body missing", Response.Status.BAD_REQUEST);
        }

        if (isBlank(request.accountNumber) || isBlank(request.firstName) || isBlank(request.lastName)) {
            throw new WebApplicationException("account number and first name and last name are all required", Response.Status.BAD_REQUEST);
        }

        if (findByAccountNumber(request.accountNumber) != null) {
            throw new WebApplicationException("the account number already exists", Response.Status.CONFLICT);
        }

        BigDecimal startBalance = request.initialBalance;
        if (startBalance == null) {
            throw new WebApplicationException("the initial balance must be set", Response.Status.BAD_REQUEST);
        }

        if (startBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new WebApplicationException("the initial balance must be 0 ddk or higher", Response.Status.BAD_REQUEST);
        }

        Account account = new Account();
        account.setAccountNumber(request.accountNumber);
        account.setFirstName(request.firstName);
        account.setLastName(request.lastName);
        account.setBalance(startBalance);

        entityManager.persist(account);
        return account;
    }

    @Transactional
    public Account deposit(String accountNumber, DepositRequest request) {
        if (request == null || request.amount == null) {
            throw new WebApplicationException("amount is required", Response.Status.BAD_REQUEST);
        }

       if (request.amount.compareTo(BigDecimal.ZERO) <= 0) {
           throw new WebApplicationException("amount must be greater than 0", Response.Status.BAD_REQUEST);
       }

        Account account = findRequired(accountNumber);
        account.setBalance(account.getBalance().add(request.amount));
        account.getBalance();

        return account;
    }

    @Transactional
    public void transfer(TransferRequest request) {
        if (request == null) {
            throw new WebApplicationException("request body missing", Response.Status.BAD_REQUEST);
        }

        if (isBlank(request.fromAccountNumber) || isBlank(request.toAccountNumber) || request.amount == null) {
            throw new WebApplicationException("from which account number, and to which account number and amount transfered are required", Response.Status.BAD_REQUEST);
        }

        if (request.fromAccountNumber.equals(request.toAccountNumber)) {
            throw new WebApplicationException("Cannot transfer to the same account", Response.Status.BAD_REQUEST);
        }

       if (request.amount.compareTo(BigDecimal.ZERO) <= 0) {
           throw new WebApplicationException("amount must be greater than 0", Response.Status.BAD_REQUEST);
       }

        Account fromAccount = findRequired(request.fromAccountNumber);
        Account toAccount = findRequired(request.toAccountNumber);

        if (fromAccount.getBalance().compareTo(request.amount) < 0) {
            throw new WebApplicationException("not enough money to transfer", Response.Status.BAD_REQUEST);
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount));
        toAccount.setBalance(toAccount.getBalance().add(request.amount));
    }

    public BigDecimal getBalance(String accountNumber) {
        return findRequired(accountNumber).getBalance();
    }

    public ExchangeRateResponse convertDkkToUsd(BigDecimal dkkAmount) {
        if (dkkAmount == null) {
            throw new WebApplicationException("dkk query parameter is required", Response.Status.BAD_REQUEST);
        }

        BigDecimal usdAmount = exchangeRateProvider.convertDkkToUsd(dkkAmount);
        return new ExchangeRateResponse(scaleCurrency(dkkAmount), scaleCurrency(usdAmount));
    }

    private Account findByAccountNumber(String accountNumber) {
        List<Account> results = entityManager.createQuery(
                "SELECT wantedAccount FROM Account wantedAccount WHERE wantedAccount.accountNumber = :accountNumber",
                Account.class
        ).setParameter("accountNumber", accountNumber).getResultList();

        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    private Account findRequired(String accountNumber) {
        Account account = findByAccountNumber(accountNumber);
        if (account == null) {
            throw new WebApplicationException("Account not found: " + accountNumber, Response.Status.NOT_FOUND);
        }
        return account;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private BigDecimal scaleCurrency(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}