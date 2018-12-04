package com.inti.student.randomfoodchoice;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    TextView btn_reg;
    Button btn_login;
    EditText email;
    EditText pass;
    FirebaseDatabase fbDB;
    FirebaseAuth fbAu;
    FirebaseUser fbUr;
    DatabaseReference ref;
    FirebaseAuth.AuthStateListener fbLs;
    TextView tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_reg = (TextView) findViewById(R.id.reg_btn);
        btn_login = (Button) findViewById(R.id.login_btn);
        email = (EditText) findViewById(R.id.input_email);
        pass = (EditText) findViewById(R.id.input_password);
        fbDB = FirebaseDatabase.getInstance();
        fbAu = FirebaseAuth.getInstance();
        ref = fbDB.getReference().child("User");
        tx = (TextView)findViewById(R.id.signIN);

//        fbLs = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if(fbUr != null){
//                    Intent i = new Intent(Login.this, Home.class);
//                    startActivity(i);
//                }
//            }
//        };

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        fbUr = fbAu.getCurrentUser();
        if(fbUr != null){
            //Toast.makeText(Login.this, "shit happens", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login.this, Home.class);
            startActivity(i);
        }else{
            //Toast.makeText(Login.this, "Sign in", Toast.LENGTH_SHORT).show();
        }
    }

    private void Login(){
        String m = email.getText().toString();
        String p = pass.getText().toString();

        if(TextUtils.isEmpty(m)){
            email.setError("Empty");
            email.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(p)){
            pass.setError("Empty");
            pass.requestFocus();
            return;
        }
        else if(p.length() < 8){
            pass.setError("Password too short");
            pass.requestFocus();
            return;
        }
        else{
            fbAu.signInWithEmailAndPassword(m, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent i = new Intent(Login.this, Home.class);
                        startActivity(i);
                    }
                    else{
                        FirebaseAuthException e = (FirebaseAuthException)task.getException();
                        Toast.makeText(Login.this, "Failed Login: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        }

    }

}
