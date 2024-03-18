package com.bridgelabz.fundoo.authentication.label.controller;

import com.bridgelabz.fundoo.authentication.label.dto.LabelDto;
import com.bridgelabz.fundoo.authentication.label.model.Label;
import com.bridgelabz.fundoo.authentication.label.service.ILabelService;
import com.bridgelabz.fundoo.authentication.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/label")
public class LabelController {

    @Autowired
    ILabelService iLabelService;



    @PostMapping("/{noteId}/{token}")
    public Mono<ResponseEntity<Response>> createLabel(@RequestBody LabelDto label, @PathVariable long noteId, @PathVariable String token) {
        return iLabelService.createLabel(label,noteId,token);
    }

    @GetMapping("/all/{token}")
    public Mono<ResponseEntity<? extends Object>>  getAllLabels(@PathVariable String token) {
        return iLabelService.getAllLabels(token);

    }

    @GetMapping("/{noteId}")
    public Mono <ResponseEntity<Object>> getLabelById(@PathVariable Long noteId) {
        return iLabelService.getLabelByNoteId(noteId);
    }



    @PutMapping("/{id}")
    public Mono<ResponseEntity<Response>> updateLabel(@PathVariable Long id, @RequestBody Label label) {
        return iLabelService.updateLabel(id, label);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Response>> deleteLabel(@PathVariable Long id) {
        return iLabelService.deleteLabel(id);
    }

}
