package com.example.wijen.training;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wijen.training.database.BookInfo;
import com.example.wijen.training.database.DatabaseHelper;
import com.example.wijen.training.database.Item;
import com.example.wijen.training.database.User;
import com.example.wijen.training.view.ItemsAdapter;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
//import com.github.sundeepk.compactcalendarview.CompactCalendarView;

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
    private String[] valuesArray;
    private String[] itemValuesArray;
    static String dateStart;
    static String dateEnd;
    Button btnStartTime,btnEndTime;
    EditText txtStartTime,txtEndTime;
    TextView dataStartTime, dataEndTime;
//    String dateStart,dateEnd;
    private int mYear, mMonth, mDay, mHour, mMinute;

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
        List<String> values = db.getNameSpinner();
        valuesArray = values.toArray(new String[0]);
        List<String> itemValues = db.getItemSpinner();
        itemValuesArray = itemValues.toArray(new String[0]);

//        db.insertbookInfo(5,7, "2018-10-18 12:00:00", "2018-10-19 08:00:00");

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1,valuesArray, itemValuesArray);
            }
        });

        final TextView monthTxt = findViewById(R.id.month);
        final TextView eventTxt = findViewById(R.id.eventTxt);
        final CompactCalendarView compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        monthTxt.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

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

//        List<Event> events = compactCalendarView.getEvents(1433701251000L); // can also take a Date object

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                eventTxt.setText("");
                monthTxt.setText(dateFormatForMonth.format(dateClicked));
                List<Event> events = compactCalendarView.getEvents(dateClicked);

                for(Event event : events){
                    Log.i("Events","="+event.getData());
                    eventTxt.setText(event.getData().toString());
                    Date date = new Date(event.getTimeInMillis());
                    Log.i("Date","=" + event.getTimeInMillis() + "inDate = " + date);

                }
                Log.d("asd", "Day was clicked: " + dateClicked + " with events " + events);


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthTxt.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

    }


    public void createBookInfo(int itemId, int userId, String startTime, String endTime) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertbookInfo(itemId, userId, startTime, endTime);

        // get the newly inserted note from db
        BookInfo n = db.getBookInfo(id);


        if (n != null) {
            // adding new note to array list at 0 position
            bookInfosList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

        }
        finish();
        startActivity(getIntent());
    }



    public void showNoteDialog(final boolean shouldUpdate, final BookInfo bookInfo, final int position, String[] valuesArray, String[] itemValuesArray) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.item_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(view);
        final CompactCalendarView compactCalendarView = view.findViewById(R.id.compactcalendar_viewBook);
        final CompactCalendarView compactCalendarViewEnd = view.findViewById(R.id.compactcalendar_viewBookEnd);
        final TextView monthTxt = view.findViewById(R.id.monthTxt);
        final TextView monthEndTxt = view.findViewById(R.id.monthEndTxt);
        final TextView dataStartTime = view.findViewById(R.id.dataStartTime);
        final SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
        final SimpleDateFormat dateFormatForInsert = new SimpleDateFormat( "yyyy-MM-dd");
        //final String dateStart = "2000-01-01",dateEnd = "2000-01-01";
        monthTxt.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        monthEndTxt.setText(dateFormatForMonth.format(compactCalendarViewEnd.getFirstDayOfCurrentMonth()));

        final Spinner spinner = view.findViewById(R.id.userSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valuesArray );
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        final Spinner itemSpinner = view.findViewById(R.id.itemSpinner);
        ArrayAdapter<String> itemSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemValuesArray );
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        itemSpinner.setAdapter(itemSpinnerAdapter);

        //Calendar Listener
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                dataStartTime.setText("");
                monthTxt.setText(dateFormatForMonth.format(dateClicked));
                List<Event> events = compactCalendarView.getEvents(dateClicked);

                for(Event event : events){
                    Log.i("Events","="+event.getData()+event);
                    dataStartTime.setText(event.getData().toString());
                }

                dateStart = dateFormatForInsert.format(dateClicked);
                Log.i("DateStartCalendar","= "+ dateStart);
                Log.d("CLicked ", "Day was clicked: " + dateClicked + " with events " + events);


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //Log.d("asd", "Month was scrolled to: " + firstDayOfNewMonth);
                monthTxt.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        compactCalendarViewEnd.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                monthTxt.setText(dateFormatForMonth.format(dateClicked));
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                for(Event event : events){
                    Log.i("Events","="+event.getData());

                }
                dateEnd = dateFormatForInsert.format(dateClicked);
                Log.i("DateEndCalendar","= "+ dateEnd);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //Log.d("asd", "Month was scrolled to: " + firstDayOfNewMonth);
                monthEndTxt.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });


