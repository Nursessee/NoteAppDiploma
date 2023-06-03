package com.example.clientlist.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface notesDAO {
    @Query("SELECT * FROM notes ORDER by id DESC")
    List<Notes> getNotesList();
    @Query("UPDATE notes SET title = :title, notes = :notes WHERE id = :id")
    void update(int id, String title, String notes);
    @Insert(onConflict = REPLACE)
    void InsertNotes(Notes notes);
    @Delete
    void deleteNotes(Notes notes);
    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);
}
