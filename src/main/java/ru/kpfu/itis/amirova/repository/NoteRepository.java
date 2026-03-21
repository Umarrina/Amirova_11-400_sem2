package ru.kpfu.itis.amirova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.amirova.model.Note;
import ru.kpfu.itis.amirova.model.User;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n JOIN FETCH n.author WHERE n.author = :author")
    List<Note> findByAuthor(@Param("author") User author);

    @Query("SELECT n FROM Note n JOIN FETCH n.author WHERE n.isPublic = true")
    List<Note> findByIsPublicTrue();

    @Query("SELECT n FROM Note n JOIN FETCH n.author")
    List<Note> findAllWithAuthor();

    @Modifying
    @Transactional
    @Query("DELETE FROM Note n WHERE n.id = :id")
    void deleteNoteById(@Param("id") Long id);

}
