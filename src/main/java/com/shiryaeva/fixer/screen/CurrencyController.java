package com.shiryaeva.fixer.screen;

import com.shiryaeva.fixer.entity.CurrencyEntity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@RestController
public class CurrencyController {

    @Autowired
    private Logger logger;

    @Autowired
    private EntityManager entityManager;

    @RequestMapping("/")
    public String latestRates() {
        logger.info("=======================");
        logger.info("Loading entries from database...");
        logger.info("=======================");

        EntityGraph<?> entityGraph = entityManager.getEntityGraph("currency-with-rates");
        List<CurrencyEntity> latestRates = entityManager
                .createQuery("select e from CurrencyEntity e where e.date = :date", CurrencyEntity.class)
                .setParameter("date", new Date())
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
        return latestRates.get(latestRates.size() - 1).toString();
    }

}
