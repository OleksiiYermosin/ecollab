package ua.kpi.ecollab.ontology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.ecollab.ontology.entity.UserEntity;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Set<UserEntity> readAllByNameIn(Set<String> names);
}
