package com.fitcrew.FitCrewAppTrainings.util;

import com.fitcrew.FitCrewAppTrainings.domains.TrainingDocument;
import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainingResourceMockUtil {

    private final static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private final static String TRAINING_NAME = "Mocked training name";
    private final static String TRAINING_DESCRIPTION = "Mocked description";
    private final static String TRAINING = "Mocked training";

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
                .trainerEmail(TRAINER_EMAIL)
                .description(TRAINING_DESCRIPTION)
                .trainingName(TRAINING_NAME)
                .training(TRAINING)
                .build();
    }

	public static TrainingDto createNotValidTrainingDto() {
		return TrainingDto.builder()
				.clients(getListOfClients())
				.trainerEmail(TRAINER_EMAIL)
				.build();
	}

    private static TrainingDocument prepareTrainingDocumentData() {
        return TrainingDocument.builder()
                .clients(getListOfClients())
                .trainerEmail(TRAINER_EMAIL)
                .description(TRAINING_DESCRIPTION)
                .trainingName(TRAINING_NAME)
                .training(TRAINING)
                .build();
    }

    private static List<String> getListOfClients() {
        return Arrays.asList(
                "mockedFirstClient",
                "mockedSecondClient"
        );
    }


}
