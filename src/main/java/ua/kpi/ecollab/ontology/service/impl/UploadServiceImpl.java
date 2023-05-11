package ua.kpi.ecollab.ontology.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.ecollab.ontology.dto.RecordDto;
import ua.kpi.ecollab.ontology.entity.*;
import ua.kpi.ecollab.ontology.repository.*;
import ua.kpi.ecollab.ontology.service.UploadService;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ua.kpi.ecollab.ontology.common.PathConstants.JSON_FILE_PATH;

@Service
public class UploadServiceImpl implements UploadService {

  private final PasswordEncoder passwordEncoder;
  private final DirectionRepository directionRepository;
  private final DirectionWorkRepository directionWorkRepository;
  private final KeywordRepository keywordRepository;
  private final KeywordWorkRepository keywordWorkRepository;
  private final TypeRepository typeRepository;
  private final UserKeywordRatingRepository userKeywordRatingRepository;
  private final UserRepository userRepository;
  private final UserWorkRatingRepository userWorkRatingRepository;
  private final UserWorkRepository userWorkRepository;
  private final WorkRepository workRepository;

  @Value("classpath:" + JSON_FILE_PATH)
  Resource resourceFile;

  @Autowired
  public UploadServiceImpl(
      PasswordEncoder passwordEncoder,
      DirectionRepository directionRepository,
      DirectionWorkRepository directionWorkRepository,
      KeywordRepository keywordRepository,
      KeywordWorkRepository keywordWorkRepository,
      TypeRepository typeRepository,
      UserKeywordRatingRepository userKeywordRatingRepository,
      UserRepository userRepository,
      UserWorkRatingRepository userWorkRatingRepository,
      UserWorkRepository userWorkRepository,
      WorkRepository workRepository) {
    this.passwordEncoder = passwordEncoder;
    this.directionRepository = directionRepository;
    this.directionWorkRepository = directionWorkRepository;
    this.keywordRepository = keywordRepository;
    this.keywordWorkRepository = keywordWorkRepository;
    this.typeRepository = typeRepository;
    this.userKeywordRatingRepository = userKeywordRatingRepository;
    this.userRepository = userRepository;
    this.userWorkRatingRepository = userWorkRatingRepository;
    this.userWorkRepository = userWorkRepository;
    this.workRepository = workRepository;
  }

  public List<RecordDto> readJsonFile() {
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    try (JsonParser parser = objectMapper.createParser(resourceFile.getFile())) {
      return objectMapper.readValue(parser, new TypeReference<List<RecordDto>>() {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Transactional
  public void saveRecords(List<RecordDto> records) {
    Map<String, TypeEntity> typesMap =
        saveEntities(
            records,
            (RecordDto r) -> Set.of(r.getType()),
            typeRepository::readAllByNameIn,
            name -> typeRepository.save(TypeEntity.builder().name(name).build()),
            TypeEntity::getName);
    Map<String, DirectionEntity> directionsMap =
        saveEntities(
            records,
            (RecordDto r) -> new HashSet<>(r.getDirections()),
            directionRepository::readAllByNameIn,
            name -> directionRepository.save(DirectionEntity.builder().name(name).build()),
            DirectionEntity::getName);
    Map<String, KeywordEntity> keywordsMap =
        saveEntities(
            records,
            (RecordDto r) -> new HashSet<>(r.getKeywords()),
            keywordRepository::readAllByNameIn,
            name -> keywordRepository.save(KeywordEntity.builder().name(name).build()),
            KeywordEntity::getName);
    Map<String, UserEntity> userMap =
        saveEntities(
            records,
            (RecordDto r) ->
                Sets.union(
                    new HashSet<>(r.getAuthor()),
                    new HashSet<>(Collections.singleton(r.getContributor()))),
            userRepository::readAllByNameIn,
            name -> {
              if (name == null || name.isEmpty()) {
                return new UserEntity();
              }
              String[] nameDetail = name.split(" ");
              String username = generateUniqueString(6);
              return userRepository.save(
                  UserEntity.builder()
                      .name(name)
                      .firstName(nameDetail[1])
                      .middleName(nameDetail[2])
                      .lastName(nameDetail[0].replaceAll(",", ""))
                      .email(username + "@gmail.com")
                      .username(username)
                      .password(passwordEncoder.encode(username))
                      .build());
            },
            UserEntity::getName);

    Map<WorkEntity, RecordDto> workMap =
        records.stream()
            .map(
                r ->
                    Pair.of(
                        workRepository.save(
                            WorkEntity.builder()
                                .name(r.getTitle())
                                .pages(
                                    r.getPages() == null
                                        ? 0
                                        : Integer.parseInt(r.getPages().split(" ")[0]))
                                .fileUrl(r.getFile())
                                .publicationDate(r.getDate())
                                .language(r.getLanguage())
                                .type(typesMap.get(r.getType()))
                                .keywords(
                                    r.getKeywords().stream()
                                        .map(keyword -> keywordsMap.get(keyword).getId())
                                        .collect(Collectors.toList()))
                                .rating(0)
                                .build()),
                        r))
            .collect(Pair.toMap());
    for (Map.Entry<WorkEntity, RecordDto> entry : workMap.entrySet()) {
      entry
          .getValue()
          .getAuthor()
          .forEach(
              author ->
                  userWorkRepository.save(
                      UserWorkEntity.builder()
                          .work(entry.getKey())
                          .user(userMap.get(author))
                          .context(0)
                          .build()));
      if (entry.getValue().getContributor() != null && userMap.get(entry.getValue().getContributor()) != null) {
        userWorkRepository.save(
            UserWorkEntity.builder()
                .work(entry.getKey())
                .user(userMap.get(entry.getValue().getContributor()))
                .context(1)
                .build());
      }
    }
    for (Map.Entry<WorkEntity, RecordDto> entry : workMap.entrySet()) {
      entry
          .getValue()
          .getKeywords()
          .forEach(
              keyword ->
                  keywordWorkRepository.save(
                      KeywordWorkMappingEntity.builder()
                          .entityId(
                              KeywordWorkMappingEntityId.builder()
                                  .work(entry.getKey())
                                  .keyword(keywordsMap.get(keyword))
                                  .build())
                          .build()));
    }
    for (Map.Entry<WorkEntity, RecordDto> entry : workMap.entrySet()) {
      entry
          .getValue()
          .getDirections()
          .forEach(
              direction ->
                  directionWorkRepository.save(
                      DirectionWorkMappingEntity.builder()
                          .entityId(
                              DirectionWorkMappingEntityId.builder()
                                  .work(entry.getKey())
                                  .direction(directionsMap.get(direction))
                                  .build())
                          .build()));
    }
  }

  private <T> Map<String, T> saveEntities(
      List<RecordDto> records,
      Function<RecordDto, Set<String>> uniqueValueExtractor,
      Function<Set<String>, Set<T>> existingValuesGetter,
      Function<String, T> entitiesSaver,
      Function<T, String> keyGetter) {
    Set<String> entitiesToBeCreated =
        records.stream()
            .flatMap(r -> uniqueValueExtractor.apply(r).stream())
            .collect(Collectors.toSet());
    Set<T> existingEntities = existingValuesGetter.apply(entitiesToBeCreated);
    entitiesToBeCreated = Sets.difference(entitiesToBeCreated, existingEntities);
    return Stream.concat(existingEntities.stream(), entitiesToBeCreated.stream().map(entitiesSaver))
        .collect(Collectors.toMap(keyGetter, Function.identity()));
  }

  private String generateUniqueString(int desiredLength) {
    return UUID.randomUUID().toString().substring(0, desiredLength);
  }
}
