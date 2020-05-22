package com.example.b10709021_hw2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b10709021_hw2.WaitlistContract;
import com.example.b10709021_hw2.WaitlistDbHelper;

public class MainActivity extends AppCompatActivity {

    private GuestListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView waitlistRecyclerView;
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllGuests();

        mAdapter = new GuestListAdapter(this, cursor);
        waitlistRecyclerView.setAdapter(mAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final long id = (long) viewHolder.itemView.getTag();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete??")
                        .setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeGuest(id);
                                mAdapter.swapCursor(getAllGuests());
                            }
                        })
                        .show();
                mAdapter.swapCursor(getAllGuests());
            }
        }).attachToRecyclerView(waitlistRecyclerView);

    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View v = inflater.inflate(R.layout.jump_scare, null);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add")
                        .setView(v)
                        .setNeutralButton("cancle", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                EditText name = (EditText) (v.findViewById(R.id.editText1));
                                EditText party = (EditText) (v.findViewById(R.id.editText2));
                                addToWaitlist(name.getText().toString(),party.getText().toString());
                            }
                        })
                        .show();
                return true;
            case R.id.action_setting:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void addToWaitlist(String name,String party)
    {
        if(name.length()==0||party.length()==0)
        {
            return;
        }
        int partysize=1;
        try{
            partysize = Integer.parseInt(party);
        }catch (NumberFormatException ex)
        {
            Log.e(LOG_TAG,"Failed to parse party size text to numer"+ex.getMessage());
        }
        addNewGuest(name,partysize);
        mAdapter.swapCursor(getAllGuests());
    }

    private Cursor getAllGuests() {
        return mDb.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }

    private long addNewGuest(String name, int partySize) {
        ContentValues cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, partySize);
        return mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, cv);
    }

    private boolean removeGuest(long id) {
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(WaitlistContract.WaitlistEntry.TABLE_NAME, WaitlistContract.WaitlistEntry._ID + "=" + id, null) > 0;
    }


}