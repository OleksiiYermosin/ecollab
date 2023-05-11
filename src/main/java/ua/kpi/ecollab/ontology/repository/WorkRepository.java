package ua.kpi.ecollab.ontology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.ecollab.ontology.entity.WorkEntity;

@Repository
public interface WorkRepository extends JpaRepository<WorkEntity, Long> {}
