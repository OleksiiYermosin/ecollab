package ua.kpi.ecollab.ontology.service;

import ua.kpi.ecollab.ontology.dto.RecordDto;

import java.util.List;

public interface UploadService {

  List<RecordDto> readJsonFile();

  void saveRecords(List<RecordDto> records);
}
