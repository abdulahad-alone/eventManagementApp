package com.example.eventapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.Models.ChatroomModel;
import com.example.eventapp.Models.UserModel;
import com.example.eventapp.R;
import com.example.eventapp.chatwin;
import com.example.eventapp.utils.AndroidUtil;
import com.example.eventapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatroomModel,RecentChatRecyclerAdapter.ChatroomModelViewHolder> {
    private Context context;
    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatroomModel> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    public void onBindViewHolder(@NonNull RecentChatRecyclerAdapter.ChatroomModelViewHolder holder, int position,ChatroomModel model) {
        FirebaseUtil.getOtheerUserFromChatroomModel(model.getUserIds()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    boolean lastMessageSendbyMe=model.getLastMessageSenderId().equals(FirebaseUtil.currentuserId());

                    UserModel otherUserModel=task.getResult().toObject(UserModel.class);
                    String otherUserId= otherUserModel.getUserId();

                    FirebaseUtil.getOtherProfilePicStorageRef(otherUserId).getDownloadUrl()
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful())
                                    {
                                        Uri uri=task.getResult();
                                        AndroidUtil.set_ProfilePic(context ,uri, holder.profilePic);
                                    }
                                }
                            });

                    holder.usernameText.setText(otherUserModel.getName());
                    if(lastMessageSendbyMe)
                    {
                        holder.lastMessageText.setText("You: "+model.getLastMessage());
                    }else
                    {
                        holder.lastMessageText.setText(model.getLastMessage());
                    }

                    holder.lastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, chatwin.class);
                            AndroidUtil.passUserModelAsintent(intent, otherUserModel);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                }
            }
        });
    }
    @NonNull
    @Override
    public ChatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.recent_chat_recycler_row,parent,false);
        return new ChatroomModelViewHolder(view);
    }




    class ChatroomModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText,lastMessageText,lastMessageTime;
        ImageView profilePic;


        public ChatroomModelViewHolder(@NonNull View itemView)
        {
            super(itemView);
            usernameText=itemView.findViewById(R.id.user_name_text);
            lastMessageText=itemView.findViewById(R.id.last_message_text);
            lastMessageTime=itemView.findViewById(R.id.last_message_time_text);
            profilePic=itemView.findViewById(R.id.profile_pic_image_view);

        }
    }
}
