package DAO.servises;

import DAO.entities.ChatUserEntity;
import DAO.repository.ChatUserRepository;
import org.telegram.telegrambots.meta.api.objects.User;

public class ChatUserService {

    public static String saveUser(User user, String chatId) {
        String result = "User already exist in db";
        final ChatUserRepository chatUserRepository = ChatUserRepository.INSTANCE;
        final ChatUserEntity chatUserEntity = chatUserRepository.get(chatId);
        if (chatUserEntity == null){
            chatUserRepository.save(user, chatId);
            result = "User successfully saved";
        }
        return result;
    }
}
