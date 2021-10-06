package com.shiryaeva.fixer.screen;

import com.shiryaeva.fixer.app.ConverterService;
import com.shiryaeva.fixer.entity.CurrencyEntity;
import com.shiryaeva.fixer.entity.Symbol;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/")
class CurrencyController {

    @Autowired
    private Logger logger;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ConverterService converter;


    @RequestMapping("/latest")
    public String latestRates() {
        logger.info("=======================");
        logger.info("Loading today's EUR rates...");
        logger.info("=======================");
        return getLatestRates().toString();
    }

    @GetMapping(value = "/{date}", produces = "application/json")
    public String findByDate(@PathVariable("date") String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date realDate = formatter.parse(date);
            return getRatesByDate(realDate).toString();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, no way", e);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Sorry, date cannot be parsed", e);
        }

    }

    @RequestMapping(value = "/{symbol}/{amount}")
    public BigDecimal convertToBase(@PathVariable String symbol, @PathVariable int amount) {
       return converter.convertCurrency(Symbol.fromId(symbol), amount);
    }

    private CurrencyEntity getLatestRates() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("currency-with-rates");
        List<CurrencyEntity> latestRates = entityManager
                .createQuery("select e from CurrencyEntity e where e.date = :date", CurrencyEntity.class)
                .setParameter("date", new Date())
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
        return latestRates.get(latestRates.size() - 1);
    }

    private CurrencyEntity getRatesByDate(Date date) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("currency-with-rates");
        List<CurrencyEntity> ratesByDate = entityManager
                .createQuery("select e from CurrencyEntity e where e.date = :date", CurrencyEntity.class)
                .setParameter("date", date)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
        return ratesByDate.get(ratesByDate.size() - 1);
    }


}
