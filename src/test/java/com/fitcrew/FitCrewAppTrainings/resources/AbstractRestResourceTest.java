package com.fitcrew.FitCrewAppTrainings.resources;

import com.fitcrew.FitCrewAppTrainings.FitCrewAppTrainingsApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;

import javax.inject.Inject;

@TestPropertySource(locations = {"classpath:application-test.properties"})
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = FitCrewAppTrainingsApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractRestResourceTest {

	@Inject
	TestRestTemplate restTemplate;
}
