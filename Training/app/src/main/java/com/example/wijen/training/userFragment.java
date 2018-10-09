package com.example.wijen.training;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wijen.training.database.BookInfo;
import com.example.wijen.training.database.DatabaseHelper;
import com.example.wijen.training.database.Item;
import com.example.wijen.training.database.User;
import com.example.wijen.training.utils.MyDividerItemDecoration;
import com.example.wijen.training.utils.RecyclerTouchListener;
import com.example.wijen.training.view.UsersAdapter;
import com.example.wijen.training.view.ItemsNoteAdapter;

import java.util.ArrayList;
import java.util.List;


public class userFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private UsersAdapter mAdapter;
    private List<User> users = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;
    private DatabaseHelper db;

    private OnFragmentInteractionListener mListener;

    public userFragment() {
        // Required empty public constructor
    }

    public static userFragment newInstance(String param1, String param2) {
        userFragment fragment = new userFragment();
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
        mAdapter = new UsersAdapter(getActivity(),users);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user, container, false);
        Log.i("create1","1");
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((Book)getActivity()).setSupportActionBar(toolbar);
        ((Book)getActivity()).setActionBarTitle("User Info");

        coordinatorLayout = v.findViewById(R.id.coordinator_layout);
        recyclerView = v.findViewById(R.id.recycler_view);
        noNotesView = v.findViewById(R.id.empty_notes_view);

        db = new DatabaseHelper(this.getActivity());
        users.clear();
        users.addAll(db.getAllUsers());

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });


        mAdapter = new UsersAdapter(this.getActivity(),users);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


        toggleEmptyItems();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                //((Book)getActivity()).showActionsDialog(position);
            }
        }));
        // Inflate the layout for this fragment
        return v;
    }

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

    public void createUserInfo(String name) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertUser(name);

        // get the newly inserted note from db
        User n = db.getUser(id);


        if (n != null) {
            // adding new note to array list at 0 position
            users.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();
            toggleEmptyItems();
            getActivity().finish();
            startActivity(getActivity().getIntent());

        }
    }

    public void showNoteDialog(final boolean shouldUpdate, final User user, final int position) {
        Log.i("SHOWNOTEDIALOG","");
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity().getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.user_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(view);

        final EditText inputName = view.findViewById(R.id.name);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && user != null) {
            inputName.setText(user.getName());
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
                if (TextUtils.isEmpty(inputName.getText().toString())  ) {
                    Toast.makeText(getActivity(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && user != null) {
                    // update note by it's id
                    // updateNote(inputNote.getText().toString(), position);
                } else {
                    // create new note
                    createUserInfo((inputName.getText().toString()));
                }
            }
        });
    }


    public void toggleEmptyItems() {
        // you can check bookInfosList.size() > 0

        if (db.getUsersCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }
}
