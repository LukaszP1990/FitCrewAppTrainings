package com.fitcrew.FitCrewAppTrainings.services;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.resolver.ErrorMsg;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingServiceFacadeImpl implements TrainingServiceFacade {

    private final TrainingService trainingService;

    public TrainingServiceFacadeImpl(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Override
    public Either<ErrorMsg, List<TrainingModel>> getTrainerTrainings(String trainerEmail) {
        return trainingService.getTrainerTrainings(trainerEmail);
    }

    @Override
    public Either<ErrorMsg, TrainingModel> createTraining(TrainingDto trainingDto) {
        return trainingService.createTraining(trainingDto);
    }

    @Override
    public Either<ErrorMsg, TrainingModel> deleteTraining(String trainingName,
                                                          String trainerEmail) {
        return trainingService.deleteTraining(trainingName, trainerEmail);
    }

    @Override
    public Either<ErrorMsg, TrainingModel> updateTraining(TrainingDto trainingDto,
                                                          String trainerEmail) {
        return trainingService.updateTraining(trainingDto, trainerEmail);
    }

    @Override
    public Either<ErrorMsg, TrainingModel> selectTraining(String trainerEmail,
                                                          String trainingName) {
        return trainingService.selectTraining(trainerEmail, trainingName);
    }

    @Override
    public Either<ErrorMsg, List<String>> clientsWhoBoughtTraining(String trainingName) {
        return trainingService.clientsWhoBoughtTraining(trainingName);
    }

    @Override
    public Either<ErrorMsg, List<String>> getAllTrainingsBoughtByClient(String clientName) {
        return trainingService.getAllTrainingsBoughtByClient(clientName);
    }
}
