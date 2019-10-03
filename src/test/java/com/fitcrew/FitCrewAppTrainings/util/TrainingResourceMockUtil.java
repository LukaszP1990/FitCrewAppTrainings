package com.fitcrew.FitCrewAppTrainings.util;

import com.fitcrew.FitCrewAppTrainings.domains.TrainingEntity;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainingResourceMockUtil {

    public static List<String> getListOfClients() {
        return Arrays.asList(
                "mockedFirstClient",
                "mockedSecondClient"
        );
    }

    public static List<TrainingEntity> createTrainingEntities() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(value -> TrainingEntity.builder()
                        .clients(getListOfClients())
                        .trainerEmail("mockedTrainer@gmail.com")
                        .description("Mocked descriprion")
                        .trainingName("Mocked training name")
                        .training("Mocked training")
                        .build())
                .collect(Collectors.toList());
    }

    public static TrainingDto createTrainingDto() {
        return TrainingDto.builder()
                .clients(getListOfClients())
                .trainerEmail("mockedTrainer@gmail.com")
                .description("Mocked descriprion")
                .name("Mocked training name")
                .training("Mocked training")
                .build();
    }

	public static TrainingDto updateTrainingDto() {
		return TrainingDto.builder()
				.clients(getListOfClients())
				.trainerEmail("UpdatedMockedTrainer@gmail.com")
				.description("Updated Mocked descriprion")
				.name("Updated Mocked training name")
				.training("Updated Mocked training")
				.build();
	}

}
