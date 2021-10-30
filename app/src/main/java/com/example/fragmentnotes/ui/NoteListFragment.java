package com.example.fragmentnotes.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragmentnotes.R;
import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.impl.NotesRepoImpl;
import com.example.fragmentnotes.ui.recycler.NotesAdapter;

public class NoteListFragment extends Fragment implements NoteFragments {

    private final NotesAdapter adapter = new NotesAdapter();
    private RecyclerView recyclerView;
    private NotesRepoImpl notesRepo;
    private ControllerNoteList controllerNoteList;
    private NoteEntity clickedNote;

    public static NoteListFragment newInstance() {
        NoteListFragment fragment = new NoteListFragment();
        return fragment;
    }

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
        registerForContextMenu(view);

        notesRepo = controllerNoteList.getRepo();
        initRecyclerView();
        setAdapterData();
    }

    @Override
    public void onDestroy() {
        controllerNoteList = null;
        super.onDestroy();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.note_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_note && clickedNote != null) {
            notesRepo.removeNote(clickedNote);
            clickedNote = null;
            /*
             Обязательно ли здесь выставлять адаптеру данные, или есть другой путь?
             Если здесь не выставить данные, то при удалении заметки у меня работает не корректно.
             Думал, что метода адаптера notifyItemRemoved хватит, но увы.
             */
            setAdapterData();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void onItemClick(NoteEntity item, int position) {
        controllerNoteList.setActiveNote(item, position);
        controllerNoteList.openNoteItem(item, position, false);
    }

    private boolean onItemContextClick(View v, NoteEntity item, int position) {
        clickedNote = item;
        v.showContextMenu();
        return true;
    }

    private void initRecyclerView() {
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this::onItemClick);
        adapter.setOnItemContextClickListener(this::onItemContextClick);
    }

    public void setAdapterData() {
        adapter.setData(notesRepo.getNotes());
    }

    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    public void notifyItemInserted(int position) {
        adapter.notifyItemInserted(position);
        setAdapterData();
    }

    public interface ControllerNoteList {
        void openNoteItem(NoteEntity item, int position, boolean isNew);

        NotesRepoImpl getRepo();

        void setActiveNote(NoteEntity activeNote, int position);
    }

}
