package com.mireia.notetaskapp;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NoteTaskViewModel extends ViewModel {

    private final MutableLiveData<List<NoteTask>> noteTaskList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<NoteTask>> getNoteTaskList() {
        return noteTaskList;
    }

    public void addNoteTask(NoteTask noteTask) {
        List<NoteTask> currentList = noteTaskList.getValue();
        if (currentList != null) {
            currentList.add(noteTask);
            noteTaskList.setValue(currentList);
        }
    }

    public void updateNoteTask(NoteTask oldNoteTask, NoteTask newNoteTask) {
        List<NoteTask> currentList = noteTaskList.getValue();
        if (currentList != null) {
            int index = currentList.indexOf(oldNoteTask);
            if (index != -1) {
                currentList.set(index, newNoteTask);
                noteTaskList.setValue(currentList);
            }
        }
    }

    public void deleteNoteTask(NoteTask noteTask) {
        List<NoteTask> currentList = noteTaskList.getValue();
        if (currentList != null) {
            currentList.remove(noteTask);
            noteTaskList.setValue(currentList);
        }
    }

    public void completeNoteTask(NoteTask noteTask) {
        List<NoteTask> currentList = noteTaskList.getValue();
        if (currentList != null) {
            int index = currentList.indexOf(noteTask);
            if (index != -1) {
                NoteTask completedTask = new NoteTask(
                        noteTask.getSubject(),
                        noteTask.getDescription(),
                        noteTask.getDate(),
                        true
                );
                currentList.set(index, completedTask);
                noteTaskList.setValue(currentList);
            }
        }
    }
}
