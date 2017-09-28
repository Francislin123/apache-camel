package com.walmart.feeds.api.core.repository.generator;

import com.walmart.feeds.api.core.repository.generator.model.GenerationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenerationHistoryRepository extends JpaRepository<GenerationHistoryEntity, UUID> {}
