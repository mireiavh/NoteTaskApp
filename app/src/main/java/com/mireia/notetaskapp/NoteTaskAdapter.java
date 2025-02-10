package com.mireia.notetaskapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteTaskAdapter extends RecyclerView.Adapter<NoteTaskAdapter.NoteTaskViewHolder> {

    private final List<NoteTask> noteTaskList;
    private final OnNoteTaskClickListener listener;

    public NoteTaskAdapter(List<NoteTask> noteTaskList, OnNoteTaskClickListener listener) {
        this.noteTaskList = noteTaskList;
        this.listener = listener;
    }

//--------------------------------------------------------------

    @NonNull
    @Override
    public NoteTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //aqui se infla la vista de cada uno de los elementos de la RecyclerView
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notetask, parent, false);
        //cada vez se tiene que crear una nueva vista para el nuevo elemento agregado
        return new NoteTaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteTaskViewHolder holder, int position) {
        //se le pide la posicion del objeto en la lista
        NoteTask noteTask = noteTaskList.get(position);

        //se situan los datos del objeto en cada uno de los elementos de la lista
        holder.subjectTextView.setText(noteTask.getSubject());
        holder.descriptionTextView.setText(noteTask.getDescription());
        holder.dueDateTextView.setText(noteTask.getDate());
        //en el caso del boolean se pone como string dependiendo de si se completa o no
        holder.statusTextView.setText(noteTask.isCompleted() ? "Completado" : "Pendiente");

        //cada vez que se da un click se agrega el objeto
        holder.itemView.setOnClickListener(v -> listener.onNoteTaskClick(noteTask));
    }

    @Override
    public int getItemCount() {
        //se pide la cantidad de la lista de los objetos
        return noteTaskList.size();
    }

//--------------------------------------------------------------

    public static class NoteTaskViewHolder extends RecyclerView.ViewHolder{
        //se ponen los datos que se requieren de cada elemento
        TextView subjectTextView;
        TextView descriptionTextView;
        TextView dueDateTextView;
        TextView statusTextView;

        public NoteTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            //se asignan los campos de los datos a los elementos de la vista agregada
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dueDateTextView = itemView.findViewById(R.id.dateTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }

    // Interfaz para manejar clics
    public interface OnNoteTaskClickListener {
        void onNoteTaskClick(NoteTask noteTask);
    }

}
