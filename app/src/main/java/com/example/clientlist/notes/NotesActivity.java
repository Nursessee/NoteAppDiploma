package com.example.clientlist.notes;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.RoomDatabase;

import com.example.clientlist.MainActivity;
import com.example.clientlist.R;
import com.example.clientlist.database.AppDatabase;
import com.example.clientlist.database.Notes;
import com.example.clientlist.settings.MySettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotesActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    List<Notes> notes = new ArrayList<>();
    AppDatabase myDb;
    FloatingActionButton fab_add;
    Notes selectedNote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);
        recyclerView = findViewById(R.id.recycler_home);
        NavigationView nav_view = findViewById(R.id.nav_view);
        hideItems(nav_view);
        fab_add = findViewById(R.id.fab_add);
        myDb = AppDatabase.getInstance(this);
        notes = myDb.notesDAO().getNotesList();
        updateRecycler(notes);
        nav_view.setNavigationItemSelectedListener(this);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this, NotesAddActivity.class);
                startActivityForResult(intent, 101);
            }
        });

    }



    private void hideItems(NavigationView nav_view) {
        Menu menu = nav_view.getMenu();
        menu.findItem(R.id.important).setVisible(false);
        menu.findItem(R.id.not_important).setVisible(false);
        menu.findItem(R.id.normal).setVisible(false);
        menu.findItem(R.id.id_special).setVisible(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101){
            if(resultCode == Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                myDb.notesDAO().InsertNotes(new_notes);
                notes.clear();
                notes.addAll(myDb.notesDAO().getNotesList());
                notesAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode==102){
            if(resultCode==Activity.RESULT_OK){
                Notes new_note = (Notes) data.getSerializableExtra("note");
                myDb.notesDAO().update(new_note.getId(), new_note.getTitle(), new_note.getNotes());
                notes.clear();
                notes.addAll(myDb.notesDAO().getNotesList());
                notesAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesAdapter = new NotesAdapter(NotesActivity.this, notes, notesOnClickListener);
        recyclerView.setAdapter(notesAdapter);
    }

    private final NotesOnClickListener notesOnClickListener = new NotesOnClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent i  = new Intent(NotesActivity.this, NotesAddActivity.class);
            i.putExtra("old_note", notes);
            startActivityForResult(i, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.id_cleint){
            Intent intent = new Intent(NotesActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if(selectedNote.isPinned()){
                    myDb.notesDAO().pin(selectedNote.getId(), false);
                    Toast.makeText(NotesActivity.this, "Откреплено!", Toast.LENGTH_SHORT);
                }
                else {
                    myDb.notesDAO().pin(selectedNote.getId(), true);
                    Toast.makeText(NotesActivity.this, "Закреплено!", Toast.LENGTH_SHORT);
                }
                notes.clear();
                notes.addAll(myDb.notesDAO().getNotesList());
                notesAdapter.notifyDataSetChanged();
                return true;
            case R.id.delete:
                myDb.notesDAO().deleteNotes(selectedNote);
                notes.remove(selectedNote);
                notesAdapter.notifyDataSetChanged();
                Toast.makeText(NotesActivity.this, "Удалено!", Toast.LENGTH_SHORT);
                return true;
            default: return false;
        }
    }
}