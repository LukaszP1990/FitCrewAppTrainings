package com.fitcrew.FitCrewAppTrainings.converter;

import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import org.mapstruct.Mapper;

@Mapper
public interface TrainingDocumentTrainingDtoConverter {
	TrainingDocument trainingDtoToTrainingDocument(TrainingDto trainingDto);
}
