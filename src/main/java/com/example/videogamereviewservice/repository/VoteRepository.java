package com.example.videogamereviewservice.repository;

import com.example.videogamereviewservice.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}

