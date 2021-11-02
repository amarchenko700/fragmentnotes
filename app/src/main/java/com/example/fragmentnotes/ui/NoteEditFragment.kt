package com.example.fragmentnotes.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.fragmentnotes.R
import com.example.fragmentnotes.domain.NoteEntity
import com.example.fragmentnotes.domain.NotesRepo

class NoteEditFragment : Fragment, NoteFragments {
    private lateinit var noteEntity: NoteEntity
    private lateinit var notesRepo: NotesRepo
    private var isNew = false
    private lateinit var titleEditText: EditText
    private lateinit var detailEditText: EditText
    private lateinit var saveButton: Button
    private var controllerNoteEdit: ControllerNoteEdit? = null

    constructor()
    constructor(noteEntity: NoteEntity?, notesRepo: NotesRepo?, isNew: Boolean) {
        this.noteEntity = noteEntity!!
        this.notesRepo = notesRepo!!
        this.isNew = isNew
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        controllerNoteEdit = if (context is ControllerNoteEdit) {
            context
        } else {
            throw IllegalStateException("Activity must implement NoteEditFragment.ControllerNoteEdit")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTextView(view)
        saveButton.setOnClickListener { v: View? -> saveNote() }
    }

    override fun onStart() {
        super.onStart()
        fillNote()
    }

    override fun onDestroy() {
        super.onDestroy()
        controllerNoteEdit = null
    }

    private fun initTextView(view: View) {
        titleEditText = view.findViewById(R.id.title_edit_text)
        detailEditText = view.findViewById(R.id.detail_edit_text)
        saveButton = view.findViewById(R.id.save_button)
    }

    private fun fillNote() {
        titleEditText.setText(noteEntity.title)
        detailEditText.setText(noteEntity.description)
    }

    private fun saveNote() {
        noteEntity.let {
            it.description = detailEditText.text.toString()
            it.title = titleEditText.text.toString()
        }
        if (isNew) {
            notesRepo.createNote(noteEntity)
        } else {
            notesRepo.editNote(noteEntity)
        }
        controllerNoteEdit!!.saveItem(noteEntity, isNew)

    }

    interface ControllerNoteEdit {
        fun saveItem(noteEntity: NoteEntity?, isNew: Boolean)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            noteEntity: NoteEntity?, notesRepo: NotesRepo?,
            isNew: Boolean
        ): NoteEditFragment {
            return NoteEditFragment(noteEntity, notesRepo, isNew)
        }
    }
}