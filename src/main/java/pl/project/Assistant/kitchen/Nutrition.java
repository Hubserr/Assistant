package pl.project.Assistant.kitchen;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Nutrition {
    private Integer calories;

    @Column(precision = 6,scale = 1)
    private BigDecimal protein;

    @Column(precision = 6,scale = 1)
    private BigDecimal fat;

    @Column(precision = 6,scale = 1)
    private BigDecimal carbs;


}
