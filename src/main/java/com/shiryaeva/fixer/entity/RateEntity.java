package com.shiryaeva.fixer.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@Table(name = "target_rate")
@Entity
public class RateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "currency_entity_id", nullable = false)
    private CurrencyEntity currencyEntity;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "target", nullable = false)
    private Symbol target;

    @Column(name = "rate", nullable = false, precision = 19, scale = 2)
    private BigDecimal rate;

    @Override
    public String toString() {
        return String.format("%s : %s", target, rate);
    }
}