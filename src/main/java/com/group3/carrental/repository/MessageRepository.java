package com.group3.carrental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.group3.carrental.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByDestinataire_IdOrderByDateEnvoiDesc(int destinataireId);

    @Query("""
        SELECT m FROM Message m
        WHERE (m.expediteur.id = :u1 AND m.destinataire.id = :u2)
           OR (m.expediteur.id = :u2 AND m.destinataire.id = :u1)
        ORDER BY m.dateEnvoi ASC
    """)
    List<Message> findConversation(@Param("u1") int u1, @Param("u2") int u2);
}
