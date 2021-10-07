package com.shiryaeva.fixer.entity;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Set;


@NamedEntityGraph(
        name = "currency-with-rates",
        attributeNodes = {
                @NamedAttributeNode("date"),
                @NamedAttributeNode("timestamp"),
                @NamedAttributeNode("base"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "rates",
                        attributeNodes = {
                                @NamedAttributeNode("rateEntities")
                        }
                )
        }
)
@Getter
@Setter
@Table(name = "currency_rate")
@Entity
public class CurrencyEntity {

    @Autowired
    private Logger logger;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "timestamp")
    private Long timestamp;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "base", nullable = false)
    private Symbol base;

    @Column(name = "date", nullable = false)
    private Date date;

    @OneToMany(mappedBy = "currencyEntity", fetch = FetchType.EAGER)
    private Set<RateEntity> rateEntities;

    @Override
    public String toString() {
        String time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(timestamp);
        return String.format("%s %s %s", time, base, rateEntities.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CurrencyEntity e = (CurrencyEntity) obj;
        long diffHours = (e.timestamp - timestamp) / (60 * 60 * 1000);
        logger.info("=======================");
        logger.info("Diff = " + diffHours + " hours");
        logger.info("=======================");
        return base == e.base &&
                date == e.date &&
                diffHours < 2;
    }
}