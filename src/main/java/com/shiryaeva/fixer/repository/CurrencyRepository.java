package com.shiryaeva.fixer.repository;

import com.shiryaeva.fixer.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {

    public CurrencyEntity findTopByOrderByIdDesc();
}