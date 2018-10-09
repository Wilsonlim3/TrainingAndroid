package com.example.wijen.training;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wijen.training.database.BookInfo;
import com.example.wijen.training.database.DatabaseHelper;
import com.example.wijen.training.database.Item;
import com.example.wijen.training.database.User;
import com.example.wijen.training.utils.MyDividerItemDecoration;
import com.example.wijen.training.utils.RecyclerTouchListener;
import com.example.wijen.training.view.ItemsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class bookFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ItemsAdapter mAdapter;
    //private NotesAdapter notesAdapter;
    private List<BookInfo> bookInfosList = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;
    private DatabaseHelper db;
    private OnFragmentInteractionListener mListener;
    private List<String> values;
    private String[] valuesArray;
    private String[] itemValuesArray;
    Button btnStartDate, btnStartTime, btnEndDate, btnEndTime;
    EditText txtStartDate, txtStartTime, txtEndDate, txtEndTime;
    private int mYear, mMonth, mDay, mHour, mMinute;


    public bookFragment() {
        // Required empty public constructor
    }


    public static bookFragment newInstance(String param1, String param2) {
        bookFragment fragment = new bookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        db = new DatabaseHelper(this.getActivity());
        mAdapter = new ItemsAdapter(getActivity(), bookInfosList,items,users);
        List<String> values = db.getNameSpinner();
        valuesArray = values.toArray(new String[0]);
        List<String> itemValues = db.getItemSpinner();
        itemValuesArray = itemValues.toArray(new String[0]);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((Book)getActivity()).setSupportActionBar(toolbar);
        ((Book)getActivity()).setActionBarTitle("Booking Info");


    coordinatorLayout = v.findViewById(R.id.coordinator_layout);
    recyclerView = v.findViewById(R.id.recycler_view);
    noNotesView = v.findViewById(R.id.empty_notes_view);


    bookInfosList.clear();
    items.clear();
    users.clear();
        //((YourActivityClassName)getActivity()).yourPublicMethod();
        bookInfosList.addAll(db.getAllItems());
        items.addAll(db.getNote());
        users.addAll(db.getName());


        //String [] values = db.getName().toArray().toString();
       // String [] values = users.toArray();







    FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
             showNoteDialog(false, null, -1,valuesArray, itemValuesArray);
        }
    });


       toggleEmptyItems();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
        @Override
        public void onClick(View view, final int position) {
        }

        @Override
        public void onLongClick(View view, int position) {
           showActionsDialog(position);
        }
    }));

        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_book, container, false);
        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new MyDividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL, 16));

        recyclerView.setAdapter(mAdapter);



    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // Inflate the menu items for use in the action bar
//
//        inflater.inflate(R.menu.mymenu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            toggleEmptyItems();
            getActivity().finish();
            startActivity(getActivity().getIntent());

        }
    }


    public void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, bookInfosList.get(position), position,valuesArray,itemValuesArray);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }

    public void showNoteDialog(final boolean shouldUpdate, final BookInfo bookInfo, final int position, String[] valuesArray, String[] itemValuesArray) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity().getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.item_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(view);

        final Spinner spinner = (Spinner) view.findViewById(R.id.userSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, valuesArray );
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        final Spinner itemSpinner = (Spinner) view.findViewById(R.id.itemSpinner);
        ArrayAdapter<String> itemSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, itemValuesArray );
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        itemSpinner.setAdapter(itemSpinnerAdapter);

        btnStartDate=view.findViewById(R.id.btn_date);
        btnStartTime=view.findViewById(R.id.btn_time);
        txtStartDate=view.findViewById(R.id.in_date);
        txtStartTime=view.findViewById(R.id.in_time);
        btnEndDate=view.findViewById(R.id.btn_endDate);
        btnEndTime=view.findViewById(R.id.btn_endTime);
        txtEndDate=view.findViewById(R.id.endin_date);
        txtEndTime=view.findViewById(R.id.endin_time);
        btnStartDate.setOnClickListener(this);
        btnStartTime.setOnClickListener(this);
        btnEndDate.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);

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
                    // update note by it's id
                    // updateNote(inputNote.getText().toString(), position);
                } else {
                    // create new note
                    final String itemSpinnerText = itemSpinner.getSelectedItem().toString();
                    final String userSpinnerText = spinner.getSelectedItem().toString();
                    String startDateTemp = txtStartDate.getText().toString();
                    String startTimeTemp = txtStartTime.getText().toString();
                    String endDateTemp = txtEndDate.getText().toString();
                    String endTimeTemp = txtEndTime.getText().toString();
                    int itemId =db.getItemFromNote(itemSpinnerText);
                    int userId =db.getUserFromName(userSpinnerText);

                    final String startTime = startDateTemp + " " + startTimeTemp;
                    final String endTime = endDateTemp + " " + endTimeTemp;
//                    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                    Date d = dft.parse(startTime);
                    Log.i("StartTime",startTime);
                    Log.i("EndTime",endTime);
                    Log.i("SpinerSelected","Item = "+itemSpinnerText+", User= "+userSpinnerText);
                    Log.i("SpinerSelectedID","Item = "+itemId+", User= "+userId);
                    //createBookInfo(Integer.parseInt(inputItemId.getText().toString()),Integer.parseInt(inputUserId.getText().toString()));
                    createBookInfo(itemId,userId,startTime,endTime);
                    alertDialog.dismiss();

                }
            }
        });
    }
    @Override
    public void onClick(View v) {

        if (v == btnStartDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String monthTemp = String.format("%02d",(monthOfYear + 1) );
                            String dayTemp = String.format("%02d", dayOfMonth);
                            txtStartDate.setText(year + "-" + monthTemp + "-" + dayTemp );

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnStartTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(),
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
        if (v == btnEndDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String monthTemp = String.format("%02d",(monthOfYear + 1) );
                            String dayTemp = String.format("%02d", dayOfMonth);
                            txtEndDate.setText(year + "-" + monthTemp + "-" + dayTemp );

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnEndTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(),
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
    }


    public void toggleEmptyItems() {
        // you can check bookInfosList.size() > 0

        if (db.getItemsCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }
    private void deleteNote(int position) {
        // deleting the note from db
        db.deleteNote(bookInfosList.get(position));

        // removing the note from the list
        bookInfosList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyItems();
    }


}
