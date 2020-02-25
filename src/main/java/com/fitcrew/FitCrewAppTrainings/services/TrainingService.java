package com.fitcrew.FitCrewAppTrainings.services;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.dao.TrainingDao;
import com.fitcrew.FitCrewAppTrainings.domains.TrainingEntity;
import com.fitcrew.FitCrewAppTrainings.enums.TrainingErrorMessageType;
import com.fitcrew.FitCrewAppTrainings.resolver.ErrorMsg;
import com.google.common.collect.Lists;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainingService {

	private final TrainingDao trainingDao;
	private static long trainingId = 1;

	public TrainingService(TrainingDao trainingDao) {
		this.trainingDao = trainingDao;
	}

	public Either<ErrorMsg, List<TrainingDto>> getTrainerTrainings(String trainerEmail) {
		ModelMapper modelMapper = prepareModelMapper();

		return trainingDao.findByTrainerEmail(trainerEmail)
				.filter(trainings -> !trainings.isEmpty())
				.map(trainings -> getTrainingDtos(modelMapper, trainings))
				.map(Either::<ErrorMsg, List<TrainingDto>>right)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_FOUND.toString())));
	}

	private List<TrainingDto> getTrainingDtos(ModelMapper modelMapper, List<TrainingEntity> training) {
		return training.stream()
				.map(trainingEntity -> modelMapper.map(trainingEntity, TrainingDto.class))
				.collect(Collectors.toList());
	}

	public Either<ErrorMsg, TrainingDto> createTraining(TrainingDto trainingDto) {
		ModelMapper modelMapper = prepareModelMapper();

		return Optional.ofNullable(trainingDao.save(modelMapper.map(trainingDto, TrainingEntity.class)))
				.map(training -> modelMapper.map(training, TrainingDto.class))
				.map(Either::<ErrorMsg, TrainingDto>right)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_SAVED.toString())));
	}

	public Either<ErrorMsg, TrainingDto> deleteTraining(String trainingName,
														String trainerEmail) {
		ModelMapper modelMapper = prepareModelMapper();

		return trainingDao.findByTrainingNameAndTrainerEmail(trainingName, trainerEmail)
				.map(training -> prepareSuccessfulTrainingDeleting(modelMapper, training))
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
		ModelMapper modelMapper = prepareModelMapper();

		return trainingDao.findByTrainerEmail(trainerEmail)
				.filter(trainings -> !trainings.isEmpty())
				.map(trainings -> checkIfTrainingExistByName(trainingName, trainings))
				.map(training -> modelMapper.map(training.get(), TrainingDto.class))
				.map(this::checkEitherResponseForTraining)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_SELECTED.toString())));
	}

	public Either<ErrorMsg, List<String>> clientsWhoBoughtTraining(String trainingName) {
		return trainingDao.findByTrainingName(trainingName)
				.map(TrainingEntity::getClients)
				.map(this::checkEitherResponseForClients)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_FOUND.toString())));
	}

	public Either<ErrorMsg, List<String>> getAllTrainingsBoughtByClient(String clientName) {
		return Optional.of(prepareTrainingEntityList())
				.map(trainingEntities -> getTrainingEntities(clientName, trainingEntities))
				.filter(trainingNames -> !trainingNames.isEmpty())
				.map(Either::<ErrorMsg, List<String>>right)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAININGS_BOUGHT.toString())));
	}

	private List<String> getTrainingEntities(String clientName, ArrayList<TrainingEntity> trainingEntities) {
		return trainingEntities.stream()
				.map(trainingEntity -> checkIfClientNameEqualsWithAnyClientFromSingleTraining(clientName, trainingEntity))
				.collect(Collectors.toList());
	}

	private Either<ErrorMsg, TrainingDto> prepareSuccessfulTrainingDeleting(ModelMapper modelMapper,
																			TrainingEntity trainingToDelete) {
		trainingDao.delete(trainingToDelete);
		TrainingDto trainingToReturn = modelMapper.map(trainingToDelete, TrainingDto.class);

		return checkEitherResponseForTraining(trainingToReturn);
	}

	private ArrayList<TrainingEntity> prepareTrainingEntityList() {
		Iterable<TrainingEntity> trainingEntities = trainingDao.findAll();
		return Lists.newArrayList(trainingEntities);
	}

	private String checkIfClientNameEqualsWithAnyClientFromSingleTraining(String clientName,
																		  TrainingEntity trainingEntity) {
		return trainingEntity.getClients().stream()
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
																TrainingEntity foundTrainerEntityByTrainingName) {
		ModelMapper modelMapper = prepareModelMapper();
		trainingDao.save(foundTrainerEntityByTrainingName);
		setNewValuesForTraining(trainingDto, foundTrainerEntityByTrainingName);
		TrainingDto trainingToReturn = modelMapper.map(foundTrainerEntityByTrainingName, TrainingDto.class);

		return checkEitherResponseForTraining(trainingToReturn);
	}

	private void setNewValuesForTraining(TrainingDto trainingDto,
										 TrainingEntity foundTrainerEntityByTrainingName) {
		foundTrainerEntityByTrainingName.setTrainingName(trainingDto.getTrainingName());
		foundTrainerEntityByTrainingName.setDescription(trainingDto.getDescription());
		foundTrainerEntityByTrainingName.setTraining(trainingDto.getTraining());
		foundTrainerEntityByTrainingName.setTrainerEmail(trainingDto.getTrainerEmail());
	}

	private Either<ErrorMsg, TrainingEntity> checkIfTrainingExistByName(String trainingName,
																		List<TrainingEntity> trainerTrainings) {
		return trainerTrainings.stream()
				.filter(trainingEntity -> trainingEntity.getTrainingName().equals(trainingName))
				.findFirst()
				.map(Either::<ErrorMsg, TrainingEntity>right)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NO_TRAINING_FOUND.toString())));
	}

	private Either<ErrorMsg, TrainingDto> checkEitherResponseForTraining(TrainingDto training) {
		return Optional.ofNullable(training)
				.map(Either::<ErrorMsg, TrainingDto>right)
				.orElse(Either.left(new ErrorMsg(TrainingErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}

	private PropertyMap<TrainingDto, TrainingEntity> skipModifiedFieldsMap = new PropertyMap<TrainingDto, TrainingEntity>() {
		protected void configure() {
			skip().setId(trainingId);
			trainingId++;
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
