package com.example.myevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_KEY = "shared_preferences";
    private static final String EVENT_LIST_JSON = "event_list";
    RecyclerView recyclerview;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Event> events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        events = new ArrayList<>();
        loadEvents();
        //clearEvents();

        recyclerview = (RecyclerView)findViewById(R.id.EventList);
        recyclerview.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        recyclerview.addItemDecoration(decoration);

        adapter = new EventListAdapter(events);
        recyclerview.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void addEvent(View view){
        TextInputEditText dateEditText = (TextInputEditText)findViewById(R.id.date_input_editText);
        TextInputEditText nameEditText = (TextInputEditText)findViewById(R.id.name_input_editText);
        String date = dateEditText.getText().toString();
        String name = nameEditText.getText().toString();
        events.add(new Event(date, name));
        dateEditText.setText("");
        nameEditText.setText("");
        adapter.notifyDataSetChanged();
        saveEvents();
    }

    private void clearEvents(){
        events.clear();
        saveEvents();
        adapter.notifyDataSetChanged();
    }

    private void loadEvents() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String eventListJson = sharedPreferences.getString(EVENT_LIST_JSON, null);
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        Gson gson = new Gson();
        events = gson.fromJson(eventListJson, type);
        if(events == null){
            events = new ArrayList<>();
        }
    }

    public void saveEvents(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String eventListJson = gson.toJson(events);
        editor.putString(EVENT_LIST_JSON, eventListJson);
        editor.apply();
    }

    public void getEvents(){
        for(int i = 0; i < Math.min(EventDB.dates.length, EventDB.descriptions.length); i++){
            events.add(new Event(EventDB.dates[i], EventDB.descriptions[i]));
        }
    }
}