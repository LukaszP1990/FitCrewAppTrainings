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
	private String SUCCESSFULLY_MAPPING = "Training object mapped successfully and send to trainer web service {}";
	private String NOT_SUCCESSFULLY_MAPPING = "Training not mapped successfully";

	public TrainingService(TrainingDao trainingDao) {
		this.trainingDao = trainingDao;
	}


	public Either<ErrorMsg, List<TrainingDto>> getTrainerTrainings(String trainerEmail) {
		List<TrainingEntity> trainingsCreatedByTrainer = trainingDao.findByTrainerEmail(trainerEmail);

		if (!trainingsCreatedByTrainer.isEmpty()) {
			log.debug("List of trainings trainer created{}", trainingsCreatedByTrainer);

			ModelMapper modelMapper = prepareModelMapperForNewTraining();

			return Either.right(
					trainingsCreatedByTrainer.stream()
							.map(trainingEntity -> modelMapper.map(trainingEntity, TrainingDto.class))
							.collect(Collectors.toList())
			);

		} else {
			log.debug("No training found");
			return Either.left(new ErrorMsg("No training found"));
		}
	}

	public Either<ErrorMsg, TrainingDto> createTraining(TrainingDto trainingDto) {

		ModelMapper modelMapper = prepareModelMapperForNewTraining();
		TrainingEntity savedTraining = trainingDao.save(
				modelMapper.map(trainingDto, TrainingEntity.class)
		);

		if (savedTraining != null) {
			log.debug("Training saved for trainer with email{}", savedTraining.getTrainerEmail());
			TrainingDto trainingToReturn = modelMapper.map(savedTraining, TrainingDto.class);

			return checkEitherResponse(trainingToReturn,
					SUCCESSFULLY_MAPPING,
					NOT_SUCCESSFULLY_MAPPING);

		} else {
			log.debug("No training saved");
			return Either.left(new ErrorMsg("No training saved"));
		}
	}

	public Either<ErrorMsg, TrainingDto> deleteTraining(String trainingName, String trainerEmail) {

		ModelMapper modelMapper = prepareModelMapperForExistingTraining();
		TrainingEntity trainingToDelete = trainingDao.findByTrainingNameAndTrainerEmail(trainingName, trainerEmail);

		if (trainingToDelete != null) {
			log.debug("Training to delete {}", trainingToDelete);
			trainingDao.delete(trainingToDelete);
			TrainingDto trainingToReturn = modelMapper.map(trainingToDelete, TrainingDto.class);

			return checkEitherResponse(trainingToReturn,
					SUCCESSFULLY_MAPPING,
					NOT_SUCCESSFULLY_MAPPING);
		} else {
			log.debug("No training deleted");
			return Either.left(new ErrorMsg("No training deleted"));
		}
	}

	public Either<ErrorMsg, TrainingDto> updateTraining(TrainingDto trainingDto, String trainerEmail) {

		List<TrainingEntity> trainerTrainings = trainingDao.findByTrainerEmail(trainerEmail);
		TrainingEntity foundTrainerEntityByTrainingName = checkIfTrainingNameExist(trainingDto, trainerTrainings);

		if (foundTrainerEntityByTrainingName != null) {
			return prepareTrainingUpdate(trainingDto, foundTrainerEntityByTrainingName);
		} else {
			log.debug("No training updated");
			return Either.left(new ErrorMsg("No training updated"));
		}
	}

	private Either<ErrorMsg, TrainingDto> prepareTrainingUpdate(TrainingDto trainingDto, TrainingEntity foundTrainerEntityByTrainingName) {
		ModelMapper modelMapper = prepareModelMapperForExistingTraining();

		setNewValuesForTraining(trainingDto, foundTrainerEntityByTrainingName);

		log.debug("Training updated {}", foundTrainerEntityByTrainingName);
		TrainingDto trainingToReturn = modelMapper.map(foundTrainerEntityByTrainingName, TrainingDto.class);

		return checkEitherResponse(trainingToReturn,
				SUCCESSFULLY_MAPPING,
				NOT_SUCCESSFULLY_MAPPING);
	}

	private void setNewValuesForTraining(TrainingDto trainingDto, TrainingEntity foundTrainerEntityByTrainingName) {
		foundTrainerEntityByTrainingName.setTrainingName(trainingDto.getName());
		foundTrainerEntityByTrainingName.setDescription(trainingDto.getDescription());
		foundTrainerEntityByTrainingName.setTraining(trainingDto.getTraining());
		foundTrainerEntityByTrainingName.setTrainerEmail(trainingDto.getTrainerEmail());
	}

	private TrainingEntity checkIfTrainingNameExist(TrainingDto trainingDto, List<TrainingEntity> trainerTrainings) {
		return trainerTrainings.stream()
				.filter(trainingEntity -> trainingEntity.getTrainingName().equals(trainingDto.getName()))
				.findFirst()
				.orElseGet(null);
	}

	private Either<ErrorMsg, TrainingDto> checkEitherResponse(TrainingDto training,
															  String eitherRightMessage,
															  String eitherLeftMessage) {
		if (training != null) {
			log.debug(eitherRightMessage, training);
			return Either.right(training);
		} else {
			log.debug(eitherLeftMessage);
			return Either.left(new ErrorMsg(eitherLeftMessage));
		}
	}

	private PropertyMap<TrainingDto, TrainingEntity> skipModifiedFieldsMap = new PropertyMap<TrainingDto, TrainingEntity>() {
		protected void configure() {
			skip().setId(clientId);
			clientId++;
		}
	};

	private ModelMapper prepareModelMapperForNewTraining() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(skipModifiedFieldsMap);
		return modelMapper;
	}

	private ModelMapper prepareModelMapperForExistingTraining() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
}
