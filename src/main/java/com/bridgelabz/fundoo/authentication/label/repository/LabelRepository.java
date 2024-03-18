package com.bridgelabz.fundoo.authentication.label.repository;

import com.bridgelabz.fundoo.authentication.label.model.Label;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LabelRepository extends ReactiveCrudRepository<Label,Long> {
    Flux<Label> findAllByUserId(long userId);

    Flux<Label> findByNoteId(Long noteId);
}
