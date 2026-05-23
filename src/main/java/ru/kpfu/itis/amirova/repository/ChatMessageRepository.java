package ru.kpfu.itis.amirova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.amirova.model.ChatMessage;
import ru.kpfu.itis.amirova.model.User;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop50ByOrderBySentAtDesc();

    List<ChatMessage> findByAuthor(User author);

    @Query("SELECT m FROM ChatMessage m WHERE LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY m.sentAt DESC")
    List<ChatMessage> searchByContent(@Param("keyword") String keyword);
}