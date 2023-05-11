package ua.kpi.ecollab.ontology.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.ecollab.ontology.dto.RecordDto;
import ua.kpi.ecollab.ontology.service.UploadService;

import java.util.List;

import static ua.kpi.ecollab.ontology.common.PathConstants.*;

@RestController
@RequestMapping(QUERY_MAKER_ROOT_PATH)
public class CommonController {

  private final UploadService uploadService;

  @Autowired
  public CommonController(UploadService uploadService) {
    this.uploadService = uploadService;
  }

  @GetMapping(path = UPLOAD_SERVICE + "/read", produces = "application/json")
  public List<RecordDto> readJson() {
    List<RecordDto> records = uploadService.readJsonFile();
    uploadService.saveRecords(records);
    return uploadService.readJsonFile();
  }
}
