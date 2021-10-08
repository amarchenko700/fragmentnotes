package com.example.fragmentnotes.ui;

import android.content.Context;
import android.content.Intent;
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
import com.example.fragmentnotes.domain.NotesRepo;
import com.example.fragmentnotes.impl.NotesRepoImpl;

public class NoteListFragment extends Fragment {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private NotesRepo notesRepo = new NotesRepoImpl();
    private NotesAdapter adapter = new NotesAdapter();
    private NoteEntity noteEntity;
    private ControllerNoteList controllerNoteList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ControllerNoteList){
            controllerNoteList = (ControllerNoteList) context;
        }else {
            throw new IllegalStateException("Activity must implement NoteListFragment.ControllerNoteList");
        }
    }

    public NoteListFragment() {
        fillRepoByTestValuesRepo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initToolbar(view);
        initRecyclerView(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.note_list_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
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

    private void initToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        /*
        Правильно ли я здесь работаю с установкой меню?
         */
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this::onItemClick);
        adapter.setData(notesRepo.getNotes());
    }

    private void onItemClick(NoteEntity item) {
        controllerNoteList.openNoteItem(item);
    }

    public NotesRepo getNotesRepo() {
        return notesRepo;
    }

    private void fillRepoByTestValuesRepo() {
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
    }

    @Override
    public void onDestroy() {
        controllerNoteList = null;
        super.onDestroy();
    }

    public interface ControllerNoteList{
        void openNoteItem(NoteEntity item);
    }
}
