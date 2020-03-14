package com.fitcrew.FitCrewAppTrainings.converter;

import org.mapstruct.Mapper;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;

@Mapper
public interface TrainingDocumentTrainingDtoConverter {

	TrainingDto trainingDocumentToTrainingDto(TrainingDocument trainingDocument);

	TrainingDocument trainingDtoToTrainingDocument(TrainingDto trainingDto);
}
