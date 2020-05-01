package com.fitcrew.FitCrewAppTrainings.converter;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.util.TrainingResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDtoTrainingModelConverterTest {

    private static TrainingDto trainingDto = TrainingResourceMockUtil.createTrainingDto();
    private static TrainingDocument trainingDocument = TrainingResourceMockUtil.createTrainingDocument();
    private TrainingDtoTrainingModelConverter trainingConverter = Mappers.getMapper(TrainingDtoTrainingModelConverter.class);
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String DESCRIPTION = "Mocked description";
    private static String NAME = "Mocked training name";
    private static String TRAINING = "Mocked training";

    @Test
    void shouldConvertTrainerDtoToTrainerModel() {
        TrainingModel trainingModel = trainingConverter.trainingDtoToTrainingModel(trainingDto);
        assertNotNull(trainingModel);
        checkAssertions(trainingModel);
    }

    @Test
    void shouldConvertTrainerDocumentToTrainerModel() {
        TrainingModel trainingModel = trainingConverter.trainingDocumentToTrainingModel(trainingDocument);
        assertNotNull(trainingModel);
        checkAssertions(trainingModel);
    }

    private void checkAssertions(TrainingModel trainingModel) {
        assertAll(() -> {
            assertEquals(NAME, trainingModel.getTrainingName());
            assertEquals(DESCRIPTION, trainingModel.getDescription());
            assertEquals(TRAINER_EMAIL, trainingModel.getTrainerEmail());
            assertEquals(TRAINING, trainingModel.getTraining());
        });
    }
}
