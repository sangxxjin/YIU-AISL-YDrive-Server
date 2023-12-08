package yiu.aisl.carpool.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Wait")
public class Wait {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int waitNum;

    @ManyToOne
    @JoinColumn(name="carpoolNum", referencedColumnName="carpoolNum")
    private Carpool carpoolNum;

    @Column(nullable = false)
    private String guest;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private int checkNum;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
}

