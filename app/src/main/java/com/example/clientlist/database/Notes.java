package com.example.clientlist.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Notes implements Serializable {
    @PrimaryKey(autoGenerate = true) private int id;
    @ColumnInfo (name = "title") private  String title;
    @ColumnInfo (name = "notes") private  String notes;
    @ColumnInfo (name = "date") private  String date;
    @ColumnInfo (name = "pinned") private  boolean pinned;

    public Notes() {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.date = date;
        this.pinned = pinned;
    }
    @Ignore
    public Notes( String title, String notes, String date, boolean pinned) {
        this.title = title;
        this.notes = notes;
        this.date = date;
        this.pinned = pinned;
    }



    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public boolean isPinned() { return pinned; }

    public void setPinned(boolean pinned) { this.pinned = pinned; }
}
