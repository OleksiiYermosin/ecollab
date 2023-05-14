package ua.kpi.ecollab.ontology.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.kpi.ecollab.ontology.dto.RecordDto;
import ua.kpi.ecollab.ontology.entity.UserWorkEntity;
import ua.kpi.ecollab.ontology.service.QueryService;
import ua.kpi.ecollab.ontology.service.UploadService;

import java.util.List;
import java.util.Set;

import static ua.kpi.ecollab.ontology.common.PathConstants.*;

@RestController
@RequestMapping(QUERY_MAKER_ROOT_PATH)
public class CommonController {

  private final UploadService uploadService;
  private final QueryService queryService;

  @Autowired
  public CommonController(UploadService uploadService, QueryService queryService) {
    this.uploadService = uploadService;
    this.queryService = queryService;
  }

  @PostMapping(
      path = UPLOAD_SERVICE + "/create",
      consumes = "application/json",
      produces = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public List<RecordDto> readJson() {
    List<RecordDto> records = uploadService.readJsonFile();
    uploadService.saveRecords(records);
    return uploadService.readJsonFile();
  }

  @GetMapping(path = ONTOLOGY_SERVICE + "/runQuery", produces = "application/json")
  @CrossOrigin(origins = "http://localhost:3000")
  public Set<UserWorkEntity> readOntology() {
    return queryService.processQuery("(DistributedSystems)*");
  }

  @GetMapping(path = ONTOLOGY_SERVICE + "/getRootDirection", produces = "application/json")
  @CrossOrigin(origins = "http://localhost:3000")
  public String readDirectRootDirection() {
    return "{\"direction\" : \"" + queryService.getRootDirection() + "\"}";
  }

  @GetMapping(path = ONTOLOGY_SERVICE + "/getDirectSubDirections/{direction}", produces = "application/json")
  @CrossOrigin(origins = "http://localhost:3000")
  public Set<String> readSubDirections(@PathVariable String direction) {
    return queryService.getDirectSubDirections(direction);
  }
}
