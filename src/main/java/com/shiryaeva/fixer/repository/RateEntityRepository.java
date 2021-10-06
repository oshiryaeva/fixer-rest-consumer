package com.shiryaeva.fixer.repository;

import com.shiryaeva.fixer.entity.RateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateEntityRepository extends JpaRepository<RateEntity, Long> {
}