package com.bridgelabz.fundoo.authentication.notes.controller;

import com.bridgelabz.fundoo.authentication.notes.dto.NotesDto;
import com.bridgelabz.fundoo.authentication.notes.model.Notes;
import com.bridgelabz.fundoo.authentication.notes.service.INotesService;
import com.bridgelabz.fundoo.authentication.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    public INotesService iNotesService;

    @PostMapping("/{token}")
    public Mono<ResponseEntity<Response>> saveNote(@RequestBody NotesDto note, @PathVariable String token) {
        return iNotesService.saveNote(note,token);
    }


    @GetMapping("/unarchive/{token}")
    public Mono<ResponseEntity<Response>> unArchive(@PathVariable String token) {
        return iNotesService.unArchive(token);
    }

    @GetMapping("/archived/{token}")
    public Mono<ResponseEntity<Response>> archive(@PathVariable String token) {
        return iNotesService.archive(token);
    }

    @GetMapping("/pin/{token}")
    Mono<ResponseEntity<Response>> pin(@PathVariable String token) {
        return iNotesService.pin(token);
    }

    @GetMapping("/unpin/{token}")
    public Mono<ResponseEntity<Response>> unpin(@PathVariable String token) {
        return iNotesService.unpin(token);
    }

    @GetMapping("/{noteId}")
    public Mono<ResponseEntity<Response>> getNoteById(@PathVariable Long noteId) {
        return iNotesService.getNoteById(noteId);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Response>> updateNoteById(@PathVariable Long id, @RequestBody NotesDto note) {
        return iNotesService.updateNoteById(id, note);
    }

    @DeleteMapping("/trash/{token}")
    public Mono<ResponseEntity<Response>> deleteNoteById(@PathVariable String token) {
        return iNotesService.deleteNoteById(token);
    }

}
