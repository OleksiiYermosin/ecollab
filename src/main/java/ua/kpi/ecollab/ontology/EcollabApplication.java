package ua.kpi.ecollab.ontology;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"ua.*"})
public class EcollabApplication {

  public static void main(String[] args) {
    SpringApplication.run(EcollabApplication.class, args);
  }
}
