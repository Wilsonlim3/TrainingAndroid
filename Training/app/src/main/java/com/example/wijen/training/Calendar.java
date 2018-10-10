package com.example.wijen.training;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wijen.training.database.BookInfo;
import com.example.wijen.training.database.DatabaseHelper;
import com.example.wijen.training.database.Item;
import com.example.wijen.training.database.User;
import com.example.wijen.training.view.ItemsAdapter;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.github.sundeepk.compactcalendarview.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calendar extends AppCompatActivity {
    BookInfo bookInfo;
    Item item;
    User user;
    private List<BookInfo> bookInfosList = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private long timeStart;
    private DatabaseHelper db;
    private ItemsAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter calendarAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        db = new DatabaseHelper(this);
        mAdapter = new ItemsAdapter(this, bookInfosList,items,users);
        final SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
        SimpleDateFormat bookDates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        recyclerView = findViewById(R.id.calendarView);
        bookInfosList.addAll(db.getAllItems());
        items.addAll(db.getNote());
        users.addAll(db.getName());



//        CalendarView simpleCalendarView = findViewById(R.id.simpleCalendarView);
//        simpleCalendarView.setFirstDayOfWeek(2);
//        simpleCalendarView.setShowWeekNumber(false);
//        simpleCalendarView.setClickable(true);
//        simpleCalendarView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("onClick","Click");
//
//            }
//        });
//        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
//                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + (month+1) + "/" + year, Toast.LENGTH_SHORT).show();
////                LayoutInflater  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////                View view = (View) inflater.inflate(R.layout.movie_list, null);
//
//            }
//        });
        final TextView monthTxt = findViewById(R.id.month);
        final TextView eventTxt = findViewById(R.id.eventTxt);
        final CompactCalendarView compactCalendarView = findViewById(R.id.compactcalendar_view);
        monthTxt.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
       // compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        int pos = 0;
        for(BookInfo temp : bookInfosList){
            try{
                String myString = bookInfosList.get(pos).getStartDate();
                Date bookDatee = bookDates.parse(myString);
                timeStart = bookDatee.getTime();
            }
            catch (ParseException e){
                e.printStackTrace();
            }

            Event eventBook = new Event(Color.GREEN,timeStart,users.get(pos).getName() + " -- " + items.get(pos).getNote()+ " until " + bookInfosList.get(pos).getEndDate());
            compactCalendarView.addEvent(eventBook);
            pos+=1;
        }



        // Add event 1 on Sun, 12 Okt 2018 12:50:00 GMT
//        Event ev1 = new Event(Color.GREEN, 1539323400000L, "Some extra data that I want to store.");
//        compactCalendarView.addEvent(ev1);
//
//        Event ev2 = new Event(Color.GREEN, 1539450000000L, "Add event manual");
//        compactCalendarView.addEvent(ev2);

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        List<Event> events = compactCalendarView.getEvents(1433701251000L); // can also take a Date object

        // events has size 2 with the 2 events inserted previously
       // Log.d("asd", "Events: " + events);

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                eventTxt.setText("");
                monthTxt.setText(dateFormatForMonth.format(dateClicked));
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                for(Event event : events){
                    Log.i("Events","="+event.getData());
                    eventTxt.setText(event.getData().toString());

                }
                Log.d("asd", "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("asd", "Month was scrolled to: " + firstDayOfNewMonth);
                monthTxt.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

    }
}
