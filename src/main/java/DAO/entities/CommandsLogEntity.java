package DAO.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table(name = "COMMANDS_LOG", schema = "bau")
public class CommandsLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String command;
    private String username;
    private Timestamp date;

    public CommandsLogEntity(String command, String username) {
        this.command = command;
        this.username = username;
    }
}
