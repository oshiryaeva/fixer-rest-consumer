package com.shiryaeva.fixer.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@Table(name = "exchange_rate")
@Entity
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "currency_entity_id", nullable = false)
    private CurrencyEntity currencyEntity;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "target", nullable = false)
    private Symbol target;
    @Column(name = "rate", nullable = false, precision = 19, scale = 2)
    private BigDecimal rate;

    public ExchangeRate(Symbol target, BigDecimal rate) {
        this.target = target;
        this.rate = rate;
    }


}