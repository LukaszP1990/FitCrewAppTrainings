package com.fitcrew.FitCrewAppTrainings.resources;

import com.fitcrew.FitCrewAppTrainings.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainings.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainings.services.TrainingService;
import com.fitcrew.FitCrewAppTrainings.services.TrainingServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Trainings resources")
@Slf4j
@RestController
@RequestMapping("/training")
class TrainingResource {

    private final TrainingServiceFacade trainingServiceFacade;

    public TrainingResource(TrainingServiceFacade trainingServiceFacade) {
        this.trainingServiceFacade = trainingServiceFacade;
    }

    @ApiOperation(value = "Return all trainings from trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful all trainings from trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/getTrainerTrainings/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})

    public ResponseEntity getTrainerTrainings(@PathVariable String trainerEmail) {
        log.debug("Trainer training by trainer email address: {}", trainerEmail);

        return ResponseResolver.resolve(
                trainingServiceFacade.getTrainerTrainings(trainerEmail)
        );
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
        log.debug("Create training: {}", trainingDto);

        return ResponseResolver.resolve(
                trainingServiceFacade.createTraining(trainingDto)
        );
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
        log.debug("Delete training by training name: {} \n trainer email address: {}", trainerEmail, trainingName);

        return ResponseResolver.resolve(
                trainingServiceFacade.deleteTraining(trainingName, trainerEmail)
        );
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
        log.debug("Update training: {} \n by trainer email address: {}", trainingDto, trainerEmail);

        return ResponseResolver.resolve(
                trainingServiceFacade.updateTraining(trainingDto, trainerEmail)
        );
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
    public ResponseEntity selectTraining(@PathVariable String trainerEmail,
                                         @PathVariable String trainingName) {
        log.debug("Select training by training name: {} \n trainer email address: {}", trainerEmail, trainingName);

        return ResponseResolver.resolve(
                trainingServiceFacade.selectTraining(trainerEmail, trainingName)
        );
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
    public ResponseEntity clientsWhoBoughtTraining(@PathVariable String trainingName) {
        log.debug("Clients who bought training by training name: {}", trainingName);

        return ResponseResolver.resolve(
                trainingServiceFacade.clientsWhoBoughtTraining(trainingName)
        );
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
    public ResponseEntity trainingsBoughtByClient(@PathVariable String clientName) {
        log.debug("Trainings bought by client: {}", clientName);

        return ResponseResolver.resolve(
                trainingServiceFacade.getAllTrainingsBoughtByClient(clientName)
        );
    }
}
