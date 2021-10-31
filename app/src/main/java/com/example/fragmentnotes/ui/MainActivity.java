package com.example.fragmentnotes.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fragmentnotes.R;
import com.example.fragmentnotes.databinding.ActivityMainBinding;
import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.domain.NotesRepo;
import com.example.fragmentnotes.ui.additioanlFragments.AboutFragment;
import com.example.fragmentnotes.ui.additioanlFragments.ProfileFragment;
import com.example.fragmentnotes.ui.additioanlFragments.SettingsFragment;
import com.example.fragmentnotes.ui.dialogs.DialogExitApp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NoteListFragment.ControllerNoteList,
        NoteEditFragment.ControllerNoteEdit {

    private static final String NOTE_LIST_TAG = "NOTE_LIST_TAG";
    private static final String NOTE_EDIT_TAG = "NOTE_EDIT_TAG";
    private static final String ADDITIONAL_FRAGMENT_TAG = "ADDITIONAL_FRAGMENT_TAG";
    private static final String ACTIVE_NOTE_KEY = "ACTIVE_NOTE_KEY";
    private static final String NOTE_LIST_FRAGMENT_KEY = "NOTE_LIST_FRAGMENT_KEY";
    private static final String POSITION_NOTE_KEY = "POSITION_NOTE_KEY";
    private final Map<Integer, Fragment> fragments = createFragments();
    public NotesRepo notesRepo;
    private ActivityMainBinding binding;
    private boolean isLandscape;
    private FragmentManager fragmentManager;
    private NoteListFragment noteListFragment;
    private int positionNote;
    private int listLayout;
    private int noteLayout;
    private NoteEntity activeNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesRepo = (NotesRepo) getApplication();
        fragmentManager = getSupportFragmentManager();

        restoreActivity(savedInstanceState);

        setSupportActionBar(binding.toolbar);
        isLandscape = getResources().getBoolean(R.bool.isLandscape);
        initLayouts();
        initBottomNavigation();
        openNewNoteFragments();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            DialogExitApp dialogFragment = new DialogExitApp();
            dialogFragment.setView(this.findViewById(android.R.id.content));
            dialogFragment.show(getSupportFragmentManager(), null);
        } else {
            activeNote = null;
            super.onBackPressed();
        }
    }

    private void openNewNoteFragments() {
        if (fragmentManager.findFragmentByTag(NOTE_LIST_TAG) == null) {
            openNotesList();
        }
        if (activeNote != null) {
            openNoteItem(activeNote, positionNote, false);
        }
    }

    private void removeFragment(String fragmentTag) {
        NoteFragments noteFragment = (NoteFragments) fragmentManager.findFragmentByTag(fragmentTag);
        if (noteFragment != null) {
            fragmentManager
                    .beginTransaction()
                    .remove((Fragment) noteFragment)
                    .commit();
            if (fragmentTag == NOTE_EDIT_TAG) {
                getSupportFragmentManager().popBackStack();
            }
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
        outState.putParcelable(ACTIVE_NOTE_KEY, activeNote);
        outState.putParcelable(NOTE_LIST_FRAGMENT_KEY, noteListFragment);
        outState.putInt(POSITION_NOTE_KEY, positionNote);
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
            openNoteItem(new NoteEntity(), notesRepo.getNotes().size(), true);
            return true;
        } else if (item.getItemId() == R.id.clear_note_menu) {
            notesRepo.clearAll();
            noteListFragment.setAdapterData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void restoreActivity(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            activeNote = savedInstanceState.getParcelable(ACTIVE_NOTE_KEY);
            positionNote = savedInstanceState.getInt(POSITION_NOTE_KEY);
            noteListFragment = savedInstanceState.getParcelable(NOTE_LIST_FRAGMENT_KEY);
        } else {
            noteListFragment = new NoteListFragment(notesRepo);
        }
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
        binding.bottomNavView.setOnItemSelectedListener(item -> {
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
    public void saveItem(NoteEntity noteEntity, boolean isNew) {
        if (isLandscape) {
            if (isNew) {
                noteListFragment.notifyItemInserted(positionNote);
            } else {
                noteListFragment.notifyItemChanged(positionNote);
            }
        } else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void openNoteItem(@Nullable NoteEntity item, int position, boolean isNew) {
        removeFragment(NOTE_EDIT_TAG);
        positionNote = position;
        fragmentManager
                .beginTransaction()
                .replace(noteLayout, NoteEditFragment.newInstance(item, notesRepo, isNew), NOTE_EDIT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public NotesRepo getRepo() {
        return notesRepo;
    }

    @Override
    public void setActiveNote(NoteEntity activeNote, int position) {
        this.activeNote = activeNote;
        this.positionNote = position;
    }
}