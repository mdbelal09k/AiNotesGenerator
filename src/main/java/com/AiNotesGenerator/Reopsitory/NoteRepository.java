package com.AiNotesGenerator.Reopsitory;

import com.AiNotesGenerator.Entity.*;
import com.AiNotesGenerator.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

	List<Note> findByUserOrderByUpdatedAtDesc(User user);

	@Query("SELECT n FROM Note n WHERE n.user = :user AND "
			+ "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	List<Note> searchByUserAndKeyword(@Param("user") User user, @Param("keyword") String keyword);
}