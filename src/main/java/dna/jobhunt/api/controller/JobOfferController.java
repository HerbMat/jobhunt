package dna.jobhunt.api.controller;

import dna.jobhunt.api.dto.JobOfferDTO;
import dna.jobhunt.api.dto.JobOfferDetailsDTO;
import dna.jobhunt.domain.Category;
import dna.jobhunt.service.JobOfferService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Api(value="Job Offer Controller", tags = "job-offer")
@RequestMapping("/api/job-offers")
@RestController
@RequiredArgsConstructor
@Slf4j
public class JobOfferController {
    private final JobOfferService jobOfferService;

    @ApiOperation(value = "Get valid job offers for given employer and category. " +
            "If parameters are not specified", response = Collection.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved user successfully."),
            @ApiResponse(code = 400, message = "Validation error."),
            @ApiResponse(code = 500, message = "Internal service error.")
    })
    @GetMapping
    public Collection<JobOfferDTO> getValidJobOffersForCategoryAndEmployer(
            @ApiParam(value = "Employer username")
            @RequestParam(value = "employer", required = false)
                String employer,
            @RequestParam(value = "category", required = false)
            @ApiParam(value = "Job Category")
                Category category) {
        log.debug("Retrieving valid job offers for category {} and employer {}", employer, category);

        return jobOfferService.getValidJobOffersForCategoryAndEmployer(category, employer);
    }

    @ApiOperation(value = "Create new job offer with given data", response = JobOfferDTO.class)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created new job offer successfully."),
            @ApiResponse(code = 400, message = "Validation error."),
            @ApiResponse(code = 500, message = "Internal service error.")
    })
    @PostMapping
    public JobOfferDTO addJobOffer(
            @ApiParam(value = "New Job offer details")
            @Valid
            @RequestBody JobOfferDetailsDTO jobOfferDetailsDTO) {
        log.debug("Received request to create user with details {}", jobOfferDetailsDTO);

        return jobOfferService.addJobOffer(jobOfferDetailsDTO);
    }
}
