package com.fitcrew.FitCrewAppTrainings.services;

import com.fitcrew.FitCrewAppTrainings.dao.TrainingDao;
import com.fitcrew.FitCrewAppTrainings.domains.TrainingEntity;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainings.util.TrainingResourceMockUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainingServiceTest {

    private static final List<TrainingEntity> mockedTrainingEntities = TrainingResourceMockUtil.createTrainingEntities();
    private static final TrainingDto mockedCreatedTrainingDto = TrainingResourceMockUtil.createTrainingDto();
    private static final TrainingDto mockedUpdatedTrainingDto = TrainingResourceMockUtil.updateTrainingDto();
    private final static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private final static String TRAINING_NAME = "Mocked training name";

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void shouldGetTrainerTrainings() {
        when(trainingDao.findByTrainerEmail(anyString()))
                .thenReturn(mockedTrainingEntities);

        Either<ErrorMsg, List<TrainingDto>> trainerTrainings =
                trainingService.getTrainerTrainings(TRAINER_EMAIL);

        verifyFindEntityByEmail();

        assertNotNull(trainerTrainings);
        assertAll(() -> {
            assertTrue(trainerTrainings.isRight());
            assertEquals(3, trainerTrainings.get().size());
        });
    }

    @Test
    void shouldNotGetTrainerTrainings() {

        Either<ErrorMsg, List<TrainingDto>> noTrainerTrainings =
                trainingService.getTrainerTrainings(TRAINER_EMAIL);

        assertNotNull(noTrainerTrainings);

        checkEitherLeft(
                true,
                "No training found",
                noTrainerTrainings.getLeft());
    }

    @Test
    void shouldCreateTraining() {
    }

    @Test
    void shouldNotCreateTraining() {

        Either<ErrorMsg, TrainingDto> noTrainingCreated =
                trainingService.createTraining(mockedCreatedTrainingDto);

        assertNotNull(noTrainingCreated);

        checkEitherLeft(
                true,
                "No training saved",
                noTrainingCreated.getLeft());
    }

    @Test
    void shouldDeleteTraining() {
    }

    @Test
    void shouldNotDeleteTraining() {

        Either<ErrorMsg, TrainingDto> noDeletedTraining =
                trainingService.deleteTraining(TRAINING_NAME, TRAINER_EMAIL);

        assertNotNull(noDeletedTraining);

        checkEitherLeft(
                true,
                "No training deleted",
                noDeletedTraining.getLeft());
    }

    @Test
    void shouldUpdateTraining() {
    }

    @Test
    void shouldNotUpdateTraining() {

        Either<ErrorMsg, TrainingDto> noUpdatedTraining =
                trainingService.updateTraining(mockedUpdatedTrainingDto, TRAINER_EMAIL);

        assertNotNull(noUpdatedTraining);

        checkEitherLeft(
                true,
                "No training deleted",
                noUpdatedTraining.getLeft());
    }

    @Test
    void shouldSelectTraining() {

    }

    @Test
    void shouldNotSelectTraining() {

        Either<ErrorMsg, TrainingDto> noSelectedTraining =
                trainingService.selectTraining(TRAINER_EMAIL, TRAINING_NAME);

        assertNotNull(noSelectedTraining);

        checkEitherLeft(
                true,
                "No training deleted",
                noSelectedTraining.getLeft());
    }

    @Test
    void shouldGetClientsWhoBoughtTraining() {
    }

    @Test
    void shouldNotGetClientsWhoBoughtTraining() {
    }

    @Test
    void shouldGetAllTrainingsBoughtByClient() {
    }

    @Test
    void shouldNotGetAllTrainingsBoughtByClient() {
    }

    private void verifyFindEntityByEmail() {
        verify(trainingDao, times(1))
                .findByTrainerEmail(TRAINER_EMAIL);
        verify(trainingDao)
                .findByTrainerEmail(stringArgumentCaptor.capture());
    }

    private void checkEitherLeft(boolean value,
                                 String message,
                                 ErrorMsg errorMsg) {
        assertAll(() -> {
            assertTrue(value);
            assertEquals(message, errorMsg.getMsg());
        });
    }
}