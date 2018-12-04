package com.inti.student.randomfoodchoice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity {
    private EditText inputChoice;
    private Button btn_enter;
    private Button btn_roll;
    private Button btn_clear;
    private String getInput;
    private ListView lv;
    ArrayPass[] pass;
    private Toolbar tlb;
    private ArrayList<String> choiceArray = new ArrayList<>();
    private ArrayAdapter<String> choiceAdapter;
    private FirebaseDatabase fbDb;
    private FirebaseAuth fbAu;
    private FirebaseUser fbUr;
    private DatabaseReference ref;
    private String id;
    private int no = 0;
    private Date date;
    private String dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        inputChoice = (EditText)findViewById(R.id.input_choice);
        lv = (ListView)findViewById(R.id.list_view);
        btn_enter = (Button)findViewById(R.id.enter_btn);
        btn_roll = (Button)findViewById(R.id.roll_btn);
        btn_clear = (Button)findViewById(R.id.clear_btn);
        tlb = (Toolbar) findViewById(R.id.tool_bar);
        date = new Date();
        fbDb = FirebaseDatabase.getInstance();
        fbAu = FirebaseAuth.getInstance();
        ref = fbDb.getReference().child("User");
        choiceAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, choiceArray);
        setSupportActionBar(tlb);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        getSupportActionBar().setIcon(R.drawable.ic_shuffle);

        btn_enter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Get input and Show input in listView
                getInput = inputChoice.getText().toString();

                // Check for duplicate and empty input
                if(choiceArray.contains(getInput)) {
                    Toast.makeText(getBaseContext(), R.string.item_dup, Toast.LENGTH_SHORT).show();
                }
                else if(getInput == null || getInput.trim().equals("")) {
                    Toast.makeText(getBaseContext(),R.string.item_empty, Toast.LENGTH_SHORT).show();
                }
                else{
                    pass = new ArrayPass[]{(new ArrayPass(getInput))};
                    choiceArray.add(getInput);
                    lv.setAdapter(choiceAdapter);
                    ((EditText)findViewById(R.id.input_choice)).setText("");
                }
            }

        });

        btn_roll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(choiceArray.isEmpty()){
                    Toast.makeText(Home.this, "Nothing is list", Toast.LENGTH_SHORT).show();
                }
                else if(choiceArray.size() == 1){
                    Toast.makeText(Home.this, "There is only 1 option", Toast.LENGTH_SHORT).show();
                }
                else{
                    saveArrayList();
                }
//                Intent i = new Intent(Home.this, Roll.class);
//                Toast.makeText(Home.this, dt_child, Toast.LENGTH_SHORT).show();
//                i.putExtra("dt",dt);
//                Bundle db = new Bundle();
//                db.putSerializable("array",choiceArray);
//                i.putExtras(db);
//                startActivity(i);
//                finish();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                choiceArray.clear();
                lv.setAdapter(choiceAdapter);
                choiceAdapter.notifyDataSetChanged();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choiceArray.remove(position);
                lv.setAdapter(choiceAdapter);
                choiceAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onBackPressed()
    {
//        finish();
        finishAffinity();
    }

    @Override
    public void onStart(){
        super.onStart();
        fbUr = fbAu.getCurrentUser();
        if(fbUr != null){
            id = fbUr.getUid();
        }
        else if(fbUr == null){
            Intent i = new Intent(Home.this, Login.class);
            startActivity(i);
        }
//        uid.setText(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.history_btn:
                Intent i = new Intent(Home.this, History.class);
                startActivity(i);
                break;
            case R.id.logout_btn:
                fbAu.signOut();
                Toast.makeText(Home.this, "Logout", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(Home.this, Login.class);
                startActivity(in);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void saveArrayList(){
//        String key = ref.child(id).push().getKey(); create roll
        ref.child(id).child("result").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(Home.this, ""+dataSnapshot.getChildren(),Toast.LENGTH_SHORT).show();
                int detect = 0;
                DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
                dt = dateF.format(date.getTime());
                while(detect != 1) {
                    if (!dataSnapshot.hasChild(""+dt)) {
                        //no += 1;
                        //Toast.makeText(Home.this, "no "+no, Toast.LENGTH_SHORT).show();
                        getKey(dt);
                        detect = 1;
                    }
                    else {
                        //Toast.makeText(getBaseContext(), "yes "+no, Toast.LENGTH_SHORT).show();
//                        no += 1;
                        detect = 0;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getKey(String dt){
        //Toast.makeText(Home.this, "1 "+dt, Toast.LENGTH_SHORT).show();
        setArray(""+dt);
    }

    private void setArray(String child){
        for(String items : choiceArray) {
            ref.child(id).child("result").child(child+"/items").setValue(choiceArray);
        }
        Intent i = new Intent(Home.this, Roll.class);
        Toast.makeText(Home.this, dt, Toast.LENGTH_SHORT).show();
        Bundle db = new Bundle();
        db.putSerializable("array",choiceArray);
        i.putExtra("dt",dt);
        i.putExtras(db);
        startActivity(i);
        finish();
    }
}
