package org.mkl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
//import java.math.RoundingMode;
import java.net.URL;

@ApplicationScoped
public class ExchangeRateApiProvider implements ExchangeRateProvider {

    @ConfigProperty(name = "exchange.rate.api.key")
    String apiKey;

    public BigDecimal convertDkkToUsd(BigDecimal dkkAmount) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new WebApplicationException("exchange rate API key is not configured.",Response.Status.INTERNAL_SERVER_ERROR);
        }

        if (dkkAmount == null || dkkAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new WebApplicationException("amount must be 0 or higher", Response.Status.BAD_REQUEST);
        }

        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/DKK/USD/" + dkkAmount.toPlainString();

        try (InputStream responseStream = new URL(url).openStream()) {
            JsonNode json = new ObjectMapper().readTree(responseStream);

            if ("success".equals(json.path("result").asText())) {
                return json.path("conversion_result").decimalValue();
            }

            String errorType = json.path("error-type").asText("unknown-error");
            throw new WebApplicationException("exchangerate api returned error: " + errorType, Response.Status.BAD_GATEWAY);
        } catch (IOException e) {
            throw new WebApplicationException("request failed to call exchangerate api", e, Response.Status.BAD_GATEWAY);
        }
    }
}