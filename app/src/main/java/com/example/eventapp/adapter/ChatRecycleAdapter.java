package com.example.eventapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.Models.ChatMessageModel;
import com.example.eventapp.R;
import com.example.eventapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecycleAdapter extends FirestoreRecyclerAdapter<ChatMessageModel,ChatRecycleAdapter.ChatModelViewHolder> {
    private final Context context;
    public ChatRecycleAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;

    }
    @Override
    public void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position,@NonNull ChatMessageModel model) {
        if(model.getSenderId().equals(FirebaseUtil.currentuserId())){
            holder.leftChatlayout.setVisibility(View.GONE);
            holder.rightchatlayout.setVisibility(View.VISIBLE);
            holder.rightchatText.setText(model.getMessage());
        }else{
            holder.leftChatlayout.setVisibility(View.VISIBLE);
            holder.rightchatlayout.setVisibility(View.GONE);
            holder.leftChatText.setText(model.getMessage());
        }
    }
    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.chat_message_recycle_row,parent,false);
        return new ChatModelViewHolder(view);
    }




    class ChatModelViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftChatlayout,rightchatlayout;
        TextView leftChatText,rightchatText;
        public ChatModelViewHolder(@NonNull View itemView)
        {
            super(itemView);
            leftChatlayout=itemView.findViewById(R.id.left_chat_layout);
            rightchatlayout=itemView.findViewById(R.id.right_chat_layout);
            leftChatText=itemView.findViewById(R.id.left_chat_text);
            rightchatText=itemView.findViewById(R.id.rigth_chat_text);
        }
    }
}
