package com.shiryaeva.fixer.app;


import com.shiryaeva.fixer.entity.CurrencyDTO;
import com.shiryaeva.fixer.entity.CurrencyEntity;
import com.shiryaeva.fixer.entity.RateEntity;
import com.shiryaeva.fixer.entity.Symbol;
import com.shiryaeva.fixer.repository.CurrencyRepository;
import com.shiryaeva.fixer.repository.RateEntityRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyDataService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Logger logger;


    public void convertFixerResponseToData(CurrencyDTO currencyDTO) {
        logger.info("=======================");
        logger.info("Saving rates to database...");
        logger.info("=======================");
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setBase(currencyDTO.getBase());
        currencyEntity.setDate(currencyDTO.getDate());
        currencyEntity.setTimestamp(currencyDTO.getTimestamp());
        CurrencyRepository currencyRepository = context.getBean(CurrencyRepository.class);
        currencyRepository.save(currencyEntity);

        RateEntityRepository rateRepository = context.getBean(RateEntityRepository.class);
        for (Map.Entry<String, BigDecimal> entry : currencyDTO.getRates().entrySet()) {
            RateEntity rateEntity = new RateEntity();
            rateEntity.setTarget(Symbol.fromId(entry.getKey()));
            rateEntity.setRate(entry.getValue());
            rateEntity.setCurrencyEntity(currencyEntity);
            rateRepository.save(rateEntity);
        }
    }
}
