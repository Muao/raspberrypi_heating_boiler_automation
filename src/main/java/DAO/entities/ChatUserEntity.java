package DAO.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
@Table(name = "CHAT_USER")
public class ChatUserEntity extends BaseEntity{
    @NonNull Integer userId;
    @NonNull private String username;
    @NonNull private String chatId;
    @CreationTimestamp
    private LocalDateTime date;
}
