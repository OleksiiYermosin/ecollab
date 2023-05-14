package ua.kpi.ecollab.ontology.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kpi.ecollab.ontology.entity.UserWorkEntity;
import ua.kpi.ecollab.ontology.ontology.OntologyManager;
import ua.kpi.ecollab.ontology.repository.DirectionWorkRepository;
import ua.kpi.ecollab.ontology.repository.UserWorkRepository;
import ua.kpi.ecollab.ontology.service.QueryService;

import java.util.*;
import java.util.function.Function;

@Service
public class QueryServiceImpl implements QueryService {

  private static final Set<Character> RESERVED_KEYS = Set.of('(', ')', '&', '|', '*', '#', '!');
  private static final Set<Character> TRIGGERS = Set.of('*', '#', '!');
  private static final int AUTHOR = 0;

  private final DirectionWorkRepository directionWorkRepository;
  private final UserWorkRepository userWorkRepository;
  private final OntologyManager ontologyManager;

  @Autowired
  public QueryServiceImpl(
      DirectionWorkRepository directionWorkRepository,
      OntologyManager ontologyManager,
      UserWorkRepository userWorkRepository) {
    this.directionWorkRepository = directionWorkRepository;
    this.ontologyManager = ontologyManager;
    this.userWorkRepository = userWorkRepository;
  }

  public Set<UserWorkEntity> processQuery(String query) {
    Set<UserWorkEntity> works = new LinkedHashSet<>();
    Set<String> bracketsList = new LinkedHashSet<>();
    StringBuilder literalBuilder = new StringBuilder();
    boolean isAnd = true;
    boolean bracketsModeEnabled = false;
    query = query.replaceAll(" ", "");
    for (int i = 0; i < query.toCharArray().length; i++) {
      if (!RESERVED_KEYS.contains(query.charAt(i))) {
        literalBuilder.append(query.charAt(i));
      } else if (query.charAt(i) == '&') {
        isAnd = true;
        bracketsList.add(literalBuilder.toString());
        literalBuilder = new StringBuilder();
      } else if (query.charAt(i) == '|') {
        isAnd = false;
        bracketsList.add(literalBuilder.toString());
        literalBuilder = new StringBuilder();
      } else if (query.charAt(i) == '(') {
        bracketsModeEnabled = true;
      } else if (query.charAt(i) == ')') {
        bracketsList.add(literalBuilder.toString());
        literalBuilder = new StringBuilder();
      } else if (TRIGGERS.contains(query.charAt(i))
          || (i + 1 == query.length()
              && (literalBuilder.length() != 0 || bracketsList.size() != 0))) {
        if (bracketsModeEnabled) {
          i = findDetails(bracketsList, works, i, query, isAnd);
          bracketsModeEnabled = false;
          bracketsList = new LinkedHashSet<>();
        } else {
          i = findDetails(Set.of(literalBuilder.toString()), works, i, query, true);
          literalBuilder = new StringBuilder();
        }
      }
    }
    return works;
  }

  public Set<String> getDirectSubDirections(String direction) {
    return ontologyManager.findDirectSubDirections(direction);
  }

  public String getRootDirection() {
    return ontologyManager.getRootDirection();
  }

  private int findDetails(
      Set<String> entryList,
      Set<UserWorkEntity> resultList,
      int currentPos,
      String query,
      boolean queryMode) {
    boolean isGlobalMode = false;
    boolean isStrictMode = false;
    int i;
    for (i = currentPos; i < query.length(); i++) {
      if (query.charAt(i) == '*') {
        isGlobalMode = true;
      }
      if (query.charAt(i) == '#') {
        isStrictMode = true;
      }
      if (query.charAt(i) == '!' || i + 1 == query.length()) {
        break;
      }
    }
    if (queryMode && isStrictMode) {
      resultList.addAll(
          executeQuery(entryList, isGlobalMode, directionWorkRepository::strictAndQuery));
    }
    if (queryMode && !isStrictMode) {
      resultList.addAll(executeQuery(entryList, isGlobalMode, directionWorkRepository::andQuery));
    }
    if (!queryMode && isStrictMode) {
      resultList.addAll(
          executeQuery(entryList, isGlobalMode, directionWorkRepository::strictOrQuery));
    }
    if (!queryMode && !isStrictMode) {
      resultList.addAll(executeQuery(entryList, isGlobalMode, directionWorkRepository::orQuery));
    }
    return i;
  }

  private Set<UserWorkEntity> executeQuery(
      Set<String> entries, boolean isGlobal, Function<Set<String>, Set<Long>> queryMapper) {
    if (isGlobal) {
      entries.addAll(ontologyManager.findSubDirections(entries));
    }
    return userWorkRepository.findAllByContextAndWork_WorkIdIn(AUTHOR, queryMapper.apply(entries));
  }
}
