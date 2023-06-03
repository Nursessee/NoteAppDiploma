package com.example.clientlist.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface clientDAO {
    @Query("Select * from client_list")
    List<Client> getClientList();
    @Query("Select * from client_list where special is 1")
    List<Client> getClientListSpecial();
    @Query("Select * from client_list where importance is :importance")
    List<Client> getClientListImportant(int importance);
    @Query("Select * from client_list where name Like '%' || :name ||  '%' or sec_name Like '%' || :name ||  '%' or number Like '%' || :name ||  '%'")
    List<Client> getClientListName(String name);
    @Insert void InsertClient(Client client);
    @Update void UpdateClient(Client client);
    @Delete void DeleteClient(Client client);
}
