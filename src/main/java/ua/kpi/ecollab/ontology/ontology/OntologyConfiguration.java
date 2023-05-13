package ua.kpi.ecollab.ontology.ontology;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static ua.kpi.ecollab.ontology.common.PathConstants.ONTOLOGY_FILE_PATH;

@Configuration
public class OntologyConfiguration {

    @Value("classpath:" + ONTOLOGY_FILE_PATH)
    Resource resourceFile;

    private static final OntModel MODEL =
            ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
    @Bean
    public InfModel infModel() throws IOException {
        MODEL.read(resourceFile.getInputStream(), null);
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        return ModelFactory.createInfModel(reasoner, MODEL);
    }
}
