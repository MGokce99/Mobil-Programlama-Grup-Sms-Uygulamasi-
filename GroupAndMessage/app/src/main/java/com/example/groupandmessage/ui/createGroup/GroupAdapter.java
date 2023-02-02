package com.example.groupandmessage.ui.createGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupandmessage.GroupModel;
import com.example.groupandmessage.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    List<GroupModel> groupModelList;
    public GroupAdapter(List<GroupModel> groupModelList){
        this.groupModelList = groupModelList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupViewHolder groupViewHolder = new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_group,parent,false));
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        GroupModel groupModel = groupModelList.get(position);
        holder.setData(groupModel);
    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }
    public class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView groupImage;
        TextView groupName, groupExplanation;
        public GroupViewHolder(View itemView){
            super(itemView);
            groupImage = itemView.findViewById(R.id.item_groupImage);
            groupName = itemView.findViewById(R.id.item_groupName);
            groupExplanation = itemView.findViewById(R.id.item_groupExplain);
        }
        public void setData(GroupModel groupModel){
            groupName.setText(groupModel.getGroupName());
            groupExplanation.setText(groupModel.getGroupExplanation());

            if(groupModel.getGroupImage() != null){
                Picasso.get().load(groupModel.getGroupImage()).into(groupImage);
            }
        }
    }
}
