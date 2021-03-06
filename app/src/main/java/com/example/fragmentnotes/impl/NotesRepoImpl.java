package com.example.fragmentnotes.impl;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;


import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.domain.NotesRepo;

import java.util.ArrayList;
import java.util.List;

public class NotesRepoImpl implements NotesRepo, Parcelable {
    private ArrayList<NoteEntity> cache = new ArrayList<>();
    private int counter = 0;

    public NotesRepoImpl() {
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

    public void clearAll(){
        counter = 0;
        cache.clear();
    }

    public NotesRepoImpl(Parcel in) {
        cache = in.createTypedArrayList(NoteEntity.CREATOR);
        counter = in.readInt();
    }

    public static final Creator<NotesRepoImpl> CREATOR = new Creator<NotesRepoImpl>() {
        @Override
        public NotesRepoImpl createFromParcel(Parcel in) {
            return new NotesRepoImpl(in);
        }

        @Override
        public NotesRepoImpl[] newArray(int size) {
            return new NotesRepoImpl[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(cache);
        dest.writeInt(counter);
    }
}
