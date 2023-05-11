package ua.kpi.ecollab.ontology.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordDto {

  private String title;

  private List<String> author;

  private String contributor;

  private LocalDateTime date;

  private String language;

  private String pages;

  private String publisher;

  private String file;

  private String type;

  private List<String> keywords;

  private List<String> directions;
}
