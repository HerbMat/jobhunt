package dna.jobhunt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobOffer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "{category.not.null}")
    private Category category;

    @NotNull(message = "{start.date.not.null}")
    private LocalDate startDate;

    @NotNull(message = "{end.date.not.null}")
    private LocalDate endDate;

    @ManyToOne
    private User employer;
}
