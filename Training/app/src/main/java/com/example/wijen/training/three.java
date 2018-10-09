package com.example.wijen.training;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link three.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link three#newInstance} factory method to
 * create an instance of this fragment.
 */
public class three extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int RESULT_LOAD_IMAGE = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public three() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment three.
     */
    // TODO: Rename and change types and number of parameters
    public static three newInstance(String param1, String param2) {
        three fragment = new three();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_three, container, false);

        Button b = (Button) rootView.findViewById(R.id.button_create);
        //b.setOnClickListener(this);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Test","Clicked Button");
                // Get the directory for the user's public pictures directory.
                final File path =
                        Environment.getExternalStoragePublicDirectory
                                (
                                        //Environment.DIRECTORY_PICTURES
                                        Environment.DIRECTORY_DOWNLOADS
                                );

                // Make sure the path directory exists.
                if(!path.exists())
                {
                    // Make it, if it doesn't exit
                    path.mkdirs();
                }

                final File file = new File(path, "config.txt");

                // Save your stream, don't forget to flush() it before closing it.

                try
                {
                    final EditText inputTxt = (EditText)rootView.findViewById(R.id.writeBox);
                    String txt = inputTxt.getText().toString();
                    Log.i("txt",txt);
                    file.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(file);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(txt);

                    myOutWriter.close();

                    fOut.flush();
                    fOut.close();
                }
                catch (IOException e)
                {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            }
        });

        Button c = (Button) rootView.findViewById(R.id.btn_read);

        final TextView editBox = (TextView)rootView.findViewById(R.id.editBox);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String absolute = path.getAbsolutePath();
                Log.d("path",absolute);
                File file = new File(absolute,"config.txt");
                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }

                    String TText = text.toString();
                    Log.i("TText",TText);
                    editBox.setText(TText);
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    //You'll need to add proper error handling here
                }

            }
        });

        //return rootView;
        return rootView;

    }
//    @Override
//    public void onClick(View rootView) {
//        switch (rootView.getId()) {
//            case R.id.button_create:
//                Log.i("Test","Clicked Button");
//                // Get the directory for the user's public pictures directory.
//                final File path =
//                        Environment.getExternalStoragePublicDirectory
//                                (
//                                        //Environment.DIRECTORY_PICTURES
//                                        Environment.DIRECTORY_DOWNLOADS
//                                );
//
//                // Make sure the path directory exists.
//                if(!path.exists())
//                {
//                    // Make it, if it doesn't exit
//                    path.mkdirs();
//                }
//
//                final File file = new File(path, "config.txt");
//
//                // Save your stream, don't forget to flush() it before closing it.
//
//                try
//                {
//
//                    file.createNewFile();
//                    FileOutputStream fOut = new FileOutputStream(file);
//                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
//                    myOutWriter.append("Isi dari config.txt");
//
//                    myOutWriter.close();
//
//                    fOut.flush();
//                    fOut.close();
//                }
//                catch (IOException e)
//                {
//                    Log.e("Exception", "File write failed: " + e.toString());
//                }
//
//        }
//        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
