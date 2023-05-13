package ua.kpi.ecollab.ontology.ontology;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.InfModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OntologyManager {

  private final InfModel infModel;

  @Autowired
  public OntologyManager(InfModel infModel) {
    this.infModel = infModel;
  }

  public Set<String> findSubDirections(Set<String> directions) {
    String filter =
        directions.stream()
            .map(direction -> "ecollab:" + direction)
            .collect(Collectors.joining(","));
    String queryString =
        """
          PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
          PREFIX owl: <http://www.w3.org/2002/07/owl#>
          PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
          PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
          PREFIX ecollab: <http://www.semanticweb.org/oleksii/ontologies/2023/3/ecollab#>
          SELECT ?direction ?value
          WHERE {
             ?direction rdf:type ecollab:Direction .
             ?direction ecollab:hasSubDirection ?value .
             FILTER(?direction in (%s))
          }""";
    Query query = QueryFactory.create(String.format(queryString, filter));
    return processResults(query);
  }

  public Set<String> findDirectSubDirections(String direction) {
    direction = "ecollab:" + direction;
    String queryString =
        """
              PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
              PREFIX owl: <http://www.w3.org/2002/07/owl#>
              PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
              PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
              PREFIX ecollab: <http://www.semanticweb.org/oleksii/ontologies/2023/3/ecollab#>
              SELECT ?direction ?value
              WHERE {
                 ?direction rdf:type ecollab:Direction .
                 ?direction ecollab:hasDirectSubDirection ?value .
                 FILTER(?direction in (%s))
              }""";
    Query query = QueryFactory.create(String.format(queryString, direction));
    return processResults(query);
  }

  private Set<String> processResults(Query query) {
    Set<String> resultList = new HashSet<>();
    try (QueryExecution queryExecution = QueryExecutionFactory.create(query, infModel)) {
      ResultSet results = queryExecution.execSelect();
      while (results.hasNext()) {
        QuerySolution solution = results.nextSolution();
        String[] parts = solution.get("value").toString().split("#");
        resultList.add(parts[parts.length - 1]);
      }
    }
    return resultList;
  }
}
