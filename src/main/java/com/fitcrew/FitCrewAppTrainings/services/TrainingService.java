package com.fitcrew.FitCrewAppTrainings.services;

import com.fitcrew.FitCrewAppTrainings.dao.TrainingDao;
import com.fitcrew.FitCrewAppTrainings.domains.TrainingEntity;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.resolver.ErrorMsg;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainingService {

    private final TrainingDao trainingDao;
    private static long clientId = 1;

    public TrainingService(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Either<ErrorMsg, List<TrainingDto>> getClientsWhoBoughtTrainingFromTrainer(String trainerName) {
        List<TrainingEntity> trainingsBoughtByTheClient = trainingDao.findByCreatedBy(trainerName);

        if (!trainingsBoughtByTheClient.isEmpty()) {
            log.debug("List of trainings bought by the client {}", trainingsBoughtByTheClient);

            ModelMapper modelMapper = prepareModelMapper();

            return Either.right(
                    trainingsBoughtByTheClient.stream()
                            .map(trainingEntity -> modelMapper.map(trainingEntity, TrainingDto.class))
                            .collect(Collectors.toList())
            );

        } else {
            log.debug("No training bought by the client");
            return Either.left(new ErrorMsg("No training bought by the client"));
        }
    }

    private PropertyMap<TrainingDto, TrainingEntity> skipModifiedFieldsMap = new PropertyMap<TrainingDto, TrainingEntity>() {
        protected void configure() {
            skip().setId(clientId);
            clientId++;
        }
    };

    private ModelMapper prepareModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addMappings(skipModifiedFieldsMap);
        return modelMapper;
    }
}
