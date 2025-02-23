package com.mireia.notetaskapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private NoteTaskAdapter noteTaskAdapter;
    private NoteTaskViewModel noteTaskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        noteTaskViewModel = new ViewModelProvider(this).get(NoteTaskViewModel.class);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        noteTaskAdapter = new NoteTaskAdapter(noteTask -> showBottomDialog(noteTask));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteTaskAdapter);

        // Observar cambios en la lista de tareas
        noteTaskViewModel.getNoteTaskList().observe(this, noteTasks -> {
            noteTaskAdapter.setNoteTaskList(noteTasks);
            noteTaskAdapter.notifyDataSetChanged();
        });

        fab.setOnClickListener(v -> showInicialDialog(null));
    }

    private void showInicialDialog(NoteTask noteTaskEdit) {
        NoteTaskDialogFragment dialogFragment = new NoteTaskDialogFragment();
        if (noteTaskEdit != null) {
            Bundle args = new Bundle();
            args.putParcelable("homework", noteTaskEdit);
            dialogFragment.setArguments(args);
        }
        dialogFragment.setOnNoteTaskSavedListener(noteTask -> {
            if (noteTaskEdit == null) {
                noteTaskViewModel.addNoteTask(noteTask);
            } else {
                noteTaskViewModel.updateNoteTask(noteTaskEdit, noteTask);
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "AddHomeworkDialog");
    }

    private void showBottomDialog(NoteTask noteTask) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_options, null);

        v.findViewById(R.id.deleteOption).setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            showDeleteDialog(noteTask);
        });
        v.findViewById(R.id.editOption).setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            showInicialDialog(noteTask);
        });
        v.findViewById(R.id.completeOption).setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            noteTaskViewModel.completeNoteTask(noteTask);
            Toast.makeText(this, "Tarea completada", Toast.LENGTH_SHORT).show();
        });

        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.show();
    }

    private void showDeleteDialog(NoteTask noteTask) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("Si se elimina la tarea no se podrá recuperar.")
                .setPositiveButton("Eliminar", (dialog, which) -> noteTaskViewModel.deleteNoteTask(noteTask)) // Eliminar tarea
                .setNegativeButton("Cancelar", null)
                .show();
    }
}