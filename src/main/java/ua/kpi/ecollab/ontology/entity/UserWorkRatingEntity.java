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
@Table(name = "user_work_rating")
public class UserWorkRatingEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "work_id")
  private WorkEntity work;

  @Column(name = "rating")
  private int rating;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserWorkRatingEntity that = (UserWorkRatingEntity) o;
    return id == that.id
        && rating == that.rating
        && Objects.equals(user, that.user)
        && Objects.equals(work, that.work);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, work, rating);
  }
}
