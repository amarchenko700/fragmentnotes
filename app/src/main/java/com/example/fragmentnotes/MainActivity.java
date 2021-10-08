package com.example.fragmentnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.ui.NoteEditFragment;
import com.example.fragmentnotes.ui.NoteListFragment;

public class MainActivity extends AppCompatActivity implements NoteListFragment.ControllerNoteList,
        NoteEditFragment.ControllerNoteEdit {

    private boolean isLandscape;
    private NoteListFragment noteListFragment;
    private NoteEditFragment noteEditFragment;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();

        isLandscape = getResources().getBoolean(R.bool.isLandscape);
        openNotesList();
    }

    private void initFragments() {
        noteListFragment = new NoteListFragment();
        noteEditFragment = new NoteEditFragment();
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void openNotesList() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, noteListFragment)
                .commit();
    }

    @Override
    public void openNoteItem(NoteEntity item) {
        noteEditFragment.setNoteEntity(item);
        noteEditFragment.setNotesRepo(noteListFragment.getNotesRepo());
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, noteEditFragment)
                .addToBackStack(null)
                .commit();
    }

}