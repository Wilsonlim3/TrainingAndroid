package com.example.wijen.training;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.wijen.training.database.BookInfo;
import com.example.wijen.training.database.DatabaseHelper;
import com.example.wijen.training.database.Item;
import com.example.wijen.training.database.User;
import com.example.wijen.training.utils.MyDividerItemDecoration;
import com.example.wijen.training.utils.RecyclerTouchListener;
import com.example.wijen.training.view.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class Book extends AppCompatActivity {
    private ItemsAdapter mAdapter;
    private List<BookInfo> bookInfosList = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        ViewPager viewPager = (ViewPager) findViewById(R.id.bookPager);
        Book.ViewPagerAdapter adapter = new Book.ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new bookFragment(), "Book Info");
        adapter.addFragment(new itemFragment(), "Item");
        adapter.addFragment(new userFragment(), "User");
        viewPager.setAdapter(adapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.bookTabs);
        tabLayout.setupWithViewPager(viewPager);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

}

    /**
     * Updating note in db and updating
     * item in the list by its position
     */
    private void updateNote(int note, int position) {
        BookInfo n = bookInfosList.get(position);
        // updating note text
        n.setItemId(note);

        // updating note in db
        db.updateItem(n);

        // refreshing the list
        bookInfosList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyItems();
    }


    public void toggleEmptyItems() {
        // you can check bookInfosList.size() > 0

        if (db.getItemsCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
