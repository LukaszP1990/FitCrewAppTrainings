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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

@Api(value = "Trainings resources")
@Slf4j
@RestController
@RequestMapping("/training")
class TrainingResource {

    private final TrainingService trainingService;

    public TrainingResource(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @ApiOperation(value = "Return all trainings from trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful all trainings from trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/getTrainerTrainings/{trainerName}/trainerName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})

    private ResponseEntity getTrainerTrainings(@PathVariable String trainerEmail) {

        Either<ErrorMsg, List<TrainingDto>> trainerTrainings =
                trainingService.getTrainerTrainings(trainerEmail);
        return ResponseResolver.resolve(trainerTrainings);
    }

    @ApiOperation(value = "Training created by trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful training created by trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @PostMapping(value = "/createTraining",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity createTraining(@RequestBody @Valid TrainingDto trainingDto) {

        Either<ErrorMsg, TrainingDto> createdTrainingByTrainer =
                trainingService.createTraining(trainingDto);

        return ResponseResolver.resolve(createdTrainingByTrainer);
    }


    @ApiOperation(value = "Training deleted by trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful training deleted by trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @DeleteMapping(value = "/deleteTraining/{trainerEmail}/trainerEmail/{trainingName}/trainingName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity deleteTraining(@PathVariable String trainerEmail,
                                         @PathVariable String trainingName) {

        Either<ErrorMsg, TrainingDto> deletedTraining =
                trainingService.deleteTraining(trainingName, trainerEmail);

        return ResponseResolver.resolve(deletedTraining);
    }

    @ApiOperation(value = "Training updated by trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful training updated by trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @PutMapping(value = "/updateTraining/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity updateTraining(@RequestBody TrainingDto trainingDto,
                                         @PathVariable String trainerEmail) {

        Either<ErrorMsg, TrainingDto> deletedTraining =
                trainingService.updateTraining(trainingDto, trainerEmail);

        return ResponseResolver.resolve(deletedTraining);
    }

    @ApiOperation(value = "Training to send to the client")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful to send to the client response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/selectTraining/{trainerEmail}/trainerEmail/{trainingName}/trainingName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    private ResponseEntity selectTraining(@PathVariable String trainerEmail,
                                          @PathVariable String trainingName) {

        Either<ErrorMsg, TrainingDto> selectedTraining =
                trainingService.selectTraining(trainerEmail, trainingName);

        return ResponseResolver.resolve(selectedTraining);
    }

    @ApiOperation(value = "Clients who has bought training")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Clients who has bought training response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/clientsWhoBoughtTraining/{trainingName}/trainingName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    private ResponseEntity clientsWhoBoughtTraining(@PathVariable String trainingName) {

        Either<ErrorMsg, List<String>> clients =
                trainingService.clientsWhoBoughtTraining(trainingName);

        return ResponseResolver.resolve(clients);
    }

    @ApiOperation(value = "Trainings has bought by client")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Trainings has bought by client response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/trainingsBoughtByClient/{clientName}/clientName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    private ResponseEntity trainingsBoughtByClient(@PathVariable String clientName) {

        Either<ErrorMsg, List<String>> boughtTrainings =
                trainingService.getAllTrainingsBoughtByClient(clientName);

        return ResponseResolver.resolve(boughtTrainings);
    }
}
