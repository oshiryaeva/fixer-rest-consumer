package com.shiryaeva.fixer.screen;

import com.shiryaeva.fixer.app.ConverterService;
import com.shiryaeva.fixer.entity.CurrencyEntity;
import com.shiryaeva.fixer.entity.RateEntity;
import com.shiryaeva.fixer.entity.Symbol;
import com.shiryaeva.fixer.util.DateUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping("/")
    public String getHelp() {
        StringBuilder builder = new StringBuilder();
        builder.append("Please enter an endpoint. Here's the quick reference: ");
        builder.append("<br>");
        builder.append("/latest for getting all latest EUR rates");
        builder.append("<br>");
        builder.append("/date/{date} (in any format) for getting all rates for this date");
        builder.append("<br>");
        builder.append("/date/{date}/{symbol} (date in any format) for getting {symbol} rate for this date");
        builder.append("<br>");
        builder.append("/symbol/{symbol} for getting {symbol} latest rate");
        builder.append("<br>");
        builder.append("/{symbol}/{amount} to convert the entered amount of {symbol} currency with the latest rate");
        builder.append("<br>");
        builder.append("/anything/else to get this app down");
        logger.info(builder.toString());
        return builder.toString();
    }

    @RequestMapping("/latest")
    public String latestRates() {
        logger.info("=======================");
        logger.info("Loading today's EUR rates...");
        logger.info("=======================");
        return getRatesByDate(new Date()).toString();
    }

    @GetMapping(value = "/date/{date}")
    public String findByDate(@PathVariable("date") String date) {
        try {
            Date realDate = parseDate(date);
            logger.info("=======================");
            logger.info("Loading rates for your date...");
            logger.info("=======================");
            return getRatesByDate(realDate).toString();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, no way", e);
        }
    }

    @RequestMapping(value = "/symbol/{symbol}")
    public BigDecimal getLatestForTarget(@PathVariable String symbol) {
        logger.info("=======================");
        logger.info("Loading " + symbol + " rates...");
        logger.info("=======================");
        return converter.convertCurrency(Symbol.fromId(symbol), 1);
    }

    @RequestMapping(value = "/{symbol}/{amount}")
    public BigDecimal convertToBase(@PathVariable String symbol, @PathVariable int amount) {
        logger.info("=======================");
        logger.info("Converting " + amount + " " + symbol + " to EUR...");
        logger.info("=======================");
        return converter.convertCurrency(Symbol.fromId(symbol), amount);
    }

    @GetMapping(value = "/date/{date}/{symbol}")
    public String findTargetByDate(@PathVariable("date") String date, @PathVariable String symbol) {
        try {
            logger.info("=======================");
            logger.info("Loading " + symbol + " rates for your date...");
            logger.info("=======================");
            Date realDate = parseDate(date);
            CurrencyEntity entity = getRatesByDate(realDate);
            RateEntity targetRate = entity.getRateEntities()
                    .stream()
                    .filter(rateEntity -> Symbol.fromId(symbol).equals(rateEntity.getTarget()))
                    .findAny()
                    .orElse(null);
            return targetRate.toString();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, no way", e);
        }
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

    private Date parseDate(String stringDate) {
        try {
            String dateFormat = DateUtil.determineDateFormat(stringDate);
            if (dateFormat != null) {
                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                Date realDate = formatter.parse(stringDate);
                return realDate;
            }
        } catch (ParseException e) {
            logger.info("Date cannot be parsed");
            logger.info(e.getLocalizedMessage());
        }
        return null;
    }
}
