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

public class ItemsNoteAdapter extends RecyclerView.Adapter<ItemsNoteAdapter.MyViewHolder> {

    private Context context;

    private List<Item> items;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public TextView dot;
        public TextView username;
        public TextView timestamp;
        public TextView userId;
        public TextView itemId;
        public TextView division;
        public MyViewHolder(View view) {
            super(view);
            itemId = view.findViewById(R.id.noteId);
            division = view.findViewById(R.id.division);
            note = view.findViewById(R.id.note);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);

        }
    }
    public ItemsNoteAdapter(Context context, List<Item> items) {

        this.context = context;
        this.items = items;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //final set text ke view dan loop tiap item

        Item item = items.get(position);

//        Log.i("BindViewHolder","Onhere");

        holder.itemId.setText("Note ID ="+String.valueOf(item.getId()));
        holder.division.setText("Division ="+String.valueOf(item.getDivision()));
        holder.note.setText("Item Name ="+String.valueOf(item.getNote()));
        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));
        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(item.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
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
