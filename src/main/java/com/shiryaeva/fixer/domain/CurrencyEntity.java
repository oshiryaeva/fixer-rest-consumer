package com.shiryaeva.fixer.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "currency_entity")
@Entity
public class CurrencyEntity {
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

    @OneToMany(mappedBy = "currencyEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ExchangeRate> exchangeRates;

}