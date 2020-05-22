package com.example.b10709021_hw2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.b10709021_hw2.WaitlistContract;


public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestViewHolder> implements SharedPreferences.OnSharedPreferenceChangeListener{

    private Cursor mCursor;
    private Context mContext;
    private TextView nameTextView;
    private TextView partySizeTextView;
    private Resources r ;
    public SharedPreferences sharedPreferences;
    public GuestListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        r = context.getResources();
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guest_list_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;
        String name = mCursor.getString(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME));
        int partySize = mCursor.getInt(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE));
        long id = mCursor.getLong(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry._ID));
        nameTextView = holder.nameTextView;
        partySizeTextView = holder.partySizeTextView;
        nameTextView.setText(name);

        GradientDrawable background = (GradientDrawable) partySizeTextView.getBackground();
        background.setColor(Color.parseColor(sharedPreferences.getString(r.getString(R.string.pref_label_key),r.getString(R.string.pref_dark_value))));

        partySizeTextView.setText(String.valueOf(partySize));
        holder.itemView.setTag(id);
    }
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        GradientDrawable background = (GradientDrawable) partySizeTextView.getBackground();
        background.setColor(Color.parseColor(sharedPreferences.getString(r.getString(R.string.pref_label_key),r.getString(R.string.pref_dark_value))));
    }

    class GuestViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView partySizeTextView;
        public GuestViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
        }

    }
}