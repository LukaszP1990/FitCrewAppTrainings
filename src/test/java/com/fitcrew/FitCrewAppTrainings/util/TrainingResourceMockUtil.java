package com.fitcrew.FitCrewAppTrainings.util;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainingResourceMockUtil {

	public static List<TrainingDocument> createTrainingDocuments() {
		return IntStream.rangeClosed(1, 3)
				.mapToObj(value -> prepareTrainingDocumentData())
				.collect(Collectors.toList());
	}

	public static TrainingDocument createTrainingDocument() {
		return prepareTrainingDocumentData();
	}

	public static TrainingDto createTrainingDto() {
		return TrainingDto.builder()
				.clients(getListOfClients())
				.trainerEmail("mockedTrainer@gmail.com")
				.description("Mocked description")
				.trainingName("Mocked training name")
				.training("Mocked training")
				.build();
	}

	public static TrainingDto updateTrainingDto() {
		return TrainingDto.builder()
				.clients(getListOfClients())
				.trainerEmail("UpdatedMockedTrainer@gmail.com")
				.description("Updated Mocked description")
				.trainingName("Updated Mocked training name")
				.training("Updated Mocked training")
				.build();
	}

	private static TrainingDocument prepareTrainingDocumentData() {
		return TrainingDocument.builder()
				.clients(getListOfClients())
				.trainerEmail("mockedTrainer@gmail.com")
				.description("Mocked description")
				.trainingName("Mocked training name")
				.training("Mocked training")
				.build();
	}

	private static List<String> getListOfClients() {
		return Arrays.asList(
				"mockedFirstClient",
				"mockedSecondClient"
		);
	}


}
