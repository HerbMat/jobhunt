package dna.jobhunt.api.mapper;

import dna.jobhunt.api.dto.UserDTO;
import dna.jobhunt.api.dto.UserDetailsDTO;
import dna.jobhunt.domain.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDetailsDTO userDetailsDTO);
    UserDTO toUserDTO(User user);
    List<UserDTO> toUserDTOList(List<User> users);
}
