package org.mkl;

import java.math.BigDecimal;

public interface ExchangeRateProvider {
    BigDecimal convertDkkToUsd(BigDecimal dkkAmount);
}