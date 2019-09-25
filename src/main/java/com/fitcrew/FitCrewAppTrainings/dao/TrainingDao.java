package com.fitcrew.FitCrewAppTrainings.dao;

import com.fitcrew.FitCrewAppTrainings.domains.TrainingEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingDao extends CrudRepository<TrainingEntity, Long> {
	List<TrainingEntity> findByTrainerEmail(String trainerEmail);

	TrainingEntity findByTrainingNameAndTrainerEmail(String trainingName, String trainerEmail);
}
