package com.bahwell.inoncharge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.ChatUserDetail;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {
    private LinearLayout layout;
    private ImageView sendButton;
    private EditText messageArea;
    private ScrollView scrollView;
    private Firebase reference1, reference2;
    private FirebaseAuth auth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(ChatActivity.this, LoginActivity.class));
            finish();
        }
        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://inon-charge.firebaseio.com/messages/" + ChatUserDetail.username + "-_-" + ChatUserDetail.chatWith);
        reference2 = new Firebase("https://inon-charge.firebaseio.com/messages/" + ChatUserDetail.chatWith + "-_-" + ChatUserDetail.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    SimpleDateFormat frmttgl = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat frmtjam = new SimpleDateFormat("hh:mm:ss");
                    String tgl = frmttgl.format(new Date());
                    String jam = frmtjam.format(new Date());
                    map.put("message", messageText);
                    map.put("user", ChatUserDetail.username);
                    map.put("Tgl",tgl);
                    map.put("jam",jam);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(ChatUserDetail.username)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(ChatUserDetail.chatWith + ":-\n" + message, 2);
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
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            auth.signOut();
            startActivity(new Intent(ChatActivity.this, LoginActivity.class));
            finish();
        }
        ChatUserDetail.username = auth.getCurrentUser().getUid();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
            finish();
        }
//        switch (item.getItemId()) {
//            case R.id.item_menu_1:
//                auth.signOut();
//                startActivity(new Intent(ChatActivity.this, LoginActivity.class));
//                finish();
//        }
        return true;
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        if(type == 1) {
            lp.setMargins(200, 0, 0, 10);
            textView.setLayoutParams(lp);
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        } else{
            lp.setMargins(0, 0, 200, 10);
            textView.setLayoutParams(lp);
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
