package com.AiNotesGenerator.Service;

import com.AiNotesGenerator.Entity.Note;
import com.AiNotesGenerator.Entity.User;
import com.AiNotesGenerator.Reopsitory.NoteRepository;
import com.AiNotesGenerator.Reopsitory.UserRepository;
import com.AiNotesGenerator.dto.NoteRequest;
import com.AiNotesGenerator.dto.NoteResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository,
                       UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public NoteResponse createNote(NoteRequest request) {

        User user = getCurrentUser();

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setUser(user);

        return mapToResponse(noteRepository.save(note));
    }

    public NoteResponse updateNote(Long id, NoteRequest request) {

        Note note = getNoteOwnedByUser(id);

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        return mapToResponse(noteRepository.save(note));
    }

    public void deleteNote(Long id) {
        noteRepository.delete(getNoteOwnedByUser(id));
    }

    public List<NoteResponse> getMyNotes() {
        return noteRepository.findByUserOrderByUpdatedAtDesc(getCurrentUser())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<NoteResponse> searchNotes(String keyword) {
        return noteRepository.searchByUserAndKeyword(getCurrentUser(), keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    private Note getNoteOwnedByUser(Long id) {

        Note note = noteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Note not found"));

        if (!note.getUser().getId()
                .equals(getCurrentUser().getId())) {

            throw new RuntimeException("Unauthorized access");
        }

        return note;
    }

    private NoteResponse mapToResponse(Note note) {

        NoteResponse response = new NoteResponse();

        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());

        return response;
    }
}