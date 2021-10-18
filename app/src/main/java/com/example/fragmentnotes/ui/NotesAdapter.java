package com.example.fragmentnotes.ui;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragmentnotes.domain.NoteEntity;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<NoteEntity> data = new ArrayList<>();
    private onItemClickListener clickListener = null;
    private onItemContextClickListener contextClickListener = null;

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(parent, clickListener, contextClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private NoteEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<NoteEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        clickListener = listener;
    }

    public void setOnItemContextClickListener(onItemContextClickListener contextListener) {
        contextClickListener = contextListener;
    }

    interface onItemClickListener {
        void onItemClick(NoteEntity item, int position);
    }

    interface onItemContextClickListener {
        boolean onItemContextClick(View v, NoteEntity item, int position);
    }
}
