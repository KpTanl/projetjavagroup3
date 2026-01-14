package com.group3.carrental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.carrental.entity.DiscussionNoteMessage;
import com.group3.carrental.entity.DiscussionNoteMessage.Cible;

@Repository
public interface DiscussionNoteMessageRepository extends JpaRepository<DiscussionNoteMessage, Long> {

    List<DiscussionNoteMessage> findByContrat_IdOrderByDateCreationAsc(Long contratId);

    List<DiscussionNoteMessage> findByContrat_IdAndCibleOrderByDateCreationAsc(Long contratId, Cible cible);
}
