package com.fitcrew.FitCrewAppTrainings.services;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.converter.TrainingDocumentTrainingDtoConverter;
import com.fitcrew.FitCrewAppTrainings.dao.TrainingDao;
import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;
import com.fitcrew.FitCrewAppTrainings.enums.TrainingErrorMessageType;
import com.fitcrew.FitCrewAppTrainings.resolver.ErrorMsg;
import com.google.common.collect.Lists;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainingService {

	private final TrainingDao trainingDao;
	private final TrainingDocumentTrainingDtoConverter trainingConverter;

	TrainingService(TrainingDao trainingDao,
					TrainingDocumentTrainingDtoConverter trainingConverter) {
		this.trainingDao = trainingDao;
		this.trainingConverter = trainingConverter;
	}

	public Either<ErrorMsg, List<TrainingDto>> getTrainerTrainings(String trainerEmail) {

		return trainingDao.findByTrainerEmail(trainerEmail)
				.filter(trainings -> !trainings.isEmpty())
				.map(this::getTrainingDtos)
				.map(Either::<ErrorMsg, List<TrainingDto>>right)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_FOUND.toString())));
	}

	private List<TrainingDto> getTrainingDtos(List<TrainingDocument> training) {
		return training.stream()
				.map(trainingConverter::trainingDocumentToTrainingDto)
				.collect(Collectors.toList());
	}

	public Either<ErrorMsg, TrainingDto> createTraining(TrainingDto trainingDto) {
		return Optional.ofNullable(trainingDao.save(trainingConverter.trainingDtoToTrainingDocument(trainingDto)))
				.map(trainingConverter::trainingDocumentToTrainingDto)
				.map(Either::<ErrorMsg, TrainingDto>right)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_SAVED.toString())));
	}

	public Either<ErrorMsg, TrainingDto> deleteTraining(String trainingName,
														String trainerEmail) {

		return trainingDao.findByTrainingNameAndTrainerEmail(trainingName, trainerEmail)
				.map(this::prepareSuccessfulTrainingDeleting)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_DELETED.toString())));
	}

	public Either<ErrorMsg, TrainingDto> updateTraining(TrainingDto trainingDto,
														String trainerEmail) {
		return trainingDao.findByTrainerEmail(trainerEmail)
				.filter(trainings -> !trainings.isEmpty())
				.map(trainings -> checkIfTrainingExistByName(trainingDto.getTrainingName(), trainings))
				.map(training -> prepareTrainingUpdate(trainingDto, training.get()))
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_UPDATED.toString())));
	}

	public Either<ErrorMsg, TrainingDto> selectTraining(String trainerEmail,
														String trainingName) {
		return trainingDao.findByTrainerEmail(trainerEmail)
				.filter(trainings -> !trainings.isEmpty())
				.map(trainings -> checkIfTrainingExistByName(trainingName, trainings))
				.map(training -> trainingConverter.trainingDocumentToTrainingDto(training.get()))
				.map(this::checkEitherResponseForTraining)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_SELECTED.toString())));
	}

	public Either<ErrorMsg, List<String>> clientsWhoBoughtTraining(String trainingName) {
		return trainingDao.findByTrainingName(trainingName)
				.map(TrainingDocument::getClients)
				.map(this::checkEitherResponseForClients)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_FOUND.toString())));
	}

	public Either<ErrorMsg, List<String>> getAllTrainingsBoughtByClient(String clientName) {
		return Optional.of(prepareTrainingDocuments())
				.map(trainingDocuments -> getTrainingDocuments(clientName, trainingDocuments))
				.filter(trainingNames -> !trainingNames.isEmpty())
				.map(Either::<ErrorMsg, List<String>>right)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAININGS_BOUGHT.toString())));
	}

	private List<String> getTrainingDocuments(String clientName, ArrayList<TrainingDocument> trainingDocuments) {
		return trainingDocuments.stream()
				.map(trainingDocument -> checkIfClientNameEqualsWithAnyClientFromSingleTraining(clientName, trainingDocument))
				.collect(Collectors.toList());
	}

	private Either<ErrorMsg, TrainingDto> prepareSuccessfulTrainingDeleting(TrainingDocument trainingDocument) {
		trainingDao.delete(trainingDocument);

		return checkEitherResponseForTraining(
				trainingConverter.trainingDocumentToTrainingDto(trainingDocument)
		);
	}

	private ArrayList<TrainingDocument> prepareTrainingDocuments() {
		Iterable<TrainingDocument> trainingDocuments = trainingDao.findAll();
		return Lists.newArrayList(trainingDocuments);
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
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_BOUGHT.toString())));
	}

	private Either<ErrorMsg, TrainingDto> prepareTrainingUpdate(TrainingDto trainingDto,
																TrainingDocument trainingDocument) {
		trainingDao.save(trainingDocument);

		return checkEitherResponseForTraining(
				trainingConverter.trainingDocumentToTrainingDto(
						setNewValuesForTrainingDocument(trainingDto, trainingDocument)
				)
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

	private Either<ErrorMsg, TrainingDto> checkEitherResponseForTraining(TrainingDto training) {
		return Optional.ofNullable(training)
				.map(Either::<ErrorMsg, TrainingDto>right)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}
}
