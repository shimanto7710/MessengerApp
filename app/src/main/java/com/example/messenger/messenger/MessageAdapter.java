package com.example.messenger.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.ConversationActions;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.messenger.MessageItem;
import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextDecoding;
import com.example.messenger.DecodeActivity;
import com.example.messenger.MessengerActivity;
import com.example.messenger.R;
//import com.example.messenger.MessageItem;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter implements TextDecodingCallback {
    MessageViewHolder holder = new MessageViewHolder();
    List<MessageItem> messages = new ArrayList<MessageItem>();
    Context context;
    TextDecoding textDecoding;


    public MessageAdapter(Context context ,TextDecoding textDecoding) {
        this.context = context;
        this.textDecoding=textDecoding;
    }

    public void add(MessageItem message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    public void deleteItem(int id) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == id) {
                messages.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    public boolean findMsg(int id) {
        for (int i = 0; i < messages.size(); i++) {
            if (id == messages.get(i).getId()) {
                return true;
            }
        }
        return false;
    }

    public void refreshAdapter() {
//        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        MessageItem message = messages.get(i);

        if (message.isBelongsToCurrentUser()) { // this message was sent by us so let's create a basic chat bubble on the right
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.tvHideText = (TextView) convertView.findViewById(R.id.item_hide_text);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.imageMsg = (ImageView) convertView.findViewById(R.id.my_msg_pic);
            convertView.setTag(holder);
//            holder.messageBody.setText(message.getMessage());
            if (message.getMessage().toLowerCase().equals("none@msg")) {
//            holder.imageMsg.setImageBitmap();
                byte[] decodedString = Base64.decode(message.getImage(), Base64.DEFAULT);
                final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imageMsg.setImageBitmap(decodedByte);
                Log.d("hhh", "image " + message.getImage());


            }

            if (message.getMessage().toLowerCase().equals("none@msg")) {
                holder.messageBody.setText(null);
            }else {
                holder.messageBody.setText(message.getMessage());
            }




        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            convertView = messageInflater.inflate(R.layout.their_message, null);
//            holder.avatar = (View) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.tvHideText = (TextView) convertView.findViewById(R.id.item_hide_text);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.imageMsg = (ImageView) convertView.findViewById(R.id.their_img_pic);
            convertView.setTag(holder);

            holder.name.setText(message.getSender().getName());
//            holder.messageBody.setText(message.getMessage());


            if (message.getMessage().toLowerCase().equals("none@msg")) {
//            holder.imageMsg.setImageBitmap();
                byte[] decodedString = Base64.decode(message.getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imageMsg.setImageBitmap(decodedByte);
                Log.d("hhh", "image " + message.getImage());

            }

            if (message.getMessage().toLowerCase().equals("none@msg")) {
                holder.messageBody.setText(null);
            }else {
                holder.messageBody.setText(message.getMessage());
            }


        }

        return convertView;
    }

    @Override
    public void onStartTextEncoding() {

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {
        holder.tvHideText.setVisibility(View.VISIBLE);
        if (result != null) {
            if (!result.isDecoded())
                holder.tvHideText.setText("No message found");
            else {
                if (!result.isSecretKeyWrong()) {
                    holder.tvHideText.setText("Decoded");
                    holder.tvHideText.setText("" + result.getMessage());
                } else {
                    holder.tvHideText.setText("Wrong secret key");
                }
            }
        } else {
            holder.tvHideText.setText("Select Image First");
        }

    }
}

class MessageViewHolder {
    public View avatar;
    public TextView name,tvHideText;
    public TextView messageBody;
    public ImageView imageMsg;
}