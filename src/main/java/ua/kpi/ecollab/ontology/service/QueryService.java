package ua.kpi.ecollab.ontology.service;

import ua.kpi.ecollab.ontology.entity.UserWorkEntity;

import java.util.Set;

public interface QueryService {
    Set<UserWorkEntity> processQuery(String query);

    Set<String> getDirectRootDirections(String direction);
}
