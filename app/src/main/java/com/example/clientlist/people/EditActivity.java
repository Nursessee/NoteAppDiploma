package com.example.clientlist.people;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientlist.AppExecuter;
import com.example.clientlist.R;
import com.example.clientlist.database.AppDatabase;
import com.example.clientlist.database.Client;
import com.example.clientlist.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditActivity extends AppCompatActivity {
    private EditText edName, edSecName, edTel, edDisc;
    private CheckBox chImp1, chImp2, chImp3, chSpecial;
    private CheckBox[] cbArray = new CheckBox[3];
    private AppDatabase myDb;
    private int importance = 3;
    private int special = 0;
    private boolean isEdited = false;
    private boolean new_user = false;
    private int id;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        init();
        getMyIntent();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImportanceFromCh();
                if (!TextUtils.isEmpty(edName.getText().toString()) && !TextUtils.isEmpty(edSecName.getText().toString())
                        && !TextUtils.isEmpty(edTel.getText().toString()) && !TextUtils.isEmpty(edDisc.getText().toString())) {
                    AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(isEdited){
                                Client client = new Client(edName.getText().toString(),
                                        edSecName.getText().toString(),
                                        edTel.getText().toString(),
                                        importance, edDisc.getText().toString(), special);
                                client.setId(id);
                                myDb.clientDAO().UpdateClient(client);
                                finish();
                            }else {
                                Client client = new Client(edName.getText().toString(),
                                        edSecName.getText().toString(),
                                        edTel.getText().toString(),
                                        importance, edDisc.getText().toString(), special);
                                myDb.clientDAO().InsertClient(client);
                                finish();
                            }

                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (new_user==false)getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.id_edit) {
            setIsEdited(true);
        }else if(id == R.id.id_delete)
        {
            deleteDialog();
        }
        return true;
    }

    private void init() {
        fab = findViewById(R.id.fb);
        myDb = AppDatabase.getInstance(getApplicationContext());
        edName = findViewById(R.id.edName);
        edSecName = findViewById(R.id.edSecName);
        edTel = findViewById(R.id.edTel);
        edDisc = findViewById(R.id.edDisc);

        chImp1 = findViewById(R.id.checkBoxImp1);
        chImp2 = findViewById(R.id.checkBoxImp2);
        chImp3 = findViewById(R.id.checkBoxImp3);
        cbArray[0] = chImp1;
        cbArray[1] = chImp2;
        cbArray[2] = chImp3;
        chSpecial = findViewById(R.id.checkBoxSpecial);
    }

    public void onClickCh1(View view) {
        chImp2.setChecked(false);
        chImp3.setChecked(false);
    }

    private void getMyIntent(){
        Intent i = getIntent();
        if(i != null)
        {
            if(i.getStringExtra(Constants.NAME_KEY)!= null)
            {
                setIsEdited(false);
                edName.setText(i.getStringExtra(Constants.NAME_KEY));
                edSecName.setText(i.getStringExtra(Constants.SEC_NAME_KEY));
                edTel.setText(i.getStringExtra(Constants.TEL_KEY));
                edDisc.setText(i.getStringExtra(Constants.DESC_KEY));
                cbArray[i.getIntExtra(Constants.IMP_KEY, 0)].setChecked(true);
                if(i.getIntExtra(Constants.SP_KEY, 0) == 1) chSpecial.setChecked(true);
                new_user = false;
                id = i.getIntExtra(Constants.ID_KEY, 0);
            }else  new_user = true;
        }

    }
    public void onClickCh2(View view) {
        chImp1.setChecked(false);
        chImp3.setChecked(false);
    }

    public void onClickCh3(View view) {
        chImp1.setChecked(false);
        chImp2.setChecked(false);
    }
    private void deleteDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_message);
        builder.setTitle(R.string.delete);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Client client = new Client(edName.getText().toString(),
                                edSecName.getText().toString(),
                                edTel.getText().toString(),
                                importance, edDisc.getText().toString(), special);
                        client.setId(id);
                        myDb.clientDAO().DeleteClient(client);
                        finish();
                    }
                });

            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    private void setIsEdited(boolean isEdited)
    {
        if(isEdited) {
            fab.show();
        }else
        {
            fab.hide();
        }
        this.isEdited = isEdited;
        chImp1.setClickable(isEdited);
        chImp2.setClickable(isEdited);
        chImp3.setClickable(isEdited);
        chSpecial.setClickable(isEdited);
        edName.setClickable(isEdited);
        edName.setFocusable(isEdited);
        edSecName.setClickable(isEdited);
        edSecName.setFocusable(isEdited);
        edTel.setClickable(isEdited);
        edTel.setFocusable(isEdited);
        edDisc.setClickable(isEdited);
        edDisc.setFocusable(isEdited);
        edName.setFocusableInTouchMode(isEdited);
        edSecName.setFocusableInTouchMode(isEdited);
        edTel.setFocusableInTouchMode(isEdited);
        edDisc.setFocusableInTouchMode(isEdited);

    }

    private void getImportanceFromCh() {
        if (chImp1.isChecked()) {
            importance = 0;
        } else if (chImp2.isChecked()) {
            importance = 1;
        } else if (chImp3.isChecked()) {
            importance = 2;
        }
        if(chSpecial.isChecked())special = 1;
    }
}
