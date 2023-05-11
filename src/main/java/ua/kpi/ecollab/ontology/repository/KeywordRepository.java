package ua.kpi.ecollab.ontology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.ecollab.ontology.entity.KeywordEntity;

import java.util.Set;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {
  Set<KeywordEntity> readAllByNameIn(Set<String> names);
}
