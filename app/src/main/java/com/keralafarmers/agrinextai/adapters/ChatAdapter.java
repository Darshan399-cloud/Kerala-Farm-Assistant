package com.keralafarmers.agrinextai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.models.Chat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying chat messages in RecyclerView
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 2;
    
    private List<Chat> chatList;
    private Context context;
    private SimpleDateFormat dateFormat;
    
    public ChatAdapter(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
        this.dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }
    
    @Override
    public int getItemViewType(int position) {
        return chatList.get(position).isFromUser() ? VIEW_TYPE_USER : VIEW_TYPE_BOT;
    }
    
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = (viewType == VIEW_TYPE_USER) ? 
            R.layout.item_chat_user : R.layout.item_chat_bot;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ChatViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        
        holder.tvMessage.setText(chat.getMessage());
        
        // Format timestamp
        String time = dateFormat.format(new Date(chat.getTimestamp()));
        holder.tvTime.setText(time);
        
        // Handle typing indicator
        if (chat.isTyping()) {
            holder.tvMessage.setText("Typing...");
            // You can add animation here for typing indicator
        }
    }
    
    @Override
    public int getItemCount() {
        return chatList.size();
    }
    
    /**
     * ViewHolder class for chat messages
     */
    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}