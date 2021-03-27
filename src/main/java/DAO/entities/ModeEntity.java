package DAO.entities;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@Table(name = "MODE")
public class ModeEntity extends BaseEntity {
    @NonNull
    @Column(unique = true)
    private String modeName;
    @NonNull
    private Boolean mode;
    @NonNull
    private String editor;
    @CreationTimestamp
    private LocalDateTime editDate;
}
