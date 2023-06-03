package com.example.clientlist.notes;

import androidx.cardview.widget.CardView;

import com.example.clientlist.database.Notes;

public interface NotesOnClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
