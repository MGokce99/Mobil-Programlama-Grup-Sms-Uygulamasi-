package com.example.groupandmessage.createMessage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.groupandmessage.MessageModel;
import com.example.groupandmessage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;


public class CreateMessageFragment extends Fragment {
FirebaseAuth mAuth;
FirebaseFirestore mStore;

EditText messageName, message;
Button createMessageButton;
RecyclerView messagesRecyclerView;

ArrayList<MessageModel> messageModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_message, container, false);

        messageName = view.findViewById(R.id.messageName);
        message = view.findViewById(R.id.message);
        createMessageButton = view.findViewById(R.id.btn_createMessage);
        messagesRecyclerView = view.findViewById(R.id.recyclerView_messages);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        messageModelList = new ArrayList<>();

        createMessageButton.setOnClickListener(v ->{
            String messageNameText = messageName.getText().toString();
            String messageText = message.getText().toString();

            if (messageNameText.isEmpty()){
                Toast.makeText(getContext(),"Mesaj Adı boş bırakılamaz",Toast.LENGTH_SHORT).show();
                return;
            }
            if(messageText.isEmpty()){
                Toast.makeText(getContext(),"Mesaj boş bırakılamaz",Toast.LENGTH_SHORT).show();
                return;
            }
            createMessage(messageNameText,messageText);
        });
        fetchMessage();
        return view;
    }
    private void createMessage(String messageNameText, String messageText){
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/users/"+userId+"/messages").add(new HashMap<String,String>(){{
            put("messageName", messageNameText);
            put("message", messageText);
        }})
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(),"Mesaj başarıyla oluşturuldu",Toast.LENGTH_SHORT).show();

                    documentReference.get().addOnSuccessListener(documentSnapshot -> {
                        MessageModel messageModel = new MessageModel(messageNameText,messageText,documentSnapshot.getId());
                        messageModelList.add(messageModel);
                        messagesRecyclerView.getAdapter().notifyItemInserted(messageModelList.size()-1);
                    });
                })
                .addOnFailureListener(e ->{
                    Toast.makeText(getContext(),"Mesaj oluşturulamadı",Toast.LENGTH_SHORT).show();
                });
    }
    private void fetchMessage(){
        String userId = mAuth.getCurrentUser().getUid();
        mStore.collection("/users/" + userId + "/messages").get()
        .addOnSuccessListener(queryDocumentSnapshot ->{
            messageModelList.clear();
            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshot.getDocuments()){
                MessageModel messageModel = new MessageModel(documentSnapshot.getString("messageName"),documentSnapshot.getString("message"),documentSnapshot.getId());
                messageModelList.add(messageModel);
            }
            messagesRecyclerView.setAdapter(new MessageAdapter(messageModelList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            messagesRecyclerView.setLayoutManager(linearLayoutManager);
        });
    }
}