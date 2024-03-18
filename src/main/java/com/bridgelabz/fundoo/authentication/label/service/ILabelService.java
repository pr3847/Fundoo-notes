package com.bridgelabz.fundoo.authentication.label.service;

import com.bridgelabz.fundoo.authentication.label.dto.LabelDto;
import com.bridgelabz.fundoo.authentication.label.model.Label;
import com.bridgelabz.fundoo.authentication.response.Response;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ILabelService {

    Mono<ResponseEntity<Response>> createLabel(LabelDto labelDto, long noteId, String token);

    Mono<ResponseEntity<? extends Object>>  getAllLabels(String token);

    Mono <ResponseEntity<Object>> getLabelByNoteId(Long noteId);

    Mono<ResponseEntity<Response>> updateLabel(Long id, Label label);

    Mono<ResponseEntity<Response>> deleteLabel(Long id);
}
