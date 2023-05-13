package ua.kpi.ecollab.ontology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.ecollab.ontology.entity.UserWorkEntity;

import java.util.Set;

@Repository
public interface UserWorkRepository extends JpaRepository<UserWorkEntity, Long> {

    Set<UserWorkEntity> findAllByContextAndWork_WorkIdIn(int context, Set<Long> idList);

}
