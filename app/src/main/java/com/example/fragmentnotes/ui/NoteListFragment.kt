package com.example.fragmentnotes.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragmentnotes.R
import com.example.fragmentnotes.databinding.FragmentNoteListBinding
import com.example.fragmentnotes.domain.NoteEntity
import com.example.fragmentnotes.domain.NotesRepo
import com.example.fragmentnotes.ui.recycler.NotesAdapter

class NoteListFragment : Fragment, NoteFragments, Parcelable {
    private lateinit var adapter: NotesAdapter
    private var binding: FragmentNoteListBinding? = null
    private lateinit var notesRepo: NotesRepo
    private var controllerNoteList: ControllerNoteList? = null
    private var clickedNote: NoteEntity? = null

    constructor()
    constructor(notesRepo: NotesRepo) {
        this.notesRepo = notesRepo
    }

    protected constructor(`in`: Parcel) {
        clickedNote = `in`.readParcelable(NoteEntity::class.java.classLoader)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        controllerNoteList = if (context is ControllerNoteList) {
            context
        } else {
            throw IllegalStateException("Activity must implement NoteListFragment.ControllerNoteList")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerForContextMenu(view)
        notesRepo = controllerNoteList!!.repo!!
        adapter = if (savedInstanceState == null) {
            NotesAdapter()
        } else {
            savedInstanceState.getParcelable(ADAPTER_KEY)
        }!!
        initRecyclerView()
        setAdapterData()
    }

    override fun onDestroy() {
        controllerNoteList = null
        super.onDestroy()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.note_list_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_note && clickedNote != null) {
            notesRepo.removeNote(clickedNote)
            clickedNote = null
            /*
             Обязательно ли здесь выставлять адаптеру данные, или есть другой путь?
             Если здесь не выставить данные, то при удалении заметки у меня работает не корректно.
             Думал, что метода адаптера notifyItemRemoved хватит, но увы.
             */setAdapterData()
            return true
        }
        return super.onContextItemSelected(item)
    }

    private fun onItemClick(item: NoteEntity, position: Int) {
        controllerNoteList!!.setActiveNote(item, position)
        controllerNoteList!!.openNoteItem(item, position, false)
    }

    private fun onItemContextClick(v: View, item: NoteEntity, position: Int): Boolean {
        clickedNote = item
        v.showContextMenu()
        return true
    }

    private fun initRecyclerView() {
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)
        binding!!.recyclerView.adapter = adapter
        adapter.setOnItemClickListener { item: NoteEntity, position: Int ->
            onItemClick(
                item,
                position
            )
        }
        adapter.setOnItemContextClickListener { v: View, item: NoteEntity, position: Int ->
            onItemContextClick(
                v,
                item,
                position
            )
        }
    }

    fun setAdapterData() {
        adapter.setData(notesRepo.notes)
    }

    fun notifyItemChanged(position: Int) {
        adapter.notifyItemChanged(position)
        setAdapterData()
    }

    fun notifyItemInserted(position: Int) {
        adapter.notifyItemInserted(position)
        setAdapterData()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(clickedNote, flags)
    }

    interface ControllerNoteList {
        fun openNoteItem(item: NoteEntity?, position: Int, isNew: Boolean)
        val repo: NotesRepo?
        fun setActiveNote(activeNote: NoteEntity?, position: Int)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(ADAPTER_KEY, adapter)
    }

    companion object CREATOR : Creator<NoteListFragment> {
        private const val ADAPTER_KEY = "ADAPTER_KEY"
        override fun createFromParcel(parcel: Parcel): NoteListFragment {
            return NoteListFragment(parcel)
        }

        override fun newArray(size: Int): Array<NoteListFragment?> {
            return arrayOfNulls(size)
        }
    }
}