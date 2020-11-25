package com.yk.repository.test;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TestEntryDao {

    @Query("SELECT *FROM testentry")
    List<TestEntry> getAll();

    @Query("SELECT * FROM testentry WHERE uid IN (:testUids)")
    List<TestEntry> loadAllByIds(int[] testUids);

    @Insert
    void insertAll(TestEntry... testEntries);

    @Delete
    void delete(TestEntry testEntry);

    @Update
    void update(TestEntry testEntry);
}
