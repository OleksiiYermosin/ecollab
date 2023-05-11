package ua.kpi.ecollab.ontology.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "directions_works")

public class DirectionWorkMappingEntity implements Serializable {
  @EmbeddedId DirectionWorkMappingEntityId entityId;

  @Transient
  public DirectionEntity getDirectionEntity() {
    return entityId.getDirection();
  }

  @Transient
  public WorkEntity getWorkEntity() {
    return entityId.getWork();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DirectionWorkMappingEntity that = (DirectionWorkMappingEntity) o;
    return Objects.equals(entityId, that.entityId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entityId);
  }
}
