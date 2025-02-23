package com.mireia.notetaskapp;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteTaskAdapter extends RecyclerView.Adapter<NoteTaskAdapter.NoteTaskViewHolder> {

    private List<NoteTask> noteTaskList;
    private final OnNoteTaskClickListener listener;

    public NoteTaskAdapter(OnNoteTaskClickListener listener) {
        this.listener = listener;
        this.noteTaskList = new ArrayList<>();
    }

    public void setNoteTaskList(List<NoteTask> noteTaskList) {
        this.noteTaskList = noteTaskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notetask, parent, false);
        return new NoteTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteTaskViewHolder holder, int position) {
        NoteTask noteTask = noteTaskList.get(position);

        holder.subjectTextView.setText(noteTask.getSubject());
        holder.descriptionTextView.setText(noteTask.getDescription());
        holder.dueDateTextView.setText(noteTask.getDate());
        holder.statusTextView.setText(noteTask.isCompleted() ? "Completado" : "Pendiente");

        holder.itemView.setOnClickListener(v -> listener.onNoteTaskClick(noteTask));
    }

    @Override
    public int getItemCount() {
        return noteTaskList.size();
    }

    public static class NoteTaskViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView;
        TextView descriptionTextView;
        TextView dueDateTextView;
        TextView statusTextView;

        public NoteTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dueDateTextView = itemView.findViewById(R.id.dateTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }

    public interface OnNoteTaskClickListener {
        void onNoteTaskClick(NoteTask noteTask);
    }
}
