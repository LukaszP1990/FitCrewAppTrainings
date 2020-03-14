package com.fitcrew.FitCrewAppTrainings.dao;

import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingDao extends MongoRepository<TrainingDocument, String> {
	Optional<List<TrainingDocument>> findByTrainerEmail(String trainerEmail);

	Optional<TrainingDocument> findByTrainingName(String trainingName);

	Optional<TrainingDocument> findByTrainingNameAndTrainerEmail(String trainingName, String trainerEmail);
}
