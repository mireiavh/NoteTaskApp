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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<NoteTask> noteTaskList;
    //creacion del adaptador para la recyclerView
    private NoteTaskAdapter noteTaskAdapter;


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

        //se inician los elementos de la vista y la lista de objetos local
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        noteTaskList = new ArrayList<>();

        //a cada uno de los elementos de la lista se le pueden realizar acciones y debe ser notificado al adaptador
        noteTaskAdapter = new NoteTaskAdapter(noteTaskList, noteTask -> showBottomDialog(noteTask));

        //se situa el adaptador a la recycler creada
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteTaskAdapter);

        //mostrar el dialog inicial donde se agregan los objetos
        //sepone null para que no entre en la opcion de editar objeto seleccionado
        fab.setOnClickListener(v -> showInicialDialog(null));

    }

    private void showInicialDialog(NoteTask noteTaskEdit){

        //el objeto que se esta creando se le debe pasar al dialog para poder rellenarlo
        //se le pone el dialog que se ha creado anteriormente
        NoteTaskDialogFragment dialogFragment = new NoteTaskDialogFragment();
        //si esta habilitada la opcion de editar se le pasa el objeto seleccionado
        if(noteTaskEdit != null){
            Bundle args = new Bundle();
            args.putParcelable("homework", noteTaskEdit);
            dialogFragment.setArguments(args);
        }
        dialogFragment.setOnNoteTaskSavedListener( noteTask -> {
            if(noteTaskEdit == null){
                noteTaskList.add(noteTask);
            } else{
                noteTaskList.set(noteTaskList.indexOf(noteTaskEdit), noteTask);
            }
            //se indica que han habido cambios al adaptador para que revise de nuevo la recycler
            noteTaskAdapter.notifyDataSetChanged();
        });

        dialogFragment.show(getSupportFragmentManager(), "AddHomeworkDialog");

    }

    //se pone un dialog donde se muestran opciones al clickar un elemento de la lista
    private void showBottomDialog(NoteTask noteTask){
        //se crea el dialog adicional y se infla la vista del dialogo
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_options, null);

        //opcion de eliminar dentro del dialog
        v.findViewById(R.id.deleteOption).setOnClickListener(view ->{
            bottomSheetDialog.dismiss();
            showDeleteDialog(noteTask);
        });
        //opcion de edicion
        v.findViewById(R.id.editOption).setOnClickListener(view ->{
            bottomSheetDialog.dismiss();
            showInicialDialog(noteTask);
        });
        //opcion para marcar el boolean de completado (true, false)
        v.findViewById(R.id.completeOption).setOnClickListener(view ->{
            bottomSheetDialog.dismiss();
            noteTask.setCompleted(true);
            noteTaskAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Tarea completada", Toast.LENGTH_SHORT).show();
        });

        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.show();
    }

    private void showDeleteDialog(NoteTask noteTask){
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("Si se eleimina la terea no se podra recuperar.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    noteTaskList.remove(noteTask);
                    noteTaskAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}