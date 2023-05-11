package ua.kpi.ecollab.ontology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.ecollab.ontology.entity.KeywordWorkMappingEntity;
import ua.kpi.ecollab.ontology.entity.KeywordWorkMappingEntityId;

@Repository
public interface KeywordWorkRepository
    extends JpaRepository<KeywordWorkMappingEntity, KeywordWorkMappingEntityId> {}
