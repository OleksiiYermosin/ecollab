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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "keywords_works")
public class KeywordWorkMappingEntity implements Serializable {
  @EmbeddedId KeywordWorkMappingEntityId entityId;

  @Transient
  public KeywordEntity getDirectionEntity() {
    return entityId.getKeyword();
  }

  @Transient
  public WorkEntity getWorkEntity() {
    return entityId.getWork();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    KeywordWorkMappingEntity that = (KeywordWorkMappingEntity) o;
    return Objects.equals(entityId, that.entityId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entityId);
  }
}
