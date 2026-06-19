package com.AiNotesGenerator.controller;

import com.AiNotesGenerator.Service.*;
import com.AiNotesGenerator.dto.NoteRequest;
import com.AiNotesGenerator.dto.NoteResponse;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(
            @Valid @RequestBody NoteRequest request) {

        return ResponseEntity.ok(noteService.createNote(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody NoteRequest request) {

        return ResponseEntity.ok(
                noteService.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long id) {

        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getMyNotes() {
        return ResponseEntity.ok(noteService.getMyNotes());
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteResponse>> searchNotes(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                noteService.searchNotes(keyword));
    }
}