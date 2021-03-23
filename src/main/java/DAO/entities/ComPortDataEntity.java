package DAO.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
@Entity
@Table(name = "COMPORT_DATA", schema = "bau")
public class ComPortDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double currentPort1;
    private double currentPort2;
    private double currentPort3;
    private double currentPort4;
    private double tempPort1;
    private double tempPort2;
    private double tempPort3;
    private double tempPort4;
    @CreationTimestamp
    private LocalDateTime date;

    public ComPortDataEntity(String[] input) {
        this.currentPort1 = Double.parseDouble(input[0]);
        this.currentPort2 = Double.parseDouble(input[1]);
        this.currentPort3 = Double.parseDouble(input[2]);
        this.currentPort4 = Double.parseDouble(input[3]);
        this.tempPort1 = Double.parseDouble(input[4]);
        this.tempPort2 = Double.parseDouble(input[5]);
        this.tempPort3 = Double.parseDouble(input[6]);
        this.tempPort4 = Double.parseDouble(input[7]);
        this.setDate(LocalDateTime.now());
    }

    public ComPortDataEntity(Double[] input) {
        this.currentPort1 = input[0];
        this.currentPort2 = input[1];
        this.currentPort3 = input[2];
        this.currentPort4 = input[3];
        this.tempPort1 = input[4];
        this.tempPort2 = input[5];
        this.tempPort3 = input[6];
        this.tempPort4 = input[7];
        this.setDate(LocalDateTime.now());
    }

    public ComPortDataEntity(ComPortDataEntity input) {
        this.id = input.id;
        this.currentPort1 = input.currentPort1;
        this.currentPort2 = input.currentPort2;
        this.currentPort3 = input.currentPort3;
        this.currentPort4 = input.currentPort4;
        this.tempPort1 = input.tempPort1;
        this.tempPort2 = input.tempPort2;
        this.tempPort3 = input.tempPort3;
        this.tempPort4 = input.tempPort4;
        this.setDate(LocalDateTime.now());
    }
}
