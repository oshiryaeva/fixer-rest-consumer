package com.shiryaeva.fixer.logic;


import com.shiryaeva.fixer.domain.CurrencyEntity;
import com.shiryaeva.fixer.domain.CurrencyRepository;
import com.shiryaeva.fixer.domain.ExchangeRate;
import com.shiryaeva.fixer.domain.Symbol;
import com.shiryaeva.fixer.response.FixerResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DomainService {

    @Autowired
    private Logger logger;

    @Autowired
    private ApplicationContext context;

    public void convertFixerResponseToData(FixerResponse fixerResponse) {
        Map<String, BigDecimal> rateResponse = fixerResponse.getRates();
//        ObjectMapper mapper = new ObjectMapper();
        CurrencyEntity currency = new CurrencyEntity();
        currency.setBase(Symbol.fromId(fixerResponse.getBase()));
        currency.setDate(fixerResponse.getDate());
        currency.setTimestamp(fixerResponse.getTimestamp());
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : rateResponse.entrySet()) {
//            RateResponse response = mapper.convertValue(rateResponse, RateResponse.class);
            ExchangeRate rate = new ExchangeRate(Symbol.fromId(entry.getKey()), entry.getValue());
            rate.setCurrencyEntity(currency);
            exchangeRates.add(rate);
        }
        currency.setExchangeRates(exchangeRates);

        CurrencyRepository repository = context.getBean(CurrencyRepository.class);
//        ExchangeRateRepository rateRepository = context.getBean(ExchangeRateRepository.class);
        repository.save(currency);
    }
}
