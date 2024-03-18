package com.bridgelabz.fundoo.authentication.label.service;

import com.bridgelabz.fundoo.authentication.label.dto.LabelDto;
import com.bridgelabz.fundoo.authentication.label.model.Label;
import com.bridgelabz.fundoo.authentication.label.repository.LabelRepository;
import com.bridgelabz.fundoo.authentication.response.Response;
import com.bridgelabz.fundoo.authentication.user.configuration.UserToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LabelServiceImpl implements ILabelService{

    @Autowired
    LabelRepository labelRepository;

    @Override
    public Mono<ResponseEntity<Response>> createLabel(LabelDto labelDto, long noteId, String token) {

        long userId = UserToken.verifyToken(token);
        ModelMapper modelMapper = new ModelMapper();
        Label labelDAO = modelMapper.map(labelDto, Label.class);

        labelDAO.setUserId(userId);
        labelDAO.setNoteId(noteId);

        return labelRepository.save(labelDAO).map(createdLabel -> new ResponseEntity<>(new Response(200, "Label created successfully", createdLabel), HttpStatus.OK))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Override
    public Mono<ResponseEntity<? extends Object>> getAllLabels(String token) {
        long userId = UserToken.verifyToken(token);
        return labelRepository.findAllByUserId(userId)
                .collectList()
                .flatMap(labelList -> {
                    if (!labelList.isEmpty()) {
                        return Mono.just(new ResponseEntity<>(Flux.fromIterable(labelList), HttpStatus.OK));
                    } else {
                        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                    }
                });
    }

    @Override
    public Mono<ResponseEntity<Object>> getLabelByNoteId(Long noteId) {
        return labelRepository.findByNoteId(noteId)
                .collectList()
                .flatMap(labelList -> {
                    if (!labelList.isEmpty()) {
                        return Mono.just(new ResponseEntity<>(labelList, HttpStatus.OK));
                    } else {
                        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                    }
                });
    }

    @Override
    public Mono<ResponseEntity<Response>> updateLabel(Long id, Label label) {
        return labelRepository.findById(id)
                .flatMap(existingLabel -> {
                    existingLabel.setName(label.getName());
                    existingLabel.setUserId(label.getUserId());
                    return labelRepository.save(existingLabel) .map(updatedLabel -> new ResponseEntity<>(new Response(200, "Label updated successfully", updatedLabel), HttpStatus.OK))
                            .defaultIfEmpty(new ResponseEntity<>(new Response(404, "Label not found"), HttpStatus.NOT_FOUND))
                            .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)));

                });
    }

    @Override
    public Mono<ResponseEntity<Response>> deleteLabel(Long id) {
        return labelRepository.deleteById(id) .then(Mono.just(new ResponseEntity<>(new Response(204, "Label deleted successfully"), HttpStatus.OK)))
                .defaultIfEmpty(new ResponseEntity<>(new Response(404, "Label not found"), HttpStatus.NOT_FOUND))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)));

    }
}
