package dna.jobhunt.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dna.jobhunt.domain.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(description = "Job Offer details.")
public class JobOfferDetailsDTO {

    @ApiModelProperty(notes = "Job Category", example = "IT", dataType = "Category")
    @NotNull(message = "{category.not.null}")
    private Category category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @ApiModelProperty(notes = "Job Offer Start Date", example = "14-11-2019")
    @NotNull(message = "{start.date.not.null}")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @ApiModelProperty(notes = "Job Offer End Date", example = "24-12-2019")
    @NotNull(message = "{end.date.not.null}")
    private LocalDate endDate;

    @ApiModelProperty(notes = "Employer's username who posted the job", example = "JoeDoe")
    @NotBlank(message = "{employer.not.blank}")
    private String employer;
}
