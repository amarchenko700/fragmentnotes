package com.example.fragmentnotes.domain;

import androidx.annotation.Nullable;

import java.util.List;

public interface NotesRepo {
    List<NoteEntity> getNotes();
    @Nullable
    NoteEntity createNote(NoteEntity note);
    NoteEntity getNote(NoteEntity id);
    boolean removeNote(NoteEntity note);
    boolean editNote(NoteEntity note);
    void clearAll();
}
