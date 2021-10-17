package com.example.fragmentnotes.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fragmentnotes.R;
import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.impl.NotesRepoImpl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NoteListFragment.ControllerNoteList,
        NoteEditFragment.ControllerNoteEdit {

    public static final String REPO_KEY = "REPO_KEY";
    private static final String NOTE_LIST_TAG = "NOTE_LIST_TAG";
    private static final String NOTE_EDIT_TAG = "NOTE_EDIT_TAG";
    private static final String ADDITIONAL_FRAGMENT_TAG = "ADDITIONAL_FRAGMENT_TAG";
    private boolean isLandscape;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private NoteListFragment noteListFragment;
    private int listLayout;
    private int noteLayout;
    private final Map<Integer, Fragment> fragments = createFragments();
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
        noteListFragment = NoteListFragment.newInstance(notesRepo);
        noteListFragment.setRecyclerViewAdapterData();
        initToolbar();
        isLandscape = getResources().getBoolean(R.bool.isLandscape);
        initLayouts();
        initBottomNavigation();
        openNotesList();
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

    private void removeAdditionalFragment() {
        Fragment additionalFragment = fragmentManager.findFragmentByTag(MainActivity.ADDITIONAL_FRAGMENT_TAG);
        if (additionalFragment != null) {
            fragmentManager
                    .beginTransaction()
                    .remove(additionalFragment)
                    .commit();
        }
    }

    private Map<Integer, Fragment> createFragments() {
        Map<Integer, Fragment> fragments = new HashMap<>();

        fragments.put(R.id.about, new AboutFragment());
        fragments.put(R.id.settings, new SettingsFragment());
        fragments.put(R.id.profile, new ProfileFragment());

        return fragments;
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
        } else if (item.getItemId() == R.id.clear_note_menu) {
            notesRepo.clearAll();
            noteListFragment.setRecyclerViewAdapterData();
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

    private void initBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.list_notes) {
                openNotesList();
            } else {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container_additional_fragments, Objects.requireNonNull(fragments.get(item.getItemId())), MainActivity.ADDITIONAL_FRAGMENT_TAG)
                        .commit();
            }
            return true;
        });
    }

    private void openNotesList() {
        removeAdditionalFragment();

        fragmentManager
                .beginTransaction()
                .replace(listLayout, noteListFragment, NOTE_LIST_TAG)
                .commit();
    }

    @Override
    public void createdNote(NoteEntity noteEntity){
        noteListFragment.setCreatedNoteEntity(noteEntity);
        openNotesList();
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