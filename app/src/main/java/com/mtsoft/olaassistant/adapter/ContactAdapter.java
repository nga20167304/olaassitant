package com.mtsoft.olaassistant.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mtsoft.olaassistant.Call;
import com.mtsoft.olaassistant.activities.MainActivity;
import com.mtsoft.olaassistant.R;
import com.mtsoft.olaassistant.models.Contact;

import java.util.ArrayList;


/**
 * Created by manhhung on 5/6/17.
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private final Activity activity;
    private ArrayList<Contact> contacts;
    AlertDialog alert;

    public ContactAdapter(Context mContext, ArrayList<Contact> contactArrayList, AlertDialog alert, Activity activity) {
        this.mContext = mContext;
        this.contacts = contactArrayList;
        this.alert = alert;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemContact = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemContact);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Contact contact = contacts.get(position);
        ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
        contactViewHolder.tvName.setText(contact.getName());
        contactViewHolder.tvNumber.setText(contact.getNumber());
        contactViewHolder.setNumberPhone(contacts.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvName, tvNumber;
        public String numberPhone;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            itemView.setOnClickListener(this);
        }

        public void setNumberPhone(String item) {
            this.numberPhone = item;
        }

        @Override
        public void onClick(View v) {
            Log.d("ADAPTER", "onClick " + getPosition() + " " + numberPhone);
            MainActivity.phoneNumber = numberPhone;
            Call call = new Call(mContext, activity);
            call.callNumber(numberPhone);
            alert.dismiss();


        }
    }

}
