package dna.jobhunt.service.impl;

import dna.jobhunt.api.dto.JobOfferDTO;
import dna.jobhunt.api.dto.JobOfferDetailsDTO;
import dna.jobhunt.api.mapper.JobOfferMapper;
import dna.jobhunt.domain.Category;
import dna.jobhunt.domain.JobOffer;
import dna.jobhunt.domain.User;
import dna.jobhunt.exception.EmployerNotFoundException;
import dna.jobhunt.repository.JobOfferRepository;
import dna.jobhunt.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultJobOfferServiceTest {

    private static final String USERNAME = "test";

    @Mock
    private JobOfferRepository jobOfferRepository;

    @Mock
    private JobOfferMapper jobOfferMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private DefaultJobOfferService jobOfferService;

    @DisplayName("It creates new job and sets right employer successfully.")
    @Test
    public void addJobOffer() {
        final var jobOfferArgumentCaptor = ArgumentCaptor.forClass(JobOffer.class);
        final var mockJobOfferDetails = JobOfferDetailsDTO.builder()
                .employer(USERNAME)
                .build();
        final var jobOffer = new JobOffer();
        final var jobOfferDTO = new JobOfferDTO();
        final var employer = new User();
        when(jobOfferMapper.toJobOffer(mockJobOfferDetails)).thenReturn(jobOffer);
        when(jobOfferMapper.toJobOfferDTO(jobOffer)).thenReturn(jobOfferDTO);
        when(jobOfferRepository.save(jobOffer)).thenReturn(jobOffer);
        when(userService.getUserByUsername(USERNAME)).thenReturn(Optional.of(employer));

        final var result = jobOfferService.addJobOffer(mockJobOfferDetails);

        assertThat(result, equalTo(jobOfferDTO));

        verify(jobOfferRepository, times(1)).save(jobOfferArgumentCaptor.capture());
        final var savedJobOffer = jobOfferArgumentCaptor.getValue();
        assertThat(savedJobOffer.getEmployer(), equalTo(employer));
    }


    @DisplayName("It throws EmployerNotFoundException for not existing employer with given username.")
    @Test
    public void addJobOfferNotExistingUser() {
        final var mockJobOfferDetails = JobOfferDetailsDTO.builder()
                .employer(USERNAME)
                .build();
        when(userService.getUserByUsername(USERNAME)).thenThrow(EmployerNotFoundException.class);

        assertThrows(EmployerNotFoundException.class, () -> {
            jobOfferService.addJobOffer(mockJobOfferDetails);
        });

        verify(jobOfferRepository, never()).save(any());
    }

    @DisplayName("It should return valid job offers for given username and category")
    @Test
    public void getValidJobOffersForCategoryAndEmployer() {
        final List<JobOffer> jobOffers = List.of();
        when(jobOfferRepository.findAllValidJobOffersForEmployerAndDateAndCategory(eq(USERNAME), eq(Category.Drinks), any(LocalDate.class)))
            .thenReturn(jobOffers);

        final var result = jobOfferService.getValidJobOffersForCategoryAndEmployer(Category.Drinks, USERNAME);

        assertThat(result, equalTo(jobOffers));
        verify(jobOfferRepository, times(1))
                .findAllValidJobOffersForEmployerAndDateAndCategory(eq(USERNAME), eq(Category.Drinks), any(LocalDate.class));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForCategoryAndDate(eq(Category.Drinks), any(LocalDate.class));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForEmployerAndDate(eq(USERNAME), any(LocalDate.class));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForDate(any(LocalDate.class));
    }

    @DisplayName("It should return valid job offers for given employer username.")
    @Test
    public void getValidJobOffersForEmployer() {
        final List<JobOffer> jobOffers = List.of();
        when(jobOfferRepository.findAllValidJobOffersForEmployerAndDate(eq(USERNAME), any(LocalDate.class)))
                .thenReturn(jobOffers);

        final var result = jobOfferService.getValidJobOffersForCategoryAndEmployer(null, USERNAME);

        assertThat(result, equalTo(jobOffers));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForEmployerAndDateAndCategory(eq(USERNAME), eq(Category.Drinks), any(LocalDate.class));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForCategoryAndDate(eq(Category.Drinks), any(LocalDate.class));
        verify(jobOfferRepository, times(1))
                .findAllValidJobOffersForEmployerAndDate(eq(USERNAME), any(LocalDate.class));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForDate(any(LocalDate.class));
    }

    @DisplayName("It should return valid job offers for category")
    @Test
    public void getValidJobOffersForCategory() {
        final List<JobOffer> jobOffers = List.of();
        when(jobOfferRepository.findAllValidJobOffersForCategoryAndDate(eq(Category.Drinks), any(LocalDate.class)))
                .thenReturn(jobOffers);

        final var result = jobOfferService.getValidJobOffersForCategoryAndEmployer(Category.Drinks, null);

        assertThat(result, equalTo(jobOffers));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForEmployerAndDateAndCategory(eq(USERNAME), eq(Category.Drinks), any(LocalDate.class));
        verify(jobOfferRepository, times(1))
                .findAllValidJobOffersForCategoryAndDate(eq(Category.Drinks), any(LocalDate.class));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForEmployerAndDate(eq(USERNAME), any(LocalDate.class));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForDate(any(LocalDate.class));
    }

    @DisplayName("It should return all valid job offers.")
    @Test
    public void getValidJobOffers() {
        final List<JobOffer> jobOffers = List.of();
        when(jobOfferRepository.findAllValidJobOffersForDate(any(LocalDate.class)))
                .thenReturn(jobOffers);

        final var result = jobOfferService.getValidJobOffersForCategoryAndEmployer(null, null);

        assertThat(result, equalTo(jobOffers));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForEmployerAndDateAndCategory(eq(USERNAME), eq(Category.Drinks), any(LocalDate.class));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForCategoryAndDate(eq(Category.Drinks), any(LocalDate.class));
        verify(jobOfferRepository, never())
                .findAllValidJobOffersForEmployerAndDate(eq(USERNAME), any(LocalDate.class));
        verify(jobOfferRepository, times(1))
                .findAllValidJobOffersForDate(any(LocalDate.class));
    }
}