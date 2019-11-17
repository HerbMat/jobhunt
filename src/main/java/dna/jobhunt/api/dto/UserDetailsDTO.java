package dna.jobhunt.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(description = "Basic user details.")
public class UserDetailsDTO {
    @ApiModelProperty(notes = "Username", example = "JoeDoe")
    @NotBlank(message = "{username.not.blank}")
    private String username;

    @ApiModelProperty(notes = "User Password", example = "pass")
    @NotBlank(message = "{password.not.blank}")
    private String password;
}
