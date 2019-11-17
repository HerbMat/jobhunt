package dna.jobhunt.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dna.jobhunt.api.dto.JobOfferDTO;
import dna.jobhunt.api.dto.JobOfferDetailsDTO;
import dna.jobhunt.domain.Category;
import dna.jobhunt.exception.EmployerNotFoundException;
import dna.jobhunt.service.JobOfferService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = JobOfferController.class)
class JobOfferControllerTest {
    private static final String BASE_JOB_OFFERS_PATH = "/api/job-offers";

    private static final String EMPLOYER_USERNAME = "test";
    private static final LocalDate START_DATE_1 = LocalDate.now().plusDays(2);
    private static final LocalDate END_DATE_1 = LocalDate.now().minusDays(2);
    private static final int JOB_OFFER_ID_1 = 1;
    private static final LocalDate START_DATE_2 = LocalDate.now().plusDays(3);
    private static final LocalDate END_DATE_2 = LocalDate.now().minusDays(1);
    private static final int JOB_OFFER_ID_2 = 2;

    private static final String EMPLOYER_NOT_BLANK_MSG = "Employer username cannot be blank.";
    private static final String CATEGORY_NOT_NULL_MSG = "Category cannot be null.";
    private static final String START_DATE_NOT_NULL = "Start date cannot be null.";
    private static final String END_DATE_NOT_NULL = "End date cannot be null.";
    private static final String EMPLOYER_NOT_FOUND_FOR_USERNAME_MSG = "Employer username must belong to existing user.";

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JobOfferService jobOfferService;

    @DisplayName("It should return all job offers for specified category and employer.")
    @Test
    void getValidJobOffersForCategoryAndEmployer() throws Exception {
        when(jobOfferService.getValidJobOffersForCategoryAndEmployer(eq(Category.Courier), eq(EMPLOYER_USERNAME)))
                .thenReturn(List.of(createMockJobOffer1(), createMockJobOffer2()));

        mockMvc.perform(get(BASE_JOB_OFFERS_PATH)
                .param("category", Category.Courier.toString())
                .param("employer", EMPLOYER_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(JOB_OFFER_ID_1)))
                .andExpect(jsonPath("$[0].employer", equalTo(EMPLOYER_USERNAME)))
                .andExpect(jsonPath("$[0].category", equalTo(Category.Courier.toString())))
                .andExpect(jsonPath("$[0].endDate", equalTo(dateTimeFormatter.format(END_DATE_1))))
                .andExpect(jsonPath("$[0].startDate", equalTo(dateTimeFormatter.format(START_DATE_1))))
                .andExpect(jsonPath("$[1].id", equalTo(JOB_OFFER_ID_2)))
                .andExpect(jsonPath("$[1].employer", equalTo(EMPLOYER_USERNAME)))
                .andExpect(jsonPath("$[1].category", equalTo(Category.Courier.toString())))
                .andExpect(jsonPath("$[1].endDate", equalTo(dateTimeFormatter.format(END_DATE_2))))
                .andExpect(jsonPath("$[1].startDate", equalTo(dateTimeFormatter.format(START_DATE_2))));
    }

    @DisplayName("It should return all job offers for not specified category and employer.")
    @Test
    void getValidJobOffersAll() throws Exception {
        when(jobOfferService.getValidJobOffersForCategoryAndEmployer(isNull(), isNull()))
                .thenReturn(List.of(createMockJobOffer1(), createMockJobOffer2()));

        mockMvc.perform(get(BASE_JOB_OFFERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(JOB_OFFER_ID_1)))
                .andExpect(jsonPath("$[0].employer", equalTo(EMPLOYER_USERNAME)))
                .andExpect(jsonPath("$[0].category", equalTo(Category.Courier.toString())))
                .andExpect(jsonPath("$[0].endDate", equalTo(dateTimeFormatter.format(END_DATE_1))))
                .andExpect(jsonPath("$[0].startDate", equalTo(dateTimeFormatter.format(START_DATE_1))))
                .andExpect(jsonPath("$[1].id", equalTo(JOB_OFFER_ID_2)))
                .andExpect(jsonPath("$[1].employer", equalTo(EMPLOYER_USERNAME)))
                .andExpect(jsonPath("$[1].category", equalTo(Category.Courier.toString())))
                .andExpect(jsonPath("$[1].endDate", equalTo(dateTimeFormatter.format(END_DATE_2))))
                .andExpect(jsonPath("$[1].startDate", equalTo(dateTimeFormatter.format(START_DATE_2))));
    }

