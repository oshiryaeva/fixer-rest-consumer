package com.shiryaeva.fixer.repository;

import com.shiryaeva.fixer.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {

    public CurrencyEntity findTopByOrderByIdDesc();
}