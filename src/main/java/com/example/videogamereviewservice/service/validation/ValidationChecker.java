package com.example.videogamereviewservice.service.validation;

import com.example.videogamereviewservice.error.InvalidIdException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class ValidationChecker {
    public static <T> void checkInvalidId(JpaRepository<T, Long> repository, Long id, String type) {
        if (id == null) {
            throw new InvalidIdException("%s id cannot be null".formatted(type));
        }

        repository.findById(id)
                .orElseThrow(() -> new InvalidIdException("Some %s id does no exist".formatted(type)));
    }

    public static <T> void checkInvalidIds(JpaRepository<T, Long> repository, List<Long> ids, String type) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        List<T> entities = repository.findAllById(ids);

        if (entities.size() != ids.size()){
            throw new InvalidIdException("Some %s ids do not exist".formatted(type));
        }
    }
}
