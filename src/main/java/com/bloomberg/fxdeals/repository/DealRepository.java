package com.bloomberg.fxdeals.repository;

import com.bloomberg.fxdeals.entity.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealRepository extends JpaRepository<DealEntity, java.util.UUID> {
    Optional<DealEntity> findByDealUniqueId(String dealUniqueId);
    boolean existsByDealUniqueId(String dealUniqueId);
}
