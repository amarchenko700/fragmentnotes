package com.example.fragmentnotes.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragmentnotes.R;
import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.impl.NotesRepoImpl;

public class NoteListFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotesAdapter adapter = new NotesAdapter();
    private NoteEntity noteEntity;
    private NotesRepoImpl notesRepo;
    private ControllerNoteList controllerNoteList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ControllerNoteList) {
            controllerNoteList = (ControllerNoteList) context;
        } else {
            throw new IllegalStateException("Activity must implement NoteListFragment.ControllerNoteList");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    @Override
    public void onDestroy() {
        controllerNoteList = null;
        super.onDestroy();
    }

    public void setNotesRepo(NotesRepoImpl notesRepo) {
        this.notesRepo = notesRepo;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_note_menu) {
            NoteEntity newNote = notesRepo.createNote(new NoteEntity());
            onItemClick(notesRepo.getNote(newNote));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onItemClick(NoteEntity item) {
        controllerNoteList.openNoteItem(item);
    }

    private void initRecyclerView() {
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this::onItemClick);
        setRecyclerViewAdapterData();
    }

    public void setRecyclerViewAdapterData() {
        adapter.setData(notesRepo.getNotes());
    }

    public interface ControllerNoteList {
        void openNoteItem(NoteEntity item);
    }

    public static NoteListFragment newInstance(NotesRepoImpl notesRepository) {
        NoteListFragment fragment = new NoteListFragment();
        fragment.setNotesRepo(notesRepository);
        return fragment;
    }

}
