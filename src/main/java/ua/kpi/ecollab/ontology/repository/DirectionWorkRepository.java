package ua.kpi.ecollab.ontology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.ecollab.ontology.entity.DirectionWorkMappingEntity;
import ua.kpi.ecollab.ontology.entity.DirectionWorkMappingEntityId;

@Repository
public interface DirectionWorkRepository
    extends JpaRepository<DirectionWorkMappingEntity, DirectionWorkMappingEntityId> {}
