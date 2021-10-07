package com.shiryaeva.fixer.app;

import com.shiryaeva.fixer.entity.CurrencyEntity;
import com.shiryaeva.fixer.entity.RateEntity;
import com.shiryaeva.fixer.entity.Symbol;
import com.shiryaeva.fixer.repository.CurrencyRepository;
import com.shiryaeva.fixer.repository.RateEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ConverterService {

    @Autowired
    private ApplicationContext context;

    public BigDecimal convertCurrency(Symbol target, int amount) {
        CurrencyRepository currencyRepository = context.getBean(CurrencyRepository.class);
        CurrencyEntity currencyEntity = currencyRepository.findTopByOrderByIdDesc();
        RateEntity rate = currencyEntity.getRateEntities()
                .stream()
                .filter(rateEntity -> target.equals(rateEntity.getTarget()))
                .findFirst()
                .orElse(null);
        if (rate != null) {
            return BigDecimal.valueOf(amount).divide(rate.getRate(), 2, RoundingMode.HALF_UP);
        } else
            return BigDecimal.ZERO;
    }
}
