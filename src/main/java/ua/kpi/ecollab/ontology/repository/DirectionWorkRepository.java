package ua.kpi.ecollab.ontology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kpi.ecollab.ontology.entity.DirectionWorkMappingEntity;
import ua.kpi.ecollab.ontology.entity.DirectionWorkMappingEntityId;

import java.util.Set;

@Repository
public interface DirectionWorkRepository
    extends JpaRepository<DirectionWorkMappingEntity, DirectionWorkMappingEntityId> {

  @Query(
      nativeQuery = true,
      value =
          """
                 SELECT work_id
                  FROM directions_works
                  GROUP BY work_id
                  HAVING array_agg(direction_id ORDER BY direction_id) =
                         (SELECT id
                          FROM (SELECT array_agg(id ORDER BY id) AS id, 1 AS g
                                FROM directions
                                WHERE directions.name
                                          IN ?1
                                GROUP BY g) AS temp);
                  """)
  Set<Long> strictAndQuery(Set<String> names);

  @Query(
      nativeQuery = true,
      value =
          """
                  SELECT work_id
                  FROM directions_works
                  GROUP BY work_id
                  HAVING array_agg(direction_id ORDER BY direction_id) &&
                         (SELECT id
                          FROM (SELECT array_agg(id ORDER BY id) AS id, 1 AS g
                                FROM directions
                                WHERE directions.name
                                          IN ?1
                                GROUP BY g) AS temp);
                   """)
  Set<Long> andQuery(Set<String> names);

  @Query(
      nativeQuery = true,
      value =
          """
                  SELECT DISTINCT tmp.work_id
                                    FROM (SELECT work_id
                                          FROM directions_works
                                          GROUP BY work_id
                                          HAVING array_length(array_agg(direction_id), 1) = 1) AS tmp
                                             JOIN directions_works AS dw ON dw.work_id = tmp.work_id
                                    WHERE dw.direction_id = ANY (SELECT id
                                                                 FROM directions
                                                                 WHERE name IN ?1)
                                    ORDER BY work_id;""")
  Set<Long> strictOrQuery(Set<String> names);

  @Query(
      nativeQuery = true,
      value =
          """
          SELECT DISTINCT work_id
          FROM directions_works
          WHERE direction_id = ANY(
              SELECT
                  id
              FROM
                  directions
              WHERE
                 name IN ?
          ) ORDER BY work_id""")
  Set<Long> orQuery(Set<String> names);
}
