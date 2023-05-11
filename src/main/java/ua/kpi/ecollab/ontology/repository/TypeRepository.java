package ua.kpi.ecollab.ontology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.ecollab.ontology.entity.TypeEntity;

import java.util.Set;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity, Long> {
  Set<TypeEntity> readAllByNameIn(Set<String> names);
}
