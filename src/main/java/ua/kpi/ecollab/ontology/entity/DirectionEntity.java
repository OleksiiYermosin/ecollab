package ua.kpi.ecollab.ontology.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "directions")
public class DirectionEntity implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long directionId;

  @Column(name = "name")
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DirectionEntity that = (DirectionEntity) o;
    return directionId == that.directionId && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(directionId, name);
  }
}
