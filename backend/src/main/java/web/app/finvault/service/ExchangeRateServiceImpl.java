package web.app.finvault.service;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final RestTemplate restTemplate;

    private Map<String, Double> exchangeRates = new HashMap<>();
    private final Set<String> CURRENCIES = Set.of("USD", "EUR", "GBP", "JPY", "INR");

    @Value("${currencyApiKey}")
    private String API_KEY;
    @Value("${currencyApiUrl}")
    private String API_URL;

    public Map<String, Double> getExchangeRates() {
        var response = restTemplate.getForEntity(API_URL + API_KEY, JsonNode.class);
        var data = Objects.requireNonNull(response.getBody()).get("data");
        for(var currency : CURRENCIES) {
            exchangeRates.put(currency, data.get(currency).get("value").doubleValue());
        }
        log.info("Exchange rates fetched successfully{}", exchangeRates);
        return exchangeRates;
    }

}
