package com.example.clientlist;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.SearchView;

import com.example.clientlist.database.AppDatabase;
import com.example.clientlist.database.Client;
import com.example.clientlist.people.DataAdapter;
import com.example.clientlist.database.Notes;
import com.example.clientlist.notes.NotesActivity;
import com.example.clientlist.people.EditActivity;
import com.example.clientlist.settings.SettingsActivity;
import com.example.clientlist.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DataAdapter adapter;
    private AppDatabase myDb;
    private List<Client> listClient;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private DataAdapter.AdapterOnItemClicked adapterOnItemClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        NavigationView nav_view = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        adapterOnItemClicked = new DataAdapter.AdapterOnItemClicked() {
            @Override
            public void onAdapterItemClicked(int position) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(Constants.NAME_KEY, listClient.get(position).getName());
                i.putExtra(Constants.SEC_NAME_KEY, listClient.get(position).getSec_name());
                i.putExtra(Constants.TEL_KEY, listClient.get(position).getNumber());
                i.putExtra(Constants.DESC_KEY, listClient.get(position).getDescription());
                i.putExtra(Constants.IMP_KEY, listClient.get(position).getImportance());
                i.putExtra(Constants.SP_KEY, listClient.get(position).getSpecial());
                i.putExtra(Constants.ID_KEY, listClient.get(position).getId());
                startActivity(i);
            }
        };
        nav_view.setNavigationItemSelectedListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
            @Override
            public void run() {
                listClient = myDb.clientDAO().getClientList();
                AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(adapter != null)
                        {
                            adapter.updateAdapter(listClient);
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_meny, menu);
        SearchManager sManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView sView = (SearchView) menu.findItem(R.id.id_search).getActionView();
        assert sManager != null;
        sView.setSearchableInfo(sManager.getSearchableInfo(this.getComponentName()));
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        listClient = myDb.clientDAO().getClientListName(newText);
                        AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                if(adapter != null)
                                {
                                    adapter.updateAdapter(listClient);
                                }
                            }
                        });
                    }
                });
                return true;
            }
        });
        return true;

    }

    private void init()
    {
        recyclerView  = findViewById(R.id.rView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myDb = AppDatabase.getInstance(getApplicationContext());
        listClient = new ArrayList<>();
        adapter = new DataAdapter(listClient, adapterOnItemClicked, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected( @NotNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.id_cleint)
        {
            AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    listClient = myDb.clientDAO().getClientList();
                    AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(adapter != null)
                            {
                                adapter.updateAdapter(listClient);
                            }
                        }
                    });
                }
            });
        }
        else if(id == R.id.id_settings)
        {
           Intent i = new Intent(MainActivity.this , SettingsActivity.class);
           startActivity(i);
        }
        else if(id == R.id.id_special)
        {
            AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    listClient = myDb.clientDAO().getClientListSpecial();
                    AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(adapter != null)
                            {
                                adapter.updateAdapter(listClient);
                            }
                        }
                    });
                }
            });

        }
        else if(id == R.id.important)
        {
            AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    listClient = myDb.clientDAO().getClientListImportant(0);
                    AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(adapter != null)
                            {
                                adapter.updateAdapter(listClient);
                            }
                        }
                    });
                }
            });

        }
        else if(id == R.id.normal)
        {
            AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    listClient = myDb.clientDAO().getClientListImportant(1);
                    AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(adapter != null)
                            {
                                adapter.updateAdapter(listClient);
                            }
                        }
                    });
                }
            });

        }
        else if(id == R.id.not_important)
        {
            AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    listClient = myDb.clientDAO().getClientListImportant(2);
                    AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(adapter != null)
                            {
                                adapter.updateAdapter(listClient);
                            }
                        }
                    });
                }
            });

        }
        else if(id == R.id.id_notes)
        {
            Intent intent = new Intent(MainActivity.this , NotesActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

}
