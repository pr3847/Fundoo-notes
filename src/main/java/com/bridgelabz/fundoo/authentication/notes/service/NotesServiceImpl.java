package com.bridgelabz.fundoo.authentication.notes.service;


import com.bridgelabz.fundoo.authentication.notes.dto.NotesDto;
import com.bridgelabz.fundoo.authentication.notes.model.Notes;
import com.bridgelabz.fundoo.authentication.notes.repository.NotesRepository;
import com.bridgelabz.fundoo.authentication.response.Response;
import com.bridgelabz.fundoo.authentication.user.configuration.UserToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotesServiceImpl implements INotesService {

    @Autowired
    public NotesRepository noteRepository;
    @Override
    public Mono<ResponseEntity<Response>> saveNote(NotesDto notesDto, String token) {
        long userId= UserToken.verifyToken(token);
        ModelMapper modelMapper=new ModelMapper();
        Notes newnotes=modelMapper.map(notesDto,Notes.class);
        newnotes.setUserId(userId);
        return noteRepository.save(newnotes).map(savedNote -> new ResponseEntity<>(new Response(200,"Note saved successfully"), HttpStatus.OK))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500,"Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Override
    public Mono<ResponseEntity<Response>> unArchive(String token) {
        long userId = UserToken.verifyToken(token);

        return noteRepository.findByUserIdAndArchivedFalse(userId)
                .collectList()
                .flatMap(noteList -> {
                    if (!noteList.isEmpty()) {
                        return Mono.just(new ResponseEntity<>(new Response(200, "Unarchived notes retrieved successfully", noteList), HttpStatus.OK));
                    } else {
                        return Mono.just(new ResponseEntity<>(new Response(404, "No unarchived notes found"), HttpStatus.NOT_FOUND));
                    }
                })
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)));
    }


    @Override
    public Mono<ResponseEntity<Response>> archive(String token) {
        long userId = UserToken.verifyToken(token);
        return noteRepository.findByUserIdAndArchivedTrue(userId)
                .collectList()
                .flatMap(noteList -> {
                    if (!noteList.isEmpty()) {
                        return Mono.just(new ResponseEntity<>(new Response(200, "Archived notes retrieved successfully", noteList), HttpStatus.OK));
                    } else {
                        return Mono.just(new ResponseEntity<>(new Response(404, "No archived notes found"), HttpStatus.NOT_FOUND));
                    }
                })
                .onErrorResume(error -> {
                    // Log the error for debugging purposes
                    error.printStackTrace();
                    return Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }



    @Override
    public Mono<ResponseEntity<Response>> getNoteById(Long noteId) {
        return noteRepository.findById(noteId)
                .map(note -> new ResponseEntity<>(new Response(200, "Note retrieved successfully", note), HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(new Response(404, "Note not found"), HttpStatus.NOT_FOUND)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)));

    }

    @Override
    public Mono<ResponseEntity<Response>> pin(String token){
        long userId=UserToken.verifyToken(token);
        return noteRepository.findByUserIdAndPinnedTrue(userId)
                .collectList()
                .map(notes -> new ResponseEntity<>(new Response(200, "Pinned notes retrieved successfully", notes),HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(new Response(404, "No pinned notes found"), HttpStatus.NOT_FOUND)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Override
    public Mono<ResponseEntity<Response>> unpin(String token){
        long userId=UserToken.verifyToken(token);
        return noteRepository.findByUserIdAndPinnedFalse(userId)
                .collectList()
                .map(notes -> new ResponseEntity<>(new Response(200, "Unpinned notes retrieved successfully", notes), HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(new Response(404, "No unpinned notes found"), HttpStatus.NOT_FOUND)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)));
    }


    @Override
    public Mono<ResponseEntity<Response>> updateNoteById(long id, NotesDto notesDto) {
        ModelMapper modelMapper=new ModelMapper();
        Notes notes=modelMapper.map(notesDto,Notes.class);
        return noteRepository.findById(id)
                .flatMap(existingNote -> {
                    existingNote.setTitle(notes.getTitle());
                    existingNote.setDescription(notes.getDescription());
                    existingNote.setArchived(notes.isArchived());
                    existingNote.setArchived(notes.isPinned());
                    existingNote.setTrash(notes.isTrash());
                    return noteRepository.save(existingNote)
                            .map(updatedNote -> new ResponseEntity<>(new Response(200, "Note updated successfully",updatedNote), HttpStatus.OK))
                            .switchIfEmpty(Mono.just(new ResponseEntity<>(new Response(404, "Note not found"), HttpStatus.NOT_FOUND)))
                            .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)));

                });
    }


    @Override
    public Mono<ResponseEntity<Response>> deleteNoteById(String token) {
        long userId=UserToken.verifyToken(token);
        return noteRepository.findByUserIdAndTrashTrue(userId)
                .flatMap(note -> noteRepository.delete((Notes) note)
                        .then(Mono.just(new ResponseEntity<>(new Response(204, "Note deleted successfully"), HttpStatus.OK))))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(new Response(404, "Note not found"), HttpStatus.NOT_FOUND)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)))
                .next();
    }
}
