package org.mkl;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder({"DKK", "USD"})
public class ExchangeRateResponse {
    public BigDecimal DKK;
    public BigDecimal USD;

    public ExchangeRateResponse() {
    }

    public ExchangeRateResponse(BigDecimal DKK, BigDecimal USD) {
        this.DKK = DKK;
        this.USD = USD;
    }
}