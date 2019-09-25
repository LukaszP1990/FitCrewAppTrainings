package com.fitcrew.FitCrewAppTrainings.resources;

import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainings.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainings.services.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "Trainings resources")
@Slf4j
@RestController
@RequestMapping("/training")
public class TrainingResource {

    private final TrainingService trainingService;

    public TrainingResource(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @ApiOperation(value = "Return all clients who bought training from trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful all clients who bought training from trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/getClientsWhoBoughtTraining/{trainerName}/trainerName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,})
    private ResponseEntity getClientsWhoBoughtTrainingFromTrainer(@PathVariable String trainerName) {
        Either<ErrorMsg, List<TrainingDto>> clientWhoBoughtTrainingFromTrainer =
                trainingService.getClientsWhoBoughtTrainingFromTrainer(trainerName);
        return ResponseResolver.resolve(clientWhoBoughtTrainingFromTrainer);
    }

}
