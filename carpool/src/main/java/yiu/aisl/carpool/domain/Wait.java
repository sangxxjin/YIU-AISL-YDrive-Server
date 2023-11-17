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
@Table(name="Wait")
public class Wait {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int waitNum;

    @Column(nullable = false, name="Carpool")
    private int carpoolNum;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private int checkNum;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
}

