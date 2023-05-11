package ua.kpi.ecollab.ontology.entity;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "works")
public class WorkEntity implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long workId;

  @Column(name = "title")
  private String name;

  @Column(name = "pages")
  private int pages;

  @Column(name = "file_url")
  private String fileUrl;

  @Column(name = "publication_date")
  private LocalDateTime publicationDate;

  @Column(name = "language")
  private String language;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private TypeEntity type;

  @Type(ListArrayType.class)
  @Column(name = "keywords", columnDefinition = "bigint[]")
  private List<Long> keywords;

  @Column(name = "rating")
  private int rating;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WorkEntity that = (WorkEntity) o;
    return workId == that.workId
        && pages == that.pages
        && rating == that.rating
        && Objects.equals(name, that.name)
        && Objects.equals(fileUrl, that.fileUrl)
        && Objects.equals(publicationDate, that.publicationDate)
        && Objects.equals(language, that.language)
        && Objects.equals(type, that.type)
        && Objects.equals(keywords, that.keywords);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        workId, name, pages, fileUrl, publicationDate, language, type, keywords, rating);
  }
}
