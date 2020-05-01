package com.fitcrew.FitCrewAppTrainings.converter;

import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.util.TrainingResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDocumentTrainingDtoConverterTest {

    private static TrainingDto trainingDto = TrainingResourceMockUtil.createTrainingDto();
    private TrainingDocumentTrainingDtoConverter trainingConverter = Mappers.getMapper(TrainingDocumentTrainingDtoConverter.class);
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String DESCRIPTION = "Mocked description";
    private static String NAME = "Mocked training name";
    private static String TRAINING = "Mocked training";

    @Test
    void shouldConvertTrainerDtoToTrainerDocument() {
        TrainingDocument trainingDocument = trainingConverter.trainingDtoToTrainingDocument(trainingDto);
        assertNotNull(trainingDocument);
        assertAll(() -> {
            assertEquals(NAME, trainingDocument.getTrainingName());
            assertEquals(DESCRIPTION, trainingDocument.getDescription());
            assertEquals(TRAINER_EMAIL, trainingDocument.getTrainerEmail());
            assertEquals(TRAINING, trainingDocument.getTraining());
        });
    }
}