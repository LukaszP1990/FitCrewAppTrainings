package com.fitcrew.FitCrewAppTrainings.resources;

import com.fitcrew.FitCrewAppTrainings.FitCrewAppTrainingsApplication;
import com.fitcrew.FitCrewAppTrainings.dao.TrainingDao;
import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.util.HttpEntityUtil;
import com.fitcrew.FitCrewAppTrainings.util.TrainingResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = {"classpath:application-test.properties"})
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = FitCrewAppTrainingsApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrainingResourceTest extends AbstractRestResourceTest{

	private static final List<TrainingDocument> mockedTrainingDocuments = TrainingResourceMockUtil.createTrainingDocuments();
	private static final TrainingDocument mockedTrainingDocument = TrainingResourceMockUtil.createTrainingDocument();
	private final static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
	private final static String TRAINING_NAME = "Mocked training name";
	private final static String TRAINING_DESCRIPTION = "Mocked description";
	private final static String TRAINING = "Mocked training";
	private final static String CLIENT_NAME =  "mockedFirstClient";

	@MockBean
	private TrainingDao trainingDao;

	@Test
	void shouldCreateTraining() {

		when(trainingDao.save(any()))
				.thenReturn(mockedTrainingDocument);

		ResponseEntity<TrainingDto> responseEntity = restTemplate.postForEntity(
				"/training/createTraining",
				HttpEntityUtil.getHttpEntityWithJwtAndBody(mockedTrainingDocument),
				TrainingDto.class
		);

		TrainingDto trainingDto = responseEntity.getBody();
		assertNotNull(trainingDto);
		checkAssertionsForTraining(trainingDto);
	}

	@Test
	void shouldDeleteTraining() {

		when(trainingDao.findByTrainingNameAndTrainerEmail(anyString(), anyString()))
				.thenReturn(Optional.of(mockedTrainingDocument));
		when(trainingDao.save(any()))
				.thenReturn(mockedTrainingDocument);

		ResponseEntity<TrainingDto> responseEntity = restTemplate.exchange(
				"/training/deleteTraining/" + TRAINER_EMAIL + "/trainerEmail/" + TRAINING_NAME + "/trainingName",
				HttpMethod.DELETE,
				HttpEntityUtil.getHttpEntityWithJwtAndBody(mockedTrainingDocument),
				TrainingDto.class
		);

		TrainingDto trainingDto = responseEntity.getBody();
		assertNotNull(trainingDto);
		checkAssertionsForTraining(trainingDto);
	}

	@Test
	void shouldGetTrainerTrainings() {

		when(trainingDao.findByTrainerEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainingDocuments));

		ResponseEntity<List<TrainingDto>> responseEntity = restTemplate.exchange(
				"/training/getTrainerTrainings/" + TRAINER_EMAIL + "/trainerEmail",
				HttpMethod.GET,
				HttpEntityUtil.getHttpEntityWithJwt(),
				new ParameterizedTypeReference<>() {
				}
		);

		List<TrainingDto> trainings = responseEntity.getBody();

		assertNotNull(trainings);
		assertEquals(3, trainings.size());
	}

	@Test
	void shouldUpdateTraining() {

		when(trainingDao.findByTrainerEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainingDocuments));
		when(trainingDao.save(any()))
				.thenReturn(mockedTrainingDocument);

		ResponseEntity<TrainingDto> responseEntity = restTemplate.exchange(
				"/training/updateTraining/" + TRAINER_EMAIL + "/trainerEmail",
				HttpMethod.PUT,
				HttpEntityUtil.getHttpEntityWithJwtAndBody(mockedTrainingDocument),
				TrainingDto.class
		);

		TrainingDto trainingDto = responseEntity.getBody();
		assertNotNull(trainingDto);
		checkAssertionsForTraining(trainingDto);
	}

	@Test
	void shouldSelectTraining() {

		when(trainingDao.findByTrainerEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainingDocuments));

		ResponseEntity<TrainingDto> responseEntity = restTemplate.exchange(
				"/training/selectTraining/" + TRAINER_EMAIL + "/trainerEmail/" + TRAINING_NAME + "/trainingName",
				HttpMethod.GET,
				HttpEntityUtil.getHttpEntityWithJwt(),
				TrainingDto.class
		);

		TrainingDto trainingDto = responseEntity.getBody();
		assertNotNull(trainingDto);
		checkAssertionsForTraining(trainingDto);
	}

	@Test
	void shouldGetClientsWhoBoughtTraining() {

		when(trainingDao.findByTrainingName(anyString()))
				.thenReturn(Optional.of(mockedTrainingDocument));

		ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
				"/training/clientsWhoBoughtTraining/" + TRAINING_NAME + "/trainingName",
				HttpMethod.GET,
				HttpEntityUtil.getHttpEntityWithJwt(),
				new ParameterizedTypeReference<>() {
				}
		);

		List<String> clients = responseEntity.getBody();
		assertNotNull(clients);
		assertEquals(2, clients.size());
	}

	@Test
	void shouldGetTrainingsBoughtByClient() {

		when(trainingDao.findAll())
				.thenReturn(mockedTrainingDocuments);

		ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
				"/training/trainingsBoughtByClient/" + CLIENT_NAME + "/clientName",
				HttpMethod.GET,
				HttpEntityUtil.getHttpEntityWithJwt(),
				new ParameterizedTypeReference<>() {
				}
		);

		List<String> clients = responseEntity.getBody();
		assertNotNull(clients);
		assertEquals(3, clients.size());
	}

	private void checkAssertionsForTraining(TrainingDto trainingDto) {
		assertAll(() -> {
			assertEquals(TRAINING_NAME, trainingDto.getTrainingName());
			assertEquals(TRAINING_DESCRIPTION, trainingDto.getDescription());
			assertEquals(TRAINING, trainingDto.getTraining());
			assertEquals(TRAINER_EMAIL, trainingDto.getTrainerEmail());
			assertEquals(2, trainingDto.getClients().size());
		});
	}


}
