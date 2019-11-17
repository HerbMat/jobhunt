package dna.jobhunt.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(description = "User details with id.")
public class UserDTO extends UserDetailsDTO {

    @ApiModelProperty(notes = "Job Offer Id", example = "1")
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @ApiModelProperty(notes = "User creation date", example = "12-11-2019 00:00:00")
    private LocalDateTime creationDate;

    @Builder(builderMethodName = "userDTOBuilder")
    public UserDTO(String username, String password, Integer id, LocalDateTime creationDate) {
        super(username, password);
        this.id = id;
        this.creationDate = creationDate;
    }
}