    @DisplayName("It should create new user with valid data.")
    @Test
    void addJobOffer() throws Exception {
        final var jobOfferDetailsDTOArgumentCaptor = ArgumentCaptor.forClass(JobOfferDetailsDTO.class);
        final var jobOffer = createMockJobOffer1();
        when(jobOfferService.addJobOffer(any(JobOfferDetailsDTO.class))).thenReturn(jobOffer);

        mockMvc.perform(post(BASE_JOB_OFFERS_PATH)
                .content(objectMapper.writeValueAsString(createMockJobOfferDetails()))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(JOB_OFFER_ID_1)))
                .andExpect(jsonPath("$.employer", equalTo(EMPLOYER_USERNAME)))
                .andExpect(jsonPath("$.category", equalTo(Category.Courier.toString())))
                .andExpect(jsonPath("$.endDate", equalTo(dateTimeFormatter.format(END_DATE_1))))
                .andExpect(jsonPath("$.startDate", equalTo(dateTimeFormatter.format(START_DATE_1))));

        verify(jobOfferService, times(1)).addJobOffer(jobOfferDetailsDTOArgumentCaptor.capture());
        final var jobOfferDetailsDTO = jobOfferDetailsDTOArgumentCaptor.getValue();
        assertThat(jobOfferDetailsDTO.getEmployer(), equalTo(EMPLOYER_USERNAME));
        assertThat(jobOfferDetailsDTO.getCategory(), equalTo(Category.Courier));
        assertThat(jobOfferDetailsDTO.getStartDate(), equalTo(START_DATE_1));
        assertThat(jobOfferDetailsDTO.getEndDate(), equalTo(END_DATE_1));
    }

    @DisplayName("It should throw validation errors.")
    @Test
    void addJobOfferValidationFail() throws Exception {
        mockMvc.perform(post(BASE_JOB_OFFERS_PATH)
                .content(objectMapper.writeValueAsString(new JobOfferDTO()))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$", hasItem(EMPLOYER_NOT_BLANK_MSG)))
                .andExpect(jsonPath("$", hasItem(CATEGORY_NOT_NULL_MSG)))
                .andExpect(jsonPath("$", hasItem(START_DATE_NOT_NULL)))
                .andExpect(jsonPath("$", hasItem(END_DATE_NOT_NULL)));

        verify(jobOfferService, never()).addJobOffer(any());
    }

    @DisplayName("It should throw employer not found for non existing employer.")
    @Test
    void addJobOfferNotFoundEmployer() throws Exception {

        when(jobOfferService.addJobOffer(any())).thenThrow(EmployerNotFoundException.class);

        mockMvc.perform(post(BASE_JOB_OFFERS_PATH)
                .content(objectMapper.writeValueAsString(createMockJobOfferDetails()))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$", hasItem(EMPLOYER_NOT_FOUND_FOR_USERNAME_MSG)));

        verify(jobOfferService, times(1)).addJobOffer(any());
    }


    private JobOfferDetailsDTO createMockJobOfferDetails() {
        return JobOfferDetailsDTO.builder()
                .employer(EMPLOYER_USERNAME)
                .category(Category.Courier)
                .endDate(END_DATE_1)
                .startDate(START_DATE_1)
                .build();
    }

    private JobOfferDTO createMockJobOffer1() {
        return JobOfferDTO.JobOfferDTOBuilder()
                .id(JOB_OFFER_ID_1)
                .employer(EMPLOYER_USERNAME)
                .category(Category.Courier)
                .endDate(END_DATE_1)
                .startDate(START_DATE_1)
                .build();
    }

    private JobOfferDTO createMockJobOffer2() {
        return JobOfferDTO.JobOfferDTOBuilder()
                .id(JOB_OFFER_ID_2)
                .employer(EMPLOYER_USERNAME)
                .category(Category.Courier)
                .endDate(END_DATE_2)
                .startDate(START_DATE_2)
                .build();
    }
}