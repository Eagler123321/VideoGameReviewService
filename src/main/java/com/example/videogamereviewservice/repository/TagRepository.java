package com.example.videogamereviewservice.repository;

import com.example.videogamereviewservice.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