////    Calendar
        btnStartTime=view.findViewById(R.id.startTimeBtn);
        txtStartTime=view.findViewById(R.id.txtStartTime);
        btnEndTime = view.findViewById(R.id.endTimeBtn);
        txtEndTime = view.findViewById(R.id.txtEndTime);
        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    // Get Current Time
                    final java.util.Calendar c = java.util.Calendar.getInstance();
                    mHour = c.get(java.util.Calendar.HOUR_OF_DAY);
                    mMinute = c.get(java.util.Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(Calendar.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    String mstartTime = (String.format("%02d:%02d",hourOfDay, minute));
                                    txtStartTime.setText(mstartTime + ":00" );
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();

            }
        });
        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final java.util.Calendar c = java.util.Calendar.getInstance();
                mHour = c.get(java.util.Calendar.HOUR_OF_DAY);
                mMinute = c.get(java.util.Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Calendar.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String mstartTime = (String.format("%02d:%02d",hourOfDay, minute));
                                txtEndTime.setText(mstartTime + ":00" );
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();

            }
        });

        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                compactCalendarView.removeAllEvents();
                compactCalendarViewEnd.removeAllEvents();
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                SimpleDateFormat bookDates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                int pos = 0;
                int posEnd =0;
                for(BookInfo temp : bookInfosList){
                    if(items.get(pos).getNote().equals(selectedItem)) {
                        Log.i("getNote", items.get(pos).getNote());

                        try {
                            String myString = bookInfosList.get(pos).getStartDate();
                            Date bookDatee = bookDates.parse(myString);
                            timeStart = bookDatee.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Event eventBook = new Event(Color.GREEN, timeStart, users.get(pos).getName() + " -- " + items.get(pos).getNote() + " until " + bookInfosList.get(pos).getEndDate());
                        compactCalendarView.addEvent(eventBook);
                    }
                    pos += 1;
                }
                for(BookInfo temp : bookInfosList){
                    if(items.get(posEnd).getNote().equals(selectedItem)) {
                        try {
                            String myString = bookInfosList.get(posEnd).getEndDate();
                            Date bookDatee = bookDates.parse(myString);
                            timeStart = bookDatee.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Event eventBook = new Event(Color.RED, timeStart, users.get(posEnd).getName() + " -- " + items.get(posEnd).getNote() + " from " + bookInfosList.get(posEnd).getStartDate());
                        compactCalendarViewEnd.addEvent(eventBook);
                    }
                    posEnd += 1;
                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


//        btnStartDate=view.findViewById(R.id.btn_date);
//        btnStartTime=view.findViewById(R.id.btn_time);
//        txtStartDate=view.findViewById(R.id.in_date);
//        txtStartTime=view.findViewById(R.id.in_time);
//        btnEndDate=view.findViewById(R.id.btn_endDate);
//        btnEndTime=view.findViewById(R.id.btn_endTime);
//        txtEndDate=view.findViewById(R.id.endin_date);
//        txtEndTime=view.findViewById(R.id.endin_time);
//        btnStartDate.setOnClickListener(this);
//        btnStartTime.setOnClickListener(this);
//        btnEndDate.setOnClickListener(this);
//        btnEndTime.setOnClickListener(this);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? "New Booking" : getString(R.string.lbl_edit_note_title));





        if (shouldUpdate && bookInfo != null) {
//            inputItemId.setText(bookInfo.getItemId());
//            inputUserId.setText(bookInfo.getUserId());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
//                if (TextUtils.isEmpty(inputItemId.getText().toString()) ||TextUtils.isEmpty(inputUserId.getText().toString()) ) {
//                    Toast.makeText(getActivity(), "Enter Item & User ID!", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    alertDialog.dismiss();
//                }

                // check if user updating note
                if (shouldUpdate && bookInfo != null) {

                } else {
                    // create new note
                    final String itemSpinnerText = itemSpinner.getSelectedItem().toString();
                    final String userSpinnerText = spinner.getSelectedItem().toString();

                        Log.i("dateStart", "= " + dateStart);
                        Log.i("dateEnd","=" + dateEnd);
                        String startDateTemp = dateStart;
                        String startTimeTemp = txtStartTime.getText().toString();

                        String endDateTemp = dateEnd;
                        String endTimeTemp = txtEndTime.getText().toString();
                        Log.i("asd",startTimeTemp);
                        Log.i("asd",endTimeTemp);

                    int itemId =db.getItemFromNote(itemSpinnerText);
                    int userId =db.getUserFromName(userSpinnerText);

                    final String startTime = startDateTemp + " " + startTimeTemp;
                    final String endTime = endDateTemp + " " + endTimeTemp;
                    Log.i("startTime",startTime);
                    Log.i("endTime",endTime);
//                    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                    Date d = dft.parse(startTime);


//                    createBookInfo(Integer.parseInt(inputItemId.getText().toString()),Integer.parseInt(inputUserId.getText().toString()));
                    createBookInfo(itemId,userId,startTime,endTime);
                    alertDialog.dismiss();

                }
            }
        });
    }



}
