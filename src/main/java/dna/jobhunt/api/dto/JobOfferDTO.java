package dna.jobhunt.api.dto;

import dna.jobhunt.domain.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(description = "Job Offer details with id.")
public class JobOfferDTO extends JobOfferDetailsDTO {

    @ApiModelProperty(notes = "Job Offer Id", example = "1")
    private Integer id;

    @Builder(builderMethodName = "JobOfferDTOBuilder")
    public JobOfferDTO(Category category, LocalDate startDate, LocalDate endDate, String employer, Integer id) {
        super(category, startDate, endDate, employer);
        this.id = id;
    }
}
