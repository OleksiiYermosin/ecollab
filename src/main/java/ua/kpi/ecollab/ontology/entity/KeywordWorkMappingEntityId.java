package ua.kpi.ecollab.ontology.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class KeywordWorkMappingEntityId implements Serializable {
  @ManyToOne
  @JoinColumn(name = "keyword_id")
  private KeywordEntity keyword;

  @ManyToOne
  @JoinColumn(name = "work_id")
  private WorkEntity work;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    KeywordWorkMappingEntityId that = (KeywordWorkMappingEntityId) o;
    return Objects.equals(keyword, that.keyword) && Objects.equals(work, that.work);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keyword, work);
  }
}
