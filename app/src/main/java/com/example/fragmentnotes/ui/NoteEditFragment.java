package com.example.fragmentnotes.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fragmentnotes.R;
import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.domain.NotesRepo;

public class NoteEditFragment extends Fragment {

    private NoteEntity noteEntity;
    private NotesRepo notesRepo;

    private EditText titleEditText;
    private EditText detailEditText;
    private Button saveButton;

    private ControllerNoteEdit controllerNoteEdit;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ControllerNoteEdit){
            controllerNoteEdit = (ControllerNoteEdit) context;
        }else {
            throw new IllegalStateException("Activity must implement NoteEditFragment.ControllerNoteEdit");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTextView(view);
        /* Если заполнять здесь, то при первом открытии заметки будет ок, а при открытии
        всех последующих на экране будет отображаться самая первая открытая заметка. Почему?
        Приходится метод fillNote() переносить в onStart() или в onResume() - только так работает.
        fillNote();
        */
        saveButton.setOnClickListener(v -> saveNote());
    }

    @Override
    public void onStart() {
        super.onStart();
        fillNote();
    }

    private void initTextView(View view) {
        titleEditText = view.findViewById(R.id.title_edit_text);
        detailEditText = view.findViewById(R.id.detail_edit_text);
        saveButton = view.findViewById(R.id.save_button);
    }

    private void fillNote() {
        titleEditText.setText(noteEntity.getTitle());
        detailEditText.setText(noteEntity.getDescription());
    }

    private void saveNote() {
        noteEntity.setDescription(detailEditText.getText().toString());
        noteEntity.setTitle(titleEditText.getText().toString());
        notesRepo.editNote(noteEntity.getId(), noteEntity);
        controllerNoteEdit.openNotesList();
    }

    public void setNotesRepo(NotesRepo notesRepo) {
        this.notesRepo = notesRepo;
    }

    public void setNoteEntity(NoteEntity noteEntity) {
        this.noteEntity = noteEntity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        controllerNoteEdit = null;
    }

    public interface ControllerNoteEdit {
        void openNotesList();
    }
}
