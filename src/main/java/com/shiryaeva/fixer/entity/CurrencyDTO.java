package com.shiryaeva.fixer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.stream.Collectors;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyDTO {

    @Value("${rates}")
    public Map<String, BigDecimal> rates;
    @Value("${success}")
    private boolean success;
    @Value("${timestamp}")
    private Long timestamp;
    @Value("${date}")
    private Date date;
    @Value("${base}")
    private Symbol base;

    @Override
    public String toString() {
        return "Exchange rate for " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(timestamp) + System.lineSeparator() +
                "{" + System.lineSeparator() +
                "base = " + base + System.lineSeparator() +
                "rates : " + System.lineSeparator() +
                ratesToString(rates) + System.lineSeparator() +
                '}';
    }

    private String ratesToString(Map<String, BigDecimal> rates) {
        return rates.keySet().stream()
                .map(key -> key + " = " + rates.get(key) + System.lineSeparator())
                .collect(Collectors.joining("", "", ""));
    }

}