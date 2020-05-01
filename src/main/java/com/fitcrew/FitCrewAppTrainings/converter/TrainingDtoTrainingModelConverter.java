package com.fitcrew.FitCrewAppTrainings.converter;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import org.mapstruct.Mapper;

@Mapper
public interface TrainingDtoTrainingModelConverter {
    TrainingModel trainingDtoToTrainingModel(TrainingDto trainingDto);

    TrainingModel trainingDocumentToTrainingModel(TrainingDocument trainingDocument);
}
