package com.example.eventapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventapp.Models.ChatMessageModel;
import com.example.eventapp.Models.ChatroomModel;
import com.example.eventapp.Models.UserModel;
import com.example.eventapp.Models.chatPropertyReference;
import com.example.eventapp.adapter.ChatRecycleAdapter;
import com.example.eventapp.utils.AndroidUtil;
import com.example.eventapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatwin extends AppCompatActivity {
    public static String this_userId,current_userId,propertyName,propertyPrice,UserName,userName,UserImg,PropertyImg,reciverName,SenderUID;
    ImageView property_img;
    CircleImageView user_img;
    String ChatroomId,formattedTime;
    CardView sendbtn;
    EditText textmsg ;
    String phone;
    LinearLayout phonehim;
    TextView price,name,proName;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    String senderRoom,reciverRoom;
    ChatRecycleAdapter adapter;
    UserModel otherUser;
    ChatroomModel chatroomModel;
    chatPropertyReference ChatPropertyReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatwin_activity);

        this_userId=getIntent().getStringExtra("thisId");
        current_userId=getIntent().getStringExtra("currentId");
        textmsg =findViewById(R.id.textmsg);
        price =findViewById(R.id.ad_price);
        name =findViewById(R.id.user_name_text);
        phonehim =findViewById(R.id.phonehim);
        proName=findViewById(R.id.ad_title);
        textmsg =findViewById(R.id.textmsg);
        user_img =findViewById(R.id.lol);
        property_img =findViewById(R.id.house_img_ad);
        recyclerView=findViewById(R.id.msgadpter);
        sendbtn = findViewById(R.id.sendbtnn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout linearLayout=findViewById(R.id.pro_info);
        Date currentTime = new Date();
        Intent intent = getIntent();
        phonehim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Handle the case where the device doesn't have a dialer app or can't perform the action
                    Toast.makeText(getApplicationContext(), "Phone dialer not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        otherUser= AndroidUtil.getUserModelFromIntent(getIntent());
        if(otherUser.getUserId() != null){
            this_userId=otherUser.getUserId();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm:ss a 'UTC'XXX", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getDefault());
        formattedTime = sdf.format(currentTime);
        Log.d(TAG, "other user: "+otherUser.getUserId());
        Log.d(TAG, "other user: "+this_userId);
        Log.d(TAG, "current user: "+ FirebaseUtil.currentuserId());
        ChatroomId= FirebaseUtil.getChatroomId(FirebaseUtil.currentuserId(), this_userId);

        getOrCreateChatRommModel();
        setupChatRecyclerView();
        getotheruserProfileId(this_userId);
        if(intent.hasExtra("yourExtraKey"))
        {

            propertyName = getIntent().getStringExtra("propertyName");
            propertyPrice = getIntent().getStringExtra("price");
            userName = getIntent().getStringExtra("Username");
            phone = getIntent().getStringExtra("Phone");



            UserImg = getIntent().getStringExtra("UserImage");
            PropertyImg = getIntent().getStringExtra("imageUri");
            Log.e(TAG, "lolsss: "+propertyPrice );
            proName.setText(propertyName);
            name.setText(userName);
            price.setText("Starting from "+propertyPrice);
            createProptyRefence(PropertyImg,propertyPrice,propertyName,phone);
            if(UserImg!=null )
            {
                AndroidUtil.set_ProfilePic(this, Uri.parse(UserImg),user_img );
            }

            Glide.with(this).load(Uri.parse(PropertyImg)).into(property_img);
            //createProptyRefence(PropertyImg,propertyPrice,propertyName);
        }else
        {
            getPropertyReference();

        }
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(chatwin.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessageToUser(message);


            }
        });

    }
    private void setupChatRecyclerView() {
        Query query=FirebaseUtil.getChatroomMessageReference(ChatroomId)
                .orderBy("timestamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options=new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class).build();

        adapter=new ChatRecycleAdapter(options, getApplicationContext());
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }

    private void sendMessageToUser(String message) {
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtil.currentuserId());
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(ChatroomId).set(chatroomModel);
        ChatMessageModel chatMessageModel=new ChatMessageModel(message,FirebaseUtil.currentuserId(), Timestamp.now());
        Log.d(TAG, "sendMessageToUser: "+chatMessageModel.getSenderId()+"  "+chatMessageModel.getMessage());
        FirebaseUtil.getChatroomMessageReference(ChatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful())
                        {
                            textmsg.setText("");
                        }
                    }
                });

    }

    private void getOrCreateChatRommModel() {
        FirebaseUtil.getChatroomReference(ChatroomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    chatroomModel=task.getResult().toObject(ChatroomModel.class);
                    if(chatroomModel==null)
                    {
                        chatroomModel=new ChatroomModel(ChatroomId, Arrays.asList(current_userId, this_userId) ,
                                Timestamp.now(), "");
                        FirebaseUtil.getChatroomReference(ChatroomId).set(chatroomModel);

                    }
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


            }
        });

    }

    private  void createProptyRefence(String propertyImg, String propertyPrice, String propertyName,String Phone)
    {
        Log.e(TAG, "createProptyRefence: "+ propertyPrice);
        Log.e(TAG, "roomId: "+ChatroomId );
        chatPropertyReference chatMessageModel=new chatPropertyReference(propertyImg,propertyPrice, propertyName,Phone);

        FirebaseUtil.getChatroomPropertyReference(ChatroomId).set(chatMessageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e(TAG, "onComplete: " );
            }
        });
    }
    private void getPropertyReference() {
        FirebaseUtil.getChatroomPropertyReference(ChatroomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    chatPropertyReference chatMessageModel=task.getResult().toObject(chatPropertyReference.class);
                    phone=chatMessageModel.getPhoneNumber();
                    proName.setText(chatMessageModel.getPropertyPrice());
                    price.setText(chatMessageModel.getPropertyTitle());
                    Glide.with(chatwin.this).load(Uri.parse(chatMessageModel.getPropertyImage())).into(property_img);

                }
            }
        });
    }
    private void getotheruserProfileId(String this_userId) {

        FirebaseUtil.getOtherProfilePicStorageRef(this_userId).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Uri UriFromStorage=task.getResult();
                            Log.e(TAG, "image Uri: "+UriFromStorage );
                            AndroidUtil.setProfilePic(chatwin.this, UriFromStorage, user_img);
                        }
                    }
                });
        final UserModel[] otherUser = {new UserModel()};
        FirebaseUtil. otherUserDetials(this_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // setInProgress(false);
                otherUser[0] =task.getResult().toObject(UserModel.class);
                Log.i(TAG, "onComplete: "+otherUser[0].getName());
                name.setText(otherUser[0].getName());
                otherUser[0].getImage();

            }
        });
    }
}