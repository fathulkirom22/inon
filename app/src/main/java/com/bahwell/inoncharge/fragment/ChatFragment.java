package com.bahwell.inoncharge.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.activity.ChatActivity;
import com.bahwell.inoncharge.other.ChatUserDetail;
import com.bahwell.inoncharge.other.DataListGrid;
import com.bahwell.inoncharge.other.GridViewAdapterBrowse;
import com.bahwell.inoncharge.other.ListViewAdapterChat;
import com.bahwell.inoncharge.other.SessionManagement;
import com.bahwell.inoncharge.other.User;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



public class ChatFragment extends Fragment implements View.OnClickListener {

    private List<DataListGrid> listDataUser;
    private ListView usersList;
    private FirebaseAuth auth;
    private TextView noUsersText;
    private ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> kursor = new ArrayList<String>();
    int totalUsers = 0;
    private ProgressDialog pd;

    SessionManagement session;
    HashMap<String, String> user_my;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View lt = inflater.inflate(R.layout.fragment_chat, container, false);
        usersList = (ListView)lt.findViewById(R.id.usersList);
        noUsersText = (TextView)lt.findViewById(R.id.noUsersText);

        //  pd = new ProgressDialog(getContext());
        //  pd.setMessage("Loading...");
        //  pd.setCancelable(false);
        //  pd.setCanceledOnTouchOutside(false);
        //  pd.show();

        // Session Manager
        session = new SessionManagement(getContext());

        user_my = session.getUserDetails();

        ChatUserDetail.username = user_my.get(SessionManagement.KEY_ID);

        String url = "https://inon-charge.firebaseio.com/transaksi/user/"
                +user_my.get(SessionManagement.KEY_ID)
                +".json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String a = kursor.get(position);
                ChatUserDetail.chatWith = a;
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });
        return lt;
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key = "";
            while(i.hasNext()){
                key = i.next().toString();
                if(!key.equals(ChatUserDetail.username)) {
                    kursor.add(key);

                }totalUsers++;
            }

            if(totalUsers <1){
                noUsersText.setVisibility(View.VISIBLE);
                usersList.setVisibility(View.GONE);
            }else{
                noUsersText.setVisibility(View.GONE);
                usersList.setVisibility(View.VISIBLE);
                //https://stackoverflow.com/questions/40891268/how-to-get-firebase-data-into-a-listview
                Log.d("isis",kursor.toString());
                getProductList();
                setAdapters();
                new LongOperation(this.getContext()).execute();
            }
            //pd.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<DataListGrid> getProductList() {
        listDataUser = new ArrayList<>();
        ArrayList<String> urls = new ArrayList<>();
        for (String id: kursor) {
            listDataUser.add(
                    new DataListGrid("gs://inon-charge.appspot.com/"+id+"/photo_profil.jpg",
                    id
                    )
            );
        }
        return listDataUser;
    }

    private void setAdapters() {
        ListViewAdapterChat listViewAdapterChat = new ListViewAdapterChat(
                this.getContext(),
                R.layout.list_user_chat,
                listDataUser
        );
        usersList.setAdapter(listViewAdapterChat);
    }

    private class LongOperation extends AsyncTask<Void, Void, ListViewAdapterChat> {
        private Context context;
        public LongOperation(Context c){
            super();
            this.context = c;
        }
        @Override
        protected ListViewAdapterChat doInBackground(Void... params) {
            ListViewAdapterChat gvab =  new ListViewAdapterChat(
                    context,
                    R.layout.list_user_chat,
                    listDataUser
            );
            return gvab;
        }
        @Override
        protected void onPostExecute(ListViewAdapterChat result) {
            usersList.setAdapter(result);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
