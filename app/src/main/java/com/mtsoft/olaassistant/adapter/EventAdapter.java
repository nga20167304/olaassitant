package com.mtsoft.olaassistant.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mtsoft.olaassistant.R;
import com.mtsoft.olaassistant.models.Event;

import java.util.ArrayList;


/**
 * Created by manhhung on 5/6/17.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private final Activity activity;
    private ArrayList<Event> events;
    AlertDialog alert;

    public EventAdapter(Context mContext, ArrayList<Event> eventsArrayList, AlertDialog alert, Activity activity) {
        this.mContext = mContext;
        this.events = eventsArrayList;
        this.alert = alert;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemEvent = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemEvent);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Event event = events.get(position);
        EventViewHolder eventViewHolder = (EventViewHolder) holder;
        eventViewHolder.tvTitle.setText(event.getTitle());
        eventViewHolder.tvTime.setText(event.getTime());
        eventViewHolder.tvLocation.setText(event.getLocation());
//        contactViewHolder.setNumberPhone(contacts.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle,tvTime,  tvLocation;
//        public String numberPhone;

        public EventViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
//            itemView.setOnClickListener(this);
        }

//        public void setNumberPhone(String item) {
//            this.numberPhone = item;
//        }

//        @Override
//        public void onClick(View v) {
//            Log.d("ADAPTER", "onClick " + getPosition() + " " + numberPhone);
//            MainActivity.phoneNumber = numberPhone;
//            Call call = new Call(mContext, activity);
//            call.callNumber(numberPhone);
//            alert.dismiss();
//
//
//        }
    }

}
