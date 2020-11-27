package com.plugin.repository.test;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TestEntry {
    @PrimaryKey
    public int uid;
    @ColumnInfo(name = "first_value")
    public String firstValue;
    @ColumnInfo(name = "second_value")
    public String secondValue;


}
