package com.example.groupandmessage.createMessage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupandmessage.MessageModel;
import com.example.groupandmessage.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    List<MessageModel> messageModelList;

    public MessageAdapter(List<MessageModel>messageModelList){
        this.messageModelList = messageModelList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageViewHolder messageViewHolder = new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_message,parent,false));
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        holder.setData(messageModel);
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageName,message;
        public MessageViewHolder (View itemView){
            super (itemView);
            messageName = itemView.findViewById(R.id.item_messageName);
            message = itemView.findViewById(R.id.item_message);
        }
        public void setData(MessageModel messageModel){
            messageName.setText(messageModel.getMessageName());
            message.setText(messageModel.getMessage());
        }
    }
}
