package com.example.wijen.training;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import me.toptas.fancyshowcase.FancyShowCaseView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_one.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_one#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_one extends Fragment {


    private FancyShowCaseView mFancyView, mFancyView2;
    private OnFragmentInteractionListener mListener;

    public fragment_one() {
        // Required empty public constructor
    }
    public String barcode;
    public TextView txtView;

    public static fragment_one newInstance(String param1, String param2) {
        fragment_one fragment = new fragment_one();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_one, container, false);
        Button QRbtn = v.findViewById(R.id.QRbtn);
        Button bookBtn = v.findViewById(R.id.bookBtn);
        Button calendarBook = v.findViewById(R.id.calendarBook);
        txtView = v.findViewById(R.id.txtView);
        QRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        "com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }
        });

        Button newActivity = v.findViewById(R.id.newFragment);
        newActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getContext(), Tutorial.class);
                startActivity(goToNextActivity);
            }
        });
        bookBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getContext(),Book.class);
                startActivity(goToNextActivity);
            }
        }));
        calendarBook.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getContext(),Calendar.class);
                startActivity(goToNextActivity);
            }
        }));

//        new FancyShowCaseView.Builder(this.getActivity())
//                .focusOn(v)
//                .title("Focus on View")
//                //.showOnce("test")
//                .build()
//                .show();

        return v;


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {


                String contents = intent.getStringExtra("SCAN_RESULT"); // This will contain your scan result
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.i("Content",contents);
                //barcode = intent.getStringExtra("barcode");
                txtView.setText(contents);



            }
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
