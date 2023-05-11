package ua.kpi.ecollab.ontology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.ecollab.ontology.entity.DirectionEntity;

import java.util.Set;

@Repository
public interface DirectionRepository extends JpaRepository<DirectionEntity, Long> {
  Set<DirectionEntity> readAllByNameIn(Set<String> names);
}
