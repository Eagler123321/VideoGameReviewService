package com.example.videogamereviewservice.repository;

import com.example.videogamereviewservice.entity.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
/*    @Query("SELECT DISTINCT g FROM Game g JOIN g.genres genre WHERE genre.name = :genreName")
    List<Game> findByGenreName(@Param("genreName") String genreName, Pageable pageable);

    @Query("SELECT DISTINCT g FROM Game g JOIN g.genres genre WHERE genre.name = :genreName AND g.rating >= :minRating")
    List<Game> findByGenreNameAndMinRating(@Param("genreName") String genreName,
                                           @Param("minRating") Double minRating,
                                           Pageable pageable);*/

    List<Game> findByTitleContainingIgnoreCase(String title);
}
