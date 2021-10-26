package com.example.fragmentnotes.ui.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    private final TextView titleTextView = itemView.findViewById(R.id.title_text_view);
    private final TextView detailTextView = itemView.findViewById(R.id.detail_text_view);
    private NoteEntity note;

    public NoteViewHolder(@Nullable ViewGroup parent, NotesAdapter.onItemClickListener clickListener,
                          NotesAdapter.onItemContextClickListener contextClickListener){
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
        itemView.setOnClickListener(v -> clickListener.onItemClick(note, getAdapterPosition()));
        itemView.setOnLongClickListener(v -> contextClickListener.onItemContextClick(v, note, getAdapterPosition()));
    }

    public void bind(NoteEntity note){
        this.note = note;
        titleTextView.setText(note.getTitle());
        detailTextView.setText(note.getDescription());
    }

}
