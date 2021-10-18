package com.example.fragmentnotes.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.fragmentnotes.R;
import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.impl.NotesRepoImpl;
import com.example.fragmentnotes.ui.NoteEditFragment;
import com.example.fragmentnotes.ui.NoteListFragment;

public class MainActivity extends AppCompatActivity implements NoteListFragment.ControllerNoteList,
        NoteEditFragment.ControllerNoteEdit {

    public static final String REPO_KEY = "REPO_KEY";
    private static final String NOTE_LIST_TAG = "NOTE_LIST_TAG";
    private static final String NOTE_EDIT_TAG = "NOTE_EDIT_TAG";
    private boolean isLandscape;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private int listLayout;
    private int noteLayout;
    /*
    Здесь не могу пользоваться типом NotesRepo, как в уроке, т.к. ругается что он не Parcelable.
    Как сделать так, чтобы интерфейс понимался как Parcelable?
    */
    public NotesRepoImpl notesRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNoteRepository(savedInstanceState);

        fragmentManager = getSupportFragmentManager();
        initToolbar();
        isLandscape = getResources().getBoolean(R.bool.isLandscape);
        initLayouts();
        openNotesList(notesRepo);
        removeNoteEditFragment();
    }

    private void removeNoteEditFragment() {
        NoteEditFragment noteEditFragment = (NoteEditFragment) fragmentManager.findFragmentByTag(NOTE_EDIT_TAG);
        if (!isLandscape && noteEditFragment != null) {
            fragmentManager
                    .beginTransaction()
                    .remove(noteEditFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(REPO_KEY, notesRepo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_note_menu) {
            openNoteItem(notesRepo.createNote(new NoteEntity()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNoteRepository(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            notesRepo = fillRepoByTestValuesRepo();
        } else {
            notesRepo = savedInstanceState.getParcelable(REPO_KEY);
        }
    }

    private static NotesRepoImpl fillRepoByTestValuesRepo() {
        NotesRepoImpl notesRepo = new NotesRepoImpl();
        notesRepo.createNote(new NoteEntity("День 1", "Решил заниматься андроидом"));
        notesRepo.createNote(new NoteEntity("День 2", "Записался на GeekBrains"));
        notesRepo.createNote(new NoteEntity("День 3", "И пошла жара"));
        notesRepo.createNote(new NoteEntity("День 4", "Теперь даже некогда отдыхать"));
        notesRepo.createNote(new NoteEntity("День 5", "Только то и делаю, что что-то клипаю, клипаю и клипаю"));
        notesRepo.createNote(new NoteEntity("День 6", "Иногда некогда покушать"));
        notesRepo.createNote(new NoteEntity("День 7", "Но в целом учиться - очень круто"));
        notesRepo.createNote(new NoteEntity("День 8", "Пишем на Java, скоро Kotlin - в общем мы крутые перцы "));
        notesRepo.createNote(new NoteEntity("День 9", "Все отлично"));
        notesRepo.createNote(new NoteEntity("День 10", "Все замечательно"));
        notesRepo.createNote(new NoteEntity("День 11", "Это такой типа дневник"));
        notesRepo.createNote(new NoteEntity("День 12", "Почти все"));
        notesRepo.createNote(new NoteEntity("День 13", "Еще не все"));
        notesRepo.createNote(new NoteEntity("День 14", "Теперь все"));
        return notesRepo;
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initLayouts() {
        if (isLandscape) {
            listLayout = R.id.fragment_container;
            noteLayout = R.id.fragment_note;
        } else {
            listLayout = R.id.fragment_container;
            noteLayout = R.id.fragment_container;
        }
    }

    private NoteListFragment getNoteListFragment() {
        NoteListFragment noteListFragment = (NoteListFragment) fragmentManager.findFragmentByTag(NOTE_LIST_TAG);
        if (noteListFragment == null) {
            noteListFragment = NoteListFragment.newInstance(notesRepo);
        } else {
            noteListFragment.setNotesRepo(notesRepo);
        }
        noteListFragment.setRecyclerViewAdapterData();
        return noteListFragment;
    }

    @Override
    public void openNotesList(NotesRepoImpl notesRepo) {
        fragmentManager
                .beginTransaction()
                .replace(listLayout, getNoteListFragment(), NOTE_LIST_TAG)
                .commit();
    }

    @Override
    public void openNoteItem(@Nullable NoteEntity item) {
        fragmentManager
                .beginTransaction()
                .replace(noteLayout, NoteEditFragment.newInstance(item, notesRepo), NOTE_EDIT_TAG)
                .addToBackStack(null)
                .commit();
    }

}