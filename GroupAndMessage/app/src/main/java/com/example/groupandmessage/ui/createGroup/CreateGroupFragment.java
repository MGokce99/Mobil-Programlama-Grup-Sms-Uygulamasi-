package com.example.groupandmessage.ui.createGroup;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.groupandmessage.GroupModel;
import com.example.groupandmessage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class CreateGroupFragment extends Fragment {
     EditText groupName, groupExplanation;
     RecyclerView groupRecyclerView;
     Button createGroupButton;
     ImageView groupImage;

     FirebaseAuth mAuth;
     FirebaseFirestore mStore;
     FirebaseStorage mStorage;

     Uri filePath;

     ArrayList<GroupModel> groupModelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        groupName = view.findViewById(R.id.groupName);
        groupExplanation = view.findViewById((R.id.groupExplanation));
        groupRecyclerView= view.findViewById((R.id.recyclerView_groups));
        createGroupButton = view.findViewById(R.id.btn_createGroup);
        groupImage = view.findViewById(R.id.groupImage);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();

        groupModelArrayList = new ArrayList<>();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == RESULT_OK){
            filePath = result.getData().getData();
            groupImage.setImageURI(filePath);
        }
        });
        groupImage.setOnClickListener(v ->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });

        createGroupButton.setOnClickListener(v -> {
            String name = groupName.getText().toString();
            String explanation = groupExplanation.getText().toString();
            if(name.isEmpty()){
                Toast.makeText(getContext(),"Grup adı boş bırakılamaz", Toast.LENGTH_SHORT).show();
                return;
            }
            if(explanation.isEmpty()){
                Toast.makeText(getContext(),"Grup açıklaması boş bırakılamaz", Toast.LENGTH_SHORT).show();
                return;
            }
            if(filePath != null){
                StorageReference storageReference = mStorage.getReference().child("images" + UUID.randomUUID().toString());
                storageReference.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri ->{
                        String imageUrl = uri.toString();
                        Toast.makeText(getContext(),"Resim Yüklendi",Toast.LENGTH_SHORT).show();
                        createGroup(name,explanation,imageUrl);
                    });
                });
            }else{
                createGroup(name,explanation,null);
            }
        });
        FetchGroup();
        return view;

    }
    private void createGroup(String name, String explanation, String imageUrl){
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/users/" + userId + "/groups").add(new HashMap<String, Object>(){
            {
                put("grupAdı", name);
                put("grupAçıklaması",explanation);
                put("grupResmi", imageUrl);
                put("numaralar", new ArrayList<String>());
            }
        }).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(),"Grup oluşturuldu",Toast.LENGTH_SHORT).show();
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                GroupModel groupModel = new GroupModel(name, explanation, imageUrl,(List<String>) documentSnapshot.get("numaralar"),documentSnapshot.getId());
                groupModelArrayList.add(groupModel);
                groupRecyclerView.getAdapter().notifyItemInserted(groupModelArrayList.size()-1);
            });
        }).addOnFailureListener(e ->{
            Toast.makeText(getContext(),"Grup oluşturulamadı",Toast.LENGTH_SHORT).show();
        });
    }
    private void FetchGroup(){
        String userId = mAuth.getCurrentUser().getUid();
        mStore.collection("/users/"+userId + "/groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelArrayList.clear();
            for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                GroupModel groupModel = new GroupModel(documentSnapshot.getString("grupAdı"),documentSnapshot.getString("grupAçıklaması"),
                        documentSnapshot.getString("grupResmi"),(List<String>) documentSnapshot.get("numaralar"),documentSnapshot.getId());
                groupModelArrayList.add(groupModel);
            }
            groupRecyclerView.setAdapter(new GroupAdapter(groupModelArrayList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            groupRecyclerView.setLayoutManager(linearLayoutManager);
        });
    }
}