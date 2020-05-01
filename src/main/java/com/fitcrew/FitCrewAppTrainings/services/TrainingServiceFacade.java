package com.fitcrew.FitCrewAppTrainings.services;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.resolver.ErrorMsg;
import io.vavr.control.Either;

import java.util.List;

public interface TrainingServiceFacade {
    Either<ErrorMsg, List<TrainingModel>> getTrainerTrainings(String trainerEmail);

    Either<ErrorMsg, TrainingModel> createTraining(TrainingDto trainingDto);

    Either<ErrorMsg, TrainingModel> deleteTraining(String trainingName, String trainerEmail);

    Either<ErrorMsg, TrainingModel> updateTraining(TrainingDto trainingDto, String trainerEmail);

    Either<ErrorMsg, TrainingModel> selectTraining(String trainerEmail, String trainingName);

    Either<ErrorMsg, List<String>> clientsWhoBoughtTraining(String trainingName);

    Either<ErrorMsg, List<String>> getAllTrainingsBoughtByClient(String clientName);
}
