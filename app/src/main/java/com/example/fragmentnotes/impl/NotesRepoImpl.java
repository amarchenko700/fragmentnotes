package com.example.fragmentnotes.impl;

import androidx.annotation.Nullable;


import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.domain.NotesRepo;

import java.util.ArrayList;
import java.util.List;

public class NotesRepoImpl implements NotesRepo {
    private final ArrayList<NoteEntity> cache = new ArrayList<>();
    private int counter = 0;

    @Override
    public List<NoteEntity> getNotes() {
        return new ArrayList<>(cache);
    }

    public NoteEntity getNote(NoteEntity note){
        int indexNote = cache.indexOf(note);
        if(indexNote != -1){
            return cache.get(indexNote);
        }
        return new NoteEntity();
    }

    @Nullable
    @Override
    public NoteEntity createNote(NoteEntity note) {
        Integer newId = counter++;
        note.setId(newId);
        cache.add(note);
        return note;
    }

    @Override
    public boolean removeNote(Integer id) {
        for (int i = 0; i < cache.size(); i++) {
            if(cache.get(i).getId() == id){
                cache.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean editNote(Integer id, NoteEntity note) {
        //removeNote(id);
        //note.setId(id);
        //cache.add(note);
        cache.set(id, note);
        return true;
    }
}
