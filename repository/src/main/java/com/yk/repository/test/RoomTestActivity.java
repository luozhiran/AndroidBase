package com.yk.repository.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.yk.repository.R;

public class RoomTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_test);
        TestDatabase testDatabase = Room.databaseBuilder(getApplicationContext(), TestDatabase.class, "db").build();
    }
}
