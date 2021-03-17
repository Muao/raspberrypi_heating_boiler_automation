package DAO.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import utilites.ComportData;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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

    public ComPortDataEntity(ComportData comportData) {
        this.currentPort1 = comportData.getCurrentPort1();
        this.currentPort2 = comportData.getCurrentPort2();
        this.currentPort3 = comportData.getCurrentPort3();
        this.currentPort4 = comportData.getCurrentPort4();
        this.tempPort1 = comportData.getTempPort1();
        this.tempPort2 = comportData.getTempPort2();
        this.tempPort3 = comportData.getTempPort3();
        this.tempPort4 = comportData.getTempPort4();
    }
}
