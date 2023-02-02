package com.example.groupandmessage.addMember;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.groupandmessage.GroupModel;
import com.example.groupandmessage.R;
import com.example.groupandmessage.ui.createGroup.GroupAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class AddMemberFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    RecyclerView recyclerView_addMember_groups, recyclerView_addMember_contacts;
    TextView addMember_groupName;

    GroupModel selectedGroup;
    ArrayList<GroupModel> groupModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        recyclerView_addMember_groups = view.findViewById(R.id.recyclerView_addMember_groups);
        recyclerView_addMember_contacts = view.findViewById(R.id.recyclerView_addMember_contacts);
        addMember_groupName = view.findViewById(R.id.addMember_groupName);

        groupModelList = new ArrayList<>();
       // fetchGroups();
        return view;
    }

}