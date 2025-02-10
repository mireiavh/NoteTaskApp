package com.mireia.notetaskapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class NoteTaskDialogFragment extends DialogFragment {

    //se ponen todas los elementos de la vista del fragmento
    private EditText descriptionEditText;
    private EditText dateEditText;
    private Spinner subjectSpinner;
    //se indican los elementos de la vista en los datos de aqui
    private NoteTaskDialogFragment.OnNoteTaskSavedListener listener;
    private NoteTask noteTask;

    public interface OnNoteTaskSavedListener {
        void onNoteTaskSaved(NoteTask noteTask);
    }

    public void setOnNoteTaskSavedListener(NoteTaskDialogFragment.OnNoteTaskSavedListener listener) {
        this.listener = listener;
    }

    //pillar el indice del spinner
    private int getIndex(Spinner subjectSpinner, String subject) {
        for (int i = 0; i < subjectSpinner.getCount(); i++) {
            if (subjectSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(subject)) {
                return i;
            }
        }
        return 0;
    }

    // definir en el oncreate() que devuelve el dialogo
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //crear el dialog con el builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //inflar la vista del dialog y tomar los elementos y situalos a los datos de aqui
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.fragment_note_task_dialog, null);

        descriptionEditText = v.findViewById(R.id.descriptionEditText);
        dateEditText = v.findViewById(R.id.dateEditText);
        subjectSpinner = v.findViewById(R.id.subjectSpinner);

        //se le indica que debe dituar el dialog de calendario para poder determinar los datos
        dateEditText.setOnClickListener(view -> showCalendarDialog());

        //aqui se puede situar la opcion de edicion, si la tarea esta para editar se situan los campos que se tomen del objeto en la lista
        if (getArguments() != null) {
            noteTask = getArguments().getParcelable("homework");
            if (noteTask != null) {
                descriptionEditText.setText(noteTask.getDescription());
                dateEditText.setText(noteTask.getDate());
                //aqui se le indica al spinner que debe poner la asignatura que le indica el objeto
                subjectSpinner.setSelection(getIndex(subjectSpinner, noteTask.getSubject()));
            }
        }

        Button saveButton = v.findViewById(R.id.saveButton);
        Button cancelButton = v.findViewById(R.id.cancelButton);

        //se guarda el objeto en la lista si pasa la prueba de validacion de los datos: que no esten vacios
        saveButton.setOnClickListener(view ->{
            if(dataConfirm()){
                NoteTask newNoteTask = new NoteTask(subjectSpinner.getSelectedItem().toString(), descriptionEditText.getText().toString(), dateEditText.getText().toString(), false);
                if(listener != null){
                    listener.onNoteTaskSaved(newNoteTask);
                }
                dismiss();
            }
        });
        //si se cancela pues no se guardan los datos y el dialog se cierra
        cancelButton.setOnClickListener(view -> dismiss());

        builder.setView(v);

        return builder.create();
    }

    //crear un dialog para obtener el menu del calendario
    public void showCalendarDialog(){
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
            getContext(),
                (DatePicker view, int year, int month, int day) ->{
                    String date = day + "/" + (month+1) + "/" + year;
                    dateEditText.setText(date);
                },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    //validar los elementos de entrada para agregar al nuevo objeto guardado
    private boolean dataConfirm() {
        if (TextUtils.isEmpty(descriptionEditText.getText())) {
            descriptionEditText.setError("La descripción es obligatoria");
            return false;
        }
        if (TextUtils.isEmpty(dateEditText.getText())) {
            dateEditText.setError("La fecha de entrega es obligatoria");
            return false;
        }
        return true;
    }
}
