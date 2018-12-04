package com.inti.student.randomfoodchoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class History extends AppCompatActivity {

    FirebaseDatabase fbDb;
    FirebaseAuth fbAu;
    FirebaseUser fbUr;
    DatabaseReference ref;
    String uid;
    String key;
    ListView his_list;
    ArrayList<String> listArray = new ArrayList<>();
    ArrayAdapter<String> listAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        his_list = (ListView) findViewById(R.id.list_history);
        fbDb = FirebaseDatabase.getInstance();
        fbAu = FirebaseAuth.getInstance();
        fbUr = fbAu.getCurrentUser();
        uid = fbUr.getUid();
        ref = fbDb.getReference().child("User").child(uid).child("result");
        listAdap = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listArray);

//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                for (String user : map.values()) {
////                    Toast.makeText(History.this, user, Toast.LENGTH_SHORT).show();
////                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                key = dataSnapshot.getKey().toString();
//                listArray.add("something " + key);
                listArray.add(""+key);
                Collections.sort(listArray, Collections.<String>reverseOrder());
                his_list.setAdapter(listAdap);
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        his_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = his_list.getAdapter().getItem(position);
                String val = obj.toString();
                Toast.makeText(History.this, val, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(History.this, Result.class);
                i.putExtra("Key", val);
                startActivity(i);
            }
        });

        his_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listArray.remove(position);
                his_list.setAdapter(listAdap);
                ref.child(key).removeValue();
                Toast.makeText(History.this, "Deleted", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                return true;
            }
        });
    }
}
