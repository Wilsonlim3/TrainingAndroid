package com.example.wijen.training.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wijen.training.R;
import com.example.wijen.training.database.BookInfo;
import com.example.wijen.training.database.Item;
import com.example.wijen.training.database.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Wijen on 24/09/2018.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private Context context;

    private List<User> users;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public TextView dot;
        public TextView username;
        public TextView timestamp;
        public TextView userId;

        public MyViewHolder(View view) {
            super(view);

            userId = view.findViewById(R.id.userId);
            username = view.findViewById(R.id.username);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);

        }
    }
    public UsersAdapter(Context context, List<User> users) {

        this.context = context;
        this.users = users;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //final set text ke view dan loop tiap item

        User user = users.get(position);

//        Log.i("BindViewHolder","Onhere");

        holder.userId.setText("User ID ="+String.valueOf(user.getId()));
        holder.username.setText("Name ="+String.valueOf(user.getName()));

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));
        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(user.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
