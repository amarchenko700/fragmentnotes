package com.example.fragmentnotes.impl;

import android.app.Application;

import androidx.annotation.Nullable;
import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.domain.NotesRepo;
import java.util.ArrayList;
import java.util.List;

public class NotesRepoImpl extends Application implements NotesRepo{
    private ArrayList<NoteEntity> cache = new ArrayList<>();
    private int counter = 0;

    public NotesRepoImpl() {
        this.createNote(new NoteEntity("День 1", "Решил заниматься андроидом"));
        this.createNote(new NoteEntity("День 2", "Записался на GeekBrains"));
        this.createNote(new NoteEntity("День 3", "И пошла жара"));
        this.createNote(new NoteEntity("День 4", "Теперь даже некогда отдыхать"));
        this.createNote(new NoteEntity("День 5", "Только то и делаю, что что-то клипаю, клипаю и клипаю"));
        this.createNote(new NoteEntity("День 6", "Иногда некогда покушать"));
        this.createNote(new NoteEntity("День 7", "Но в целом учиться - очень круто"));
        this.createNote(new NoteEntity("День 8", "Пишем на Java, скоро Kotlin - в общем мы крутые перцы "));
        this.createNote(new NoteEntity("День 9", "Все отлично"));
        this.createNote(new NoteEntity("День 10", "Все замечательно"));
        this.createNote(new NoteEntity("День 11", "Это такой типа дневник"));
        this.createNote(new NoteEntity("День 12", "Почти все"));
        this.createNote(new NoteEntity("День 13", "Еще не все"));
        this.createNote(new NoteEntity("День 14", "Теперь все"));
    }

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
    public boolean removeNote(NoteEntity note) {
        return cache.remove(note);
    }

    @Override
    public boolean editNote(NoteEntity note) {
        cache.set(cache.indexOf(note), note);
        return true;
    }

    @Override
    public void clearAll(){
        counter = 0;
        cache.clear();
    }
}
