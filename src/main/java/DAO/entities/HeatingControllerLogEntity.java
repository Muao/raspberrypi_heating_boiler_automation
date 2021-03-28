package DAO.entities;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@Table(name = "HEATING_LOG")
public class HeatingControllerLogEntity extends BaseEntity{
    @NonNull private String action;
    @NonNull private Double temperature;
    @CreationTimestamp
    private LocalDateTime date;
}
