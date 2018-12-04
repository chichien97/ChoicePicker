package com.inti.student.randomfoodchoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    FirebaseDatabase fbDb;
    FirebaseAuth fbAu;
    FirebaseUser fbUr;
    DatabaseReference ref;
    String uid;
    ArrayList<String> item = new ArrayList<String>();
    ArrayAdapter<String> itemAdap;
    ListView itemList;
    TextView fRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        fbDb = FirebaseDatabase.getInstance();
        fbAu = FirebaseAuth.getInstance();
        fbUr = fbAu.getCurrentUser();
        uid = fbUr.getUid();
        ref = fbDb.getReference().child("User").child(uid).child("result");
        itemList = (ListView) findViewById(R.id.list_items);
        fRes = (TextView) findViewById(R.id.result_txt);
        itemAdap = new ArrayAdapter<String>(Result.this, R.layout.support_simple_spinner_dropdown_item, item);
        show();

    }

    private void show(){
        Intent i = getIntent();
        String dt = i.getStringExtra("Key");
//        String sub = dt.substring(dt.indexOf("") +1);
        Toast.makeText(Result.this, dt, Toast.LENGTH_SHORT).show();
        ref.child(dt).child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String itemName = ds.getValue().toString();
//                    Toast.makeText(Result.this, itemName, Toast.LENGTH_SHORT).show();
                    item.add(itemName);
                }
                itemList.setAdapter(itemAdap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref.child(dt).child("final").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String finalRes = ds.getValue().toString();
                    fRes.setText(finalRes);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
