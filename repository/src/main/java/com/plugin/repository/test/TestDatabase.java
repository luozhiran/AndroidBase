package com.plugin.repository.test;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TestEntry.class}, version = 1)
public abstract class TestDatabase extends RoomDatabase {
    public abstract TestEntryDao testEntryDao();
}
