package com.example.fragmentnotes.ui.recycler;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragmentnotes.domain.NoteEntity;
import com.example.fragmentnotes.ui.NoteListFragment;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> implements Parcelable {

    private List<NoteEntity> data = new ArrayList<>();
    private onItemClickListener clickListener = null;
    private onItemContextClickListener contextClickListener = null;

    public NotesAdapter(){

    }

    public NotesAdapter(Parcel in) {
        data = in.createTypedArrayList(NoteEntity.CREATOR);
    }

    public static final Creator<NotesAdapter> CREATOR = new Creator<NotesAdapter>() {
        @Override
        public NotesAdapter createFromParcel(Parcel in) {
            return new NotesAdapter(in);
        }

        @Override
        public NotesAdapter[] newArray(int size) {
            return new NotesAdapter[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
    }

    public interface onItemClickListener {
        void onItemClick(NoteEntity item, int position);
    }

    public interface onItemContextClickListener {
        boolean onItemContextClick(View v, NoteEntity item, int position);
    }
}
