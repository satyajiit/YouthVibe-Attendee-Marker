package com.satyajit.attendance.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "marked")
public class DbModel {

    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String YvNumber, hash;

    @Ignore
    public DbModel(String name, String YvNumber, String hash) {

        this.name = name;
        this.YvNumber = YvNumber;
        this.hash = hash;

    }



    public DbModel(int id, String name, String YvNumber, String hash) {

        this.id = id;
        this.name = name;
        this.YvNumber = YvNumber;
        this.hash = hash;

    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYvNumber() {
        return YvNumber;
    }

    public void setYvNumber(String yvNumber) {
        YvNumber = yvNumber;
    }
}