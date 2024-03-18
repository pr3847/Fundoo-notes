package com.bridgelabz.fundoo.authentication.label.dto;

public class LabelDto {

    private Long id;
    private String name;
    private Long userId;
    private Long noteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public LabelDto() {
    }

    public LabelDto(Long id, String name, Long userId, Long noteId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.noteId = noteId;
    }
}
