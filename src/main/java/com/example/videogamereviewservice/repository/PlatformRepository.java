package com.example.videogamereviewservice.repository;

import com.example.videogamereviewservice.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
}
