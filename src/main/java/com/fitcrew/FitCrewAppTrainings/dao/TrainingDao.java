package com.fitcrew.FitCrewAppTrainings.dao;

import com.fitcrew.FitCrewAppTrainings.domains.TrainingEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingDao extends CrudRepository<TrainingEntity, Long> {
	Optional<List<TrainingEntity>> findByTrainerEmail(String trainerEmail);

	Optional<TrainingEntity> findByTrainingName(String trainingName);

	Optional<TrainingEntity> findByTrainingNameAndTrainerEmail(String trainingName, String trainerEmail);
}
