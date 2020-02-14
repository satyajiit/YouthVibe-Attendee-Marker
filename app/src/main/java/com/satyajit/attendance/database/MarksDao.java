package com.satyajit.attendance.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.satyajit.attendance.models.DbModel;

import java.util.List;

@Dao
public interface MarksDao {

    @Query("SELECT * FROM marked ORDER BY ID")
    List<DbModel> loadAllMarks();

    @Insert
    void insertMark(DbModel person);

    @Update
    void updateMark(DbModel person);

    @Query("SELECT COUNT(*) FROM marked")
    int getCount();

    @Delete
    void delete(DbModel person);

    @Query("SELECT * FROM marked WHERE id = :id")
    DbModel loadPersonById(int id);
}
