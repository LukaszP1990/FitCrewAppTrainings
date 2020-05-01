package com.fitcrew.FitCrewAppTrainings.validation;

import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.dto.validation.NotAllRequiredValueSetInTrainingDto;
import com.fitcrew.FitCrewAppTrainings.util.TrainingResourceMockUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotAllRequiredValueSetInTrainingDtoTest {

    private static TrainingDto validTrainingDto = TrainingResourceMockUtil.createTrainingDto();
    private static TrainingDto notValidTrainingDto = TrainingResourceMockUtil.createNotValidTrainingDto();

    @Mock
    private NotAllRequiredValueSetInTrainingDto.NotAllRequiredValueSetInTrainingDtoValidator trainingValidator;

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        when(trainingValidator.isValid(any(), any())).thenCallRealMethod();
    }

    @Test
    void shouldSucceedWhenRequiredValuesHaveBeenSet() {

        assertTrue(trainingValidator
                .isValid(validTrainingDto, constraintValidatorContext));
    }

    @Test
    void shouldFailWhenWhenRequiredValuesHaveNotBeenSet() {
        assertFalse(trainingValidator
                .isValid(notValidTrainingDto, constraintValidatorContext));
    }
}