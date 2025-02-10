package com.mireia.notetaskapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NoteTask implements Parcelable {

    private String subject;
    private String description;
    private String date;
    private boolean isCompleted;

    public NoteTask(String subject, String description, String date, boolean isCompleted) {
        this.subject = subject;
        this.description = description;
        this.date = date;
        this.isCompleted = isCompleted;
    }
//--------------------------------------------------------------
    protected NoteTask(Parcel in) {
        subject = in.readString();
        description = in.readString();
        date = in.readString();
        isCompleted = in.readByte() != 0;
    }

    public static final Creator<NoteTask> CREATOR = new Creator<NoteTask>() {
        @Override
        public NoteTask createFromParcel(Parcel in) {
            return new NoteTask(in);
        }

        @Override
        public NoteTask[] newArray(int size) {
            return new NoteTask[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(subject);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeByte((byte) (isCompleted ? 1 : 0));
    }
//--------------------------------------------------------------
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

}
