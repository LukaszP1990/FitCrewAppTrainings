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
    private static final TrainingEntity mockedTrainingEntity = TrainingResourceMockUtil.createTrainingEntity();
    private static final TrainingDto mockedCreatedTrainingDto = TrainingResourceMockUtil.createTrainingDto();
    private static final TrainingDto mockedUpdatedTrainingDto = TrainingResourceMockUtil.updateTrainingDto();
    private final static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private final static String TRAINING_NAME = "Mocked training name";

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<TrainingEntity> trainingEntityArgumentCaptor;

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

        when(trainingDao.save(any())).thenReturn(mockedTrainingEntity);

        Either<ErrorMsg, TrainingDto> createdTraining =
                trainingService.createTraining(mockedCreatedTrainingDto);

        verifySaveTraining();

        checkAssertionsForTraining(createdTraining);
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

        when(trainingDao.findByTrainingNameAndTrainerEmail(anyString(), anyString()))
                .thenReturn(mockedTrainingEntity);

        Either<ErrorMsg, TrainingDto> deletedTraining =
                trainingService.deleteTraining(TRAINING_NAME, TRAINER_EMAIL);

        verifyDeleteTraining();

        checkAssertionsForTraining(deletedTraining);
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

        when(trainingDao.findByTrainerEmail(anyString()))
                .thenReturn(mockedTrainingEntities);

        Either<ErrorMsg, TrainingDto> updatedTraining =
                trainingService.updateTraining(mockedCreatedTrainingDto, TRAINER_EMAIL);

        verifyFindEntityByEmail();

        checkAssertionsForTraining(updatedTraining);
    }

    @Test
    void shouldNotUpdateTraining() {

        Either<ErrorMsg, TrainingDto> noUpdatedTraining =
                trainingService.updateTraining(mockedUpdatedTrainingDto, TRAINER_EMAIL);

        assertNotNull(noUpdatedTraining);

        checkEitherLeft(
                true,
                "No training updated",
                noUpdatedTraining.getLeft());
    }

    @Test
    void shouldSelectTraining() {

        when(trainingDao.findByTrainerEmail(anyString()))
                .thenReturn(mockedTrainingEntities);

        Either<ErrorMsg, TrainingDto> selectedTraining =
                trainingService.selectTraining(TRAINER_EMAIL, TRAINING_NAME);

        verifyFindEntityByEmail();

        checkAssertionsForTraining(selectedTraining);
    }

    @Test
    void shouldNotSelectTraining() {

        Either<ErrorMsg, TrainingDto> noSelectedTraining =
                trainingService.selectTraining(TRAINER_EMAIL, TRAINING_NAME);

        assertNotNull(noSelectedTraining);

        checkEitherLeft(
                true,
                "No training selected",
                noSelectedTraining.getLeft());
    }

    @Test
    void shouldGetClientsWhoBoughtTraining() {

        when(trainingDao.findByTrainingName(anyString()))
                .thenReturn(mockedTrainingEntity);

        Either<ErrorMsg, List<String>> clients =
                trainingService.clientsWhoBoughtTraining(TRAINING_NAME);

        verifyFindEntityByTrainingName();

        assertAll(() -> {
            assertTrue(clients.isRight());
            assertEquals(2, clients.get().size());
        });
    }

    @Test
    void shouldNotGetClientsWhoBoughtTraining() {

        Either<ErrorMsg, List<String>> noTrainingFound =
                trainingService.clientsWhoBoughtTraining(TRAINING_NAME);

        assertNotNull(noTrainingFound);

        checkEitherLeft(
                true,
                "No training found",
                noTrainingFound.getLeft());
    }

    @Test
    void shouldGetAllTrainingsBoughtByClient() {

        when(trainingDao.findAll())
                .thenReturn(mockedTrainingEntities);

        Either<ErrorMsg, List<String>> trainings =
                trainingService.getAllTrainingsBoughtByClient("mockedFirstClient");

        verify(trainingDao, times(1))
                .findAll();

        assertAll(() -> {
            assertTrue(trainings.isRight());
            assertEquals(3, trainings.get().size());
        });
    }

    @Test
    void shouldNotGetAllTrainingsBoughtByClient() {

        Either<ErrorMsg, List<String>> noTrainingFound =
                trainingService.getAllTrainingsBoughtByClient(TRAINING_NAME);

        assertNotNull(noTrainingFound);

        checkEitherLeft(
                true,
                "No trainings has client bought",
                noTrainingFound.getLeft());
    }

    private void verifyFindEntityByEmail() {
        verify(trainingDao, times(1))
                .findByTrainerEmail(TRAINER_EMAIL);
        verify(trainingDao)
                .findByTrainerEmail(stringArgumentCaptor.capture());
    }

    private void verifyFindEntityByTrainingName() {
        verify(trainingDao, times(1))
                .findByTrainingName(TRAINING_NAME);
        verify(trainingDao)
                .findByTrainingName(stringArgumentCaptor.capture());
    }

    private void verifySaveTraining() {
        verify(trainingDao, times(1))
                .save(any());
        verify(trainingDao)
                .save(trainingEntityArgumentCaptor.capture());
    }

    private void verifyDeleteTraining() {
        verify(trainingDao, times(1))
                .delete(mockedTrainingEntity);
        verify(trainingDao)
                .delete(trainingEntityArgumentCaptor.capture());
    }

    private void checkEitherLeft(boolean value,
                                 String message,
                                 ErrorMsg errorMsg) {
        assertAll(() -> {
            assertTrue(value);
            assertEquals(message, errorMsg.getMsg());
        });
    }

    private void checkAssertionsForTraining(Either<ErrorMsg, TrainingDto> selectedTraining) {
        assertAll(() -> {
            assertTrue(selectedTraining.isRight());
            assertEquals("Mocked training name", selectedTraining.get().getTrainingName());
            assertEquals("Mocked description", selectedTraining.get().getDescription());
            assertEquals("Mocked training", selectedTraining.get().getTraining());
            assertEquals(TRAINER_EMAIL, selectedTraining.get().getTrainerEmail());
            assertEquals(2, selectedTraining.get().getClients().size());
        });
    }
}