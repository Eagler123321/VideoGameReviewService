package com.example.videogamereviewservice.repository;

import com.example.videogamereviewservice.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
