package com.bridgelabz.fundoo.authentication.notes.service;
import com.bridgelabz.fundoo.authentication.notes.dto.NotesDto;
import com.bridgelabz.fundoo.authentication.notes.model.Notes;
import com.bridgelabz.fundoo.authentication.response.Response;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface INotesService {

    Mono<ResponseEntity<Response>> saveNote(NotesDto notesDto,String token);
    Mono<ResponseEntity<Response>> unArchive(String token);
    Mono<ResponseEntity<Response>> archive(String token);
    Mono<ResponseEntity<Response>> pin(String token);
    Mono<ResponseEntity<Response>> unpin(String token);
    Mono<ResponseEntity<Response>>getNoteById(Long noteId);
    Mono<ResponseEntity<Response>> updateNoteById(long noteId, NotesDto notesDto);
    Mono<ResponseEntity<Response>> deleteNoteById(String token);
}
