package yiu.aisl.carpool.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="OwnerReviewed")
public class OwnerReviewed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int ownerReviewedNum;

    @Column(nullable = false, name="Carpool")
    private int carpoolNum;

    @Column
    private int star;

    @Column
    private String review;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @Column(nullable = false)
    private String email;
}
