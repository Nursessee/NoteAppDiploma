package com.example.clientlist.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.clientlist.R;
import com.example.clientlist.database.Notes;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesAddActivity extends AppCompatActivity {
    EditText editText_title, editText_notes;
    ImageView imageView_save;
    Notes notes;
    boolean isOldNote = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_add);

        imageView_save = findViewById(R.id.imageView_save);
        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);
        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            editText_notes.setText(notes.getNotes());
            editText_title.setText(notes.getTitle());
            isOldNote = true;
        }catch (Exception e){
            e.printStackTrace();
        }


        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                String edNotes = editText_notes.getText().toString();

                if(edNotes.isEmpty()){
                    Toast.makeText(NotesAddActivity.this, "Пожалуйста добавьте замеку!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyy HH:mm a");
                Date date = new Date();
                if(!isOldNote){
                    notes = new Notes();
                }

                notes.setTitle(title);
                notes.setNotes(edNotes);
                notes.setDate(formatter.format(date));

                Intent i = new Intent();
                i.putExtra("note", notes);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
    }
}