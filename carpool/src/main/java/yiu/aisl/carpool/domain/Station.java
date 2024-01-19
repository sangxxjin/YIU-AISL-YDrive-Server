package yiu.aisl.carpool.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="station")
public class Station {
    @Id
    private int stationNum;
    @Column
    private String stationName;
    @Column
    private String city;
    @Column
    private String district;
    @Column
    private int exitNum;
}
