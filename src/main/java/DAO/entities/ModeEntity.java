package DAO.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Table(name = "MODE")
public class ModeEntity extends BaseEntity {
    @NonNull
    @Column(unique = true)
    private String modeName;
    @NonNull
    @Setter
    private Boolean mode;
    @NonNull
    @Setter
    private String editor;
    @CreationTimestamp
    @Setter
    private LocalDateTime editDate;
}
