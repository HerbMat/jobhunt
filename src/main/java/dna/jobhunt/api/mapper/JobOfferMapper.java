package dna.jobhunt.api.mapper;

import dna.jobhunt.api.dto.JobOfferDTO;
import dna.jobhunt.api.dto.JobOfferDetailsDTO;
import dna.jobhunt.domain.JobOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobOfferMapper {

    @Mapping(ignore = true, target = "employer")
    JobOffer toJobOffer(JobOfferDetailsDTO jobOfferDetailsDTO);

    @Mapping(source = "employer.username", target = "employer")
    JobOfferDTO toJobOfferDTO(JobOffer jobOffer);

    List<JobOfferDTO> toJobOfferDTOs(List<JobOffer> jobOffers);
}
