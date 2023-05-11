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
@Table(name = "users_works")
public class UserWorkEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "work_id")
  private WorkEntity work;

  @Column(name = "context")
  private int context;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserWorkEntity that = (UserWorkEntity) o;
    return id == that.id
        && context == that.context
        && Objects.equals(user, that.user)
        && Objects.equals(work, that.work);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, work, context);
  }
}
