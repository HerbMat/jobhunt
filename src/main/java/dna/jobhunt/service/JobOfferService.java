package dna.jobhunt.service;

import dna.jobhunt.api.dto.JobOfferDTO;
import dna.jobhunt.api.dto.JobOfferDetailsDTO;
import dna.jobhunt.domain.Category;
import dna.jobhunt.domain.JobOffer;

import java.util.Collection;

/**
 * Service for managing {@link dna.jobhunt.domain.JobOffer}
 */
public interface JobOfferService {

    /**
     * Creates new job offer.
     *
     * @param jobOfferDetailsDTO new job offer details.
     *
     * @return newly created job.
     */
    JobOfferDTO addJobOffer(final JobOfferDetailsDTO jobOfferDetailsDTO);

    /**
     * It get valid job offers for given category and employer.
     * Valid job offer is when the current date is between {@link JobOffer#getStartDate()}
     * and {@link JobOffer#getEndDate()}.
     *
     * @param category job offer category
     * @param employer employer username.
     *
     * @return collection of valid jobs for given category and employer
     */
    Collection<JobOfferDTO> getValidJobOffersForCategoryAndEmployer(final Category category, final String employer);
}
