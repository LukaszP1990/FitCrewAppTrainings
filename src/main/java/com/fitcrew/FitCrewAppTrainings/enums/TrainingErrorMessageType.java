package com.fitcrew.FitCrewAppTrainings.enums;

public enum TrainingErrorMessageType {

	NOT_SUCCESSFULLY_MAPPING("Training object not mapped successfully"),
	NO_TRAINING_FOUND("No training found"),
	NO_TRAINING_UPDATED("No training updated"),
	NO_TRAINING_DELETED("No training deleted"),
	NO_TRAINING_SAVED("No training saved"),
	NO_TRAINING_SELECTED("No training selected"),
	NO_TRAININGS_BOUGHT("No trainings has client bought"),
	NO_TRAINING_BOUGHT("No client has bought this training");


	TrainingErrorMessageType(String string) {
	}
}
