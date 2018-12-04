package com.inti.student.randomfoodchoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Roll extends AppCompatActivity {

    FirebaseDatabase fbDb;
    DatabaseReference ref;
    FirebaseAuth fbAu;
    FirebaseUser fbUr;
    ArrayList<String> aList;
    ArrayAdapter<String> aListAdapter;
    private TextView res;
    private Button btn_agn;
    private String id;
    RollResult rr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);

        btn_agn = (Button)findViewById(R.id.again_btn);
        fbDb = FirebaseDatabase.getInstance();
        fbAu = FirebaseAuth.getInstance();
        fbUr = fbAu.getCurrentUser();
        id = fbUr.getUid();
        ref = fbDb.getReference().child("User").child(id).child("result");
        res = (TextView)findViewById(R.id.result_txt);
        rr = new RollResult();
//        no = getIntent().getIntExtra("root",0);

        btn_agn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                ShuffleList();
                Toast.makeText(Roll.this,"ROLLED",Toast.LENGTH_SHORT).show();
                displayResult();
            }
        });
        ShuffleList();
        displayResult();
    }

//    public void onStart(){
//        super.onStart();
//        fbUr = fbAu.getCurrentUser();
//        if(fbUr != null){
//            id = fbUr.getUid();
//            ref = fbDb.getReference().child("User").child(id);
//        }
//        else if(fbUr == null){
//            Intent i = new Intent(Roll.this, Login.class);
//            startActivity(i);
//        }
//    }

//    private void getValue(){
//        rr.setResult(aListAdapter.getItem(0));
//
//    }

    public void displayResult(){
        ShuffleList();
        Intent i = getIntent();
        String dte = i.getStringExtra("dt");
        String fRow = aList.get(0).toString();
        res.setText(fRow);
        System.out.println(dte);
        ref.child(dte+"/final/result").setValue(fRow);
//        getValue();
//        id = fbUr.getUid();
//        ref = fbDb.getReference().child("User").child(id).child("result");
//        getR(fRow);
//        rr.setResult(fRow);
//        Toast.makeText(Roll.this, "1 "+rr.getResult().toString(), Toast.LENGTH_LONG).show();
//        ref.child(dte).child("final").setValue(rr);
    }

    private void ShuffleList(){
        Bundle db = getIntent().getExtras();
        aList = (ArrayList<String>)db.getSerializable("array");
        Collections.shuffle(aList);
        aListAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, aList);
    }

//    private void saveArray(){
//        Toast.makeText(Roll.this, aList.get(0), Toast.LENGTH_SHORT).show();
//    }
}
