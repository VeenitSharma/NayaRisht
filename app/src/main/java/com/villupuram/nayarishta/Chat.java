package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    private static final String TAG = Chat.class.getSimpleName();



    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    EmojiconEditText emojiconEditText;
    EmojIconActions emojIcon;
    FirebaseDatabase firebaseDatabase;
    RelativeLayout rlRootView;
    private static String URL_FOR_DETAIL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat);

        rlRootView = (RelativeLayout)findViewById(R.id.root_views);
        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        emojiconEditText = (EmojiconEditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

//        firebaseDatabase.getReference();

        ImageView emojibtn = (ImageView)findViewById(R.id.emojibtn);

        emojIcon = new EmojIconActions(this, rlRootView, emojiconEditText, emojibtn);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://nayarishta-app-8d5f4.firebaseio.com/messages/" + UsersDetails.username + "_" + UsersDetails.chatWith);
        reference2 = new Firebase("https://nayarishta-app-8d5f4.firebaseio.com/messages/" + UsersDetails.chatWith + "_" + UsersDetails.username);



        RelativeLayout back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = emojiconEditText.getText().toString();
                emojiconEditText.getText().clear();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UsersDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UsersDetails.username)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(UsersDetails.chatWith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void addMessageBox(String message, int type){
        EmojiconTextView textView = new EmojiconTextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(130, 20, 20, 10);
        textView.setLayoutParams(lp);
        textView.setEmojiconSize(30);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setPadding(10,30,10,30);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            textView.setBackgroundResource(R.drawable.rounded_corner2);
            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpp.setMargins(20, 20, 130, 10);
            textView.setLayoutParams(lpp);
            textView.setEmojiconSize(30);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setPadding(10,30,10,30);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }




}