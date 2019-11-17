package dna.jobhunt.repository;

import dna.jobhunt.domain.Category;
import dna.jobhunt.domain.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Integer> {

    @Query(value = "from JobOffer t WHERE :category = t.category AND (:chosenDate BETWEEN t.startDate AND t.endDate)")
    List<JobOffer> findAllValidJobOffersForCategoryAndDate(@Param("category")Category category, @Param("chosenDate") LocalDate chosenDate);

    @Query(value = "from JobOffer t JOIN User u on u.id = t.employer WHERE :employer = u.username AND (:chosenDate BETWEEN t.startDate AND t.endDate)")
    List<JobOffer> findAllValidJobOffersForEmployerAndDate(@Param("employer")String employer, @Param("chosenDate") LocalDate chosenDate);

    @Query(value = "from JobOffer t JOIN User u on u.id = t.employer WHERE :category = t.category AND :employer = u.username AND (:chosenDate BETWEEN t.startDate AND t.endDate)")
    List<JobOffer> findAllValidJobOffersForEmployerAndDateAndCategory(@Param("employer") String employer, @Param("category")Category category, @Param("chosenDate") LocalDate chosenDate);

    @Query(value = "from JobOffer t WHERE :chosenDate BETWEEN t.startDate AND t.endDate")
    List<JobOffer> findAllValidJobOffersForDate(@Param("chosenDate") LocalDate chosenDate);
}
