package com.fitcrew.FitCrewAppTrainings.services;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainings.converter.TrainingDocumentTrainingDtoConverter;
import com.fitcrew.FitCrewAppTrainings.converter.TrainingDtoTrainingModelConverter;
import com.fitcrew.FitCrewAppTrainings.dao.TrainingDao;
import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.enums.TrainingErrorMessageType;
import com.fitcrew.FitCrewAppTrainings.resolver.ErrorMsg;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainingService {

    private final TrainingDao trainingDao;
    private final TrainingDocumentTrainingDtoConverter trainingDocumentDtoConverter;
    private final TrainingDtoTrainingModelConverter trainingDtoModelConverter;

    TrainingService(TrainingDao trainingDao,
                    TrainingDocumentTrainingDtoConverter trainingDocumentDtoConverter,
                    TrainingDtoTrainingModelConverter trainingDtoModelConverter) {
        this.trainingDao = trainingDao;
        this.trainingDocumentDtoConverter = trainingDocumentDtoConverter;
        this.trainingDtoModelConverter = trainingDtoModelConverter;
    }

    Either<ErrorMsg, List<TrainingModel>> getTrainerTrainings(String trainerEmail) {

        return trainingDao.findByTrainerEmail(trainerEmail)
                .map(this::getTrainingDtos)
                .map(Either::<ErrorMsg, List<TrainingModel>>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_FOUND.toString())));
    }

    Either<ErrorMsg, TrainingModel> createTraining(TrainingDto trainingDto) {
        return Optional.ofNullable(trainingDocumentDtoConverter.trainingDtoToTrainingDocument(trainingDto))
                .map(trainingDao::save)
                .map(trainingDtoModelConverter::trainingDocumentToTrainingModel)
                .map(Either::<ErrorMsg, TrainingModel>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_SAVED.toString())));
    }

    Either<ErrorMsg, TrainingModel> deleteTraining(String trainingName,
												   String trainerEmail) {

        return trainingDao.findByTrainingNameAndTrainerEmail(trainingName, trainerEmail)
                .map(this::prepareSuccessfulTrainingDeleting)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_DELETED.toString())));
    }

    Either<ErrorMsg, TrainingModel> updateTraining(TrainingDto trainingDto,
												   String trainerEmail) {
        return trainingDao.findByTrainerEmail(trainerEmail)
                .map(trainings -> checkIfTrainingExistByName(trainingDto.getTrainingName(), trainings))
                .map(training -> prepareTrainingUpdate(trainingDto, training.get()))
                .orElseGet(() -> Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_UPDATED.toString())));
    }

    Either<ErrorMsg, TrainingModel> selectTraining(String trainerEmail,
												   String trainingName) {
        return trainingDao.findByTrainerEmail(trainerEmail)
                .map(trainings -> checkIfTrainingExistByName(trainingName, trainings))
                .map(training -> trainingDtoModelConverter.trainingDocumentToTrainingModel(training.get()))
                .map(this::checkEitherResponseForTraining)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_SELECTED.toString())));
    }

    Either<ErrorMsg, List<String>> clientsWhoBoughtTraining(String trainingName) {
        return trainingDao.findByTrainingName(trainingName)
                .map(TrainingDocument::getClients)
                .map(this::checkEitherResponseForClients)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_FOUND.toString())));
    }

    Either<ErrorMsg, List<String>> getAllTrainingsBoughtByClient(String clientName) {
        return Optional.ofNullable(trainingDao.findAll())
                .map(trainingDocuments -> getTrainingDocuments(clientName, trainingDocuments))
                .filter(trainingNames -> !trainingNames.isEmpty())
                .map(Either::<ErrorMsg, List<String>>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAININGS_BOUGHT.toString())));
    }

    private List<String> getTrainingDocuments(String clientName,
                                              List<TrainingDocument> trainingDocuments) {
        return trainingDocuments.stream()
                .map(trainingDocument -> checkIfClientNameEqualsWithAnyClientFromSingleTraining(clientName, trainingDocument))
                .collect(Collectors.toList());
    }

    private List<TrainingModel> getTrainingDtos(List<TrainingDocument> training) {
        return training.stream()
                .map(trainingDtoModelConverter::trainingDocumentToTrainingModel)
                .collect(Collectors.toList());
    }

    private Either<ErrorMsg, TrainingModel> prepareSuccessfulTrainingDeleting(TrainingDocument trainingDocument) {
        trainingDao.delete(trainingDocument);

        return checkEitherResponseForTraining(
                trainingDtoModelConverter.trainingDocumentToTrainingModel(trainingDocument)
        );
    }

    private String checkIfClientNameEqualsWithAnyClientFromSingleTraining(String clientName,
                                                                          TrainingDocument trainingDocument) {
        return trainingDocument.getClients().stream()
                .filter(client -> client.equals(clientName))
                .findFirst()
                .orElse(null);
    }

    private Either<ErrorMsg, List<String>> checkEitherResponseForClients(List<String> clients) {
        return Optional.ofNullable(clients)
                .filter(clientNames -> !clientNames.isEmpty())
                .map(Either::<ErrorMsg, List<String>>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_BOUGHT.toString())));
    }

    private Either<ErrorMsg, TrainingModel> prepareTrainingUpdate(TrainingDto trainingDto,
                                                                  TrainingDocument trainingDocument) {
        var trainingDocumentAfterUpdate = setNewValuesForTrainingDocument(trainingDto, trainingDocument);
        trainingDao.save(trainingDocumentAfterUpdate);

        return checkEitherResponseForTraining(
                trainingDtoModelConverter.trainingDocumentToTrainingModel(trainingDocumentAfterUpdate)
        );
    }

    private TrainingDocument setNewValuesForTrainingDocument(TrainingDto trainingDto,
                                                             TrainingDocument trainingDocument) {
        trainingDocument.setTrainingName(trainingDto.getTrainingName());
        trainingDocument.setDescription(trainingDto.getDescription());
        trainingDocument.setTraining(trainingDto.getTraining());
        trainingDocument.setTrainerEmail(trainingDto.getTrainerEmail());
        return trainingDocument;
    }

    private Either<ErrorMsg, TrainingDocument> checkIfTrainingExistByName(String trainingName,
                                                                          List<TrainingDocument> trainerTrainings) {
        return trainerTrainings.stream()
                .filter(trainingDocument -> trainingDocument.getTrainingName().equals(trainingName))
                .findFirst()
                .map(Either::<ErrorMsg, TrainingDocument>right)
                .orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_FOUND.toString())));
    }

    private Either<ErrorMsg, TrainingModel> checkEitherResponseForTraining(TrainingModel training) {
        return Optional.ofNullable(training)
                .map(Either::<ErrorMsg, TrainingModel>right)
                .orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
    }
}
