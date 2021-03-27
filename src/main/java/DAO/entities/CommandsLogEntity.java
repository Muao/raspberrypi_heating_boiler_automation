package DAO.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "COMMANDS_LOG", schema = "bau")
public class CommandsLogEntity extends BaseEntity{
    private String command;
    private String username;
    @CreationTimestamp
    private LocalDateTime date;

    public CommandsLogEntity(String command, String username) {
        this.command = command;
        this.username = username;
    }
}
