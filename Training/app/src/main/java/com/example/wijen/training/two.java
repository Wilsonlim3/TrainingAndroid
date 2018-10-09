package com.example.wijen.training;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.SyncStatusObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link two.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link two#newInstance} factory method to
 * create an instance of this fragment.
 */
public class two extends Fragment {


    public class Movie {
        private String movieName;
        private String genre;
        private Integer year;
        private Double rating;

        public Movie(String movieName, String genre, Integer year, Double rating) {
            this.movieName = movieName;
            this.genre = genre;
            this.year = year;
            this.rating = rating;
        }

        public String getMovieName() {
            return movieName;
        }

        public String getGenre() {
            return genre;
        }

        public Integer getYear() {
            return year;
        }

        public Double getRating() {
            return rating;
        }
    }

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.CustomViewHolder> {
        private List<Movie> movies;

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView movieName, year, genre, rating;

            public CustomViewHolder(View view) {
                super(view);
                movieName = (TextView) view.findViewById(R.id.movieName);
                genre = (TextView) view.findViewById(R.id.genre);
                year = (TextView) view.findViewById(R.id.year);
                rating = (TextView) view.findViewById(R.id.rating);
            }
        }

        public MoviesAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list, parent, false);

            return new CustomViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Movie movie = movies.get(position);
            holder.movieName.setText(movie.getMovieName());
            holder.genre.setText(movie.getGenre());
            holder.year.setText(String.valueOf(movie.getYear()));
            holder.rating.setText(String.valueOf(movie.getRating()));
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<Movie> movies = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private String m_Text = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public two() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment two.
     */
    // TODO: Rename and change types and number of parameters
    public static two newInstance(String param1, String param2) {
        two fragment = new two();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.mymenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {

            final LayoutInflater factory = LayoutInflater.from(getContext());

//text_entry is an Layout XML file containing two text field to display in alert dialog
            final View textEntryView = factory.inflate(R.layout.dialog, null);

            final EditText input1 = (EditText) textEntryView.findViewById(R.id.etName);
            final EditText input2 = (EditText) textEntryView.findViewById(R.id.etGenre);
            final EditText input3 = (EditText) textEntryView.findViewById(R.id.etRate);
            final EditText input4 = (EditText) textEntryView.findViewById(R.id.etYear);
            final Button submitBtn = (Button) textEntryView.findViewById(R.id.submitBtn);

            input1.setText("", TextView.BufferType.EDITABLE);
            input2.setText("", TextView.BufferType.EDITABLE);
            input3.setText("", TextView.BufferType.EDITABLE);
            input4.setText("", TextView.BufferType.EDITABLE);



            final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setIcon(R.drawable.icon).setTitle("").setView(textEntryView);
            final AlertDialog dialog=alert.show();

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mvName = input1.getText().toString();
                    String mvGenre = input2.getText().toString();
                    double mvRate = Double.parseDouble(input3.getText().toString());
                    int mvYear = Integer.parseInt(input4.getText().toString());

                    movies.add(new Movie(mvName, mvGenre, mvYear, mvRate));
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_two, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycle_view);
//        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        mAdapter = new MoviesAdapter(movies);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        populateMovieDetails();
        mAdapter.notifyDataSetChanged();
        return v;
    }


    private void populateMovieDetails() {
        movies.add(new Movie("The Shawshank Redemption", "Crime, Drama", 1994, 9.2));
        movies.add(new Movie("The Godfather", "Crime, Drama", 1972, 9.2));
        movies.add(new Movie("The Godfather: Part II", "Crime, Drama", 1974, 9.0));
        movies.add(new Movie("The Dark Knight", "Action, Crime, Drama", 2008, 9.0));
        movies.add(new Movie("12 Angry Men", "Crime, Drama", 1974, 8.9));
        movies.add(new Movie("Schindler's List", "Biography, Drama, History", 1993, 8.9));
        movies.add(new Movie("Pulp Fiction", "Crime, Drama", 1994, 8.9));
        movies.add(new Movie("The Lord of the Rings", "Adventure, Drama, Fantasy", 2003, 8.9));
        movies.add(new Movie("The Good, the Bad and the Ugly", "Western", 1967, 8.9));
        movies.add(new Movie("Fight Club", "Drama", 1999, 8.8));
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
