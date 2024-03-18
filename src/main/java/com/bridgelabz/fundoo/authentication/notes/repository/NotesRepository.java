package com.bridgelabz.fundoo.authentication.notes.repository;

import com.bridgelabz.fundoo.authentication.notes.model.Notes;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NotesRepository extends ReactiveCrudRepository<Notes,Long> {

    Flux<Notes> findByUserIdAndArchivedFalse(Long userId);

    Flux<Notes> findByUserIdAndArchivedTrue(Long userId);

    Flux<Notes> findByUserIdAndPinnedTrue(Long userId);

    Flux<Notes> findByUserIdAndPinnedFalse(long userId);

    Flux<Notes> findByUserIdAndTrashTrue(long userId);
}
