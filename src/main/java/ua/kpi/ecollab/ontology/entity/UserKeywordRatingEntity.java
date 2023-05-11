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
@Table(name = "user_keyword_rating")
public class UserKeywordRatingEntity implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "keyword_id")
  private KeywordEntity keyword;

  @Column(name = "value")
  private int rating;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserKeywordRatingEntity that = (UserKeywordRatingEntity) o;
    return id == that.id
        && rating == that.rating
        && Objects.equals(user, that.user)
        && Objects.equals(keyword, that.keyword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, keyword, rating);
  }
}
