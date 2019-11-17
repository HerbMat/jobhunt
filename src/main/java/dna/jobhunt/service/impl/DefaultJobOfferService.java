package dna.jobhunt.service.impl;

import dna.jobhunt.api.dto.JobOfferDTO;
import dna.jobhunt.api.dto.JobOfferDetailsDTO;
import dna.jobhunt.api.mapper.JobOfferMapper;
import dna.jobhunt.domain.Category;
import dna.jobhunt.exception.EmployerNotFoundException;
import dna.jobhunt.repository.JobOfferRepository;
import dna.jobhunt.service.JobOfferService;
import dna.jobhunt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


/**
 * Default implementation of {@link JobOfferService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultJobOfferService implements JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferMapper jobOfferMapper;
    private final UserService userService;

    /**
     * {@inheritDoc}
     *
     * @throws EmployerNotFoundException when the employer with given username was not found.
     */
    @Override
    public JobOfferDTO addJobOffer(final JobOfferDetailsDTO jobOfferDetailsDTO) {
        log.debug("Creating job offer with data {}.", jobOfferDetailsDTO);
        final var employer = userService.getUserByUsername(jobOfferDetailsDTO.getEmployer())
                .orElseThrow(() -> new EmployerNotFoundException(jobOfferDetailsDTO.getEmployer()));
        final var jobOffer = jobOfferMapper.toJobOffer(jobOfferDetailsDTO);

        jobOffer.setEmployer(employer);

        return jobOfferMapper.toJobOfferDTO(jobOfferRepository.save(jobOffer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<JobOfferDTO> getValidJobOffersForCategoryAndEmployer(final Category category, final String employer) {
        if (category != null) {
            if (employer != null) {
                log.debug("Retrieving valid job offers for category {} and employer {}.", category, employer);
                return jobOfferMapper.toJobOfferDTOs(jobOfferRepository.findAllValidJobOffersForEmployerAndDateAndCategory(employer, category, LocalDate.now()));
            }
            log.debug("Retrieving valid job offers for category {}.", category);
            return jobOfferMapper.toJobOfferDTOs(jobOfferRepository.findAllValidJobOffersForCategoryAndDate(category, LocalDate.now()));
        }
        if (employer != null) {
            log.debug("Retrieving valid job offers for employer {}.", employer);
            return jobOfferMapper.toJobOfferDTOs(jobOfferRepository.findAllValidJobOffersForEmployerAndDate(employer, LocalDate.now()));
        }
        log.debug("Retrieving all valid jobs.");
        return jobOfferMapper.toJobOfferDTOs(jobOfferRepository.findAllValidJobOffersForDate(LocalDate.now()));
    }
}
