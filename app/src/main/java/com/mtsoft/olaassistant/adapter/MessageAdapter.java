package com.mtsoft.olaassistant.adapter;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mtsoft.olaassistant.R;
import com.mtsoft.olaassistant.models.Chat;
import com.mtsoft.olaassistant.utils.CalendarUtils;

import java.util.ArrayList;


/**
 * Created by manhhung on 5/6/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private ArrayList<Chat> chats;


    public MessageAdapter(Context mContext, ArrayList<Chat> chatArrayList) {
        this.mContext = mContext;
        this.chats = chatArrayList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemViewUser = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_user, parent, false);
        switch (viewType) {
            case 0:
                View itemViewOla = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_ola, parent, false);
                return new OlaViewHolder(itemViewOla);
            case 1:
                return new UserViewHolder(itemViewUser);
            default:
                return new UserViewHolder(itemViewUser);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        Chat chat = chats.get(position);

        switch (holder.getItemViewType()) {
            case 0:
                final OlaViewHolder olaViewHolder = (OlaViewHolder) holder;
                olaViewHolder.textMessageName.setText("Ola");
                olaViewHolder.textMessageBody.setText(chat.getContent());
                olaViewHolder.textMessageTime.setVisibility(View.GONE);
                olaViewHolder.textMessageBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        olaViewHolder.textMessageTime.setVisibility(View.VISIBLE);
                    }
                });

                olaViewHolder.textMessageTime.setText(CalendarUtils.getDateTimeCalendar());
                break;

            case 1:
                final UserViewHolder userViewHolder = (UserViewHolder) holder;
                userViewHolder.textMessageBody.setText(chat.getContent());
                userViewHolder.textMessageTime.setVisibility(View.GONE);
                userViewHolder.textMessageBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userViewHolder.textMessageTime.setVisibility(View.VISIBLE);
                    }
                });
                userViewHolder.textMessageTime.setText(CalendarUtils.getDateTimeCalendar());
                break;
        }


    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = chats.get(position);
        if (chat.getPerson().equals("user")) {
            return 1;
        }
        return 0;

    }


    @Override
    public int getItemCount() {
        return chats.size();
    }


    class OlaViewHolder extends RecyclerView.ViewHolder {
        public TextView textMessageName, textMessageBody, textMessageTime;

        public OlaViewHolder(View itemView) {
            super(itemView);
            textMessageName = (TextView) itemView.findViewById(R.id.text_message_name);
            textMessageBody = (TextView) itemView.findViewById(R.id.text_message_body);
            textMessageTime = (TextView) itemView.findViewById(R.id.text_message_time);
        }
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView textMessageBody, textMessageTime;

        public UserViewHolder(View itemView) {
            super(itemView);
            textMessageBody = (TextView) itemView.findViewById(R.id.text_message_body);
            textMessageTime = (TextView) itemView.findViewById(R.id.text_message_time);
        }
    }

}
