package com.inti.student.randomfoodchoice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    Button btn_reg;
    EditText fname;
    EditText lname;
    EditText age;
    EditText email;
    EditText username;
    EditText pass;
    EditText pass2;
    RadioGroup genderG;
    RadioButton radio;
    Input input;
    FirebaseDatabase fbDB;
    DatabaseReference ref;
    FirebaseAuth fbAu;
    FirebaseUser fbUr;
    String id;
    int x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_reg = (Button) findViewById(R.id.reg_btn);
        fname = (EditText) findViewById(R.id.input_fname);
        lname = (EditText) findViewById(R.id.input_lname);
        age = (EditText) findViewById(R.id.input_age);
        email = (EditText) findViewById(R.id.input_email);
        username = (EditText) findViewById(R.id.input_username);
        pass = (EditText) findViewById(R.id.input_pass);
        pass2 = (EditText) findViewById(R.id.input_repass);
        genderG = (RadioGroup) findViewById(R.id.input_gender);
        fbDB = FirebaseDatabase.getInstance(); //  Firebase Real Time Database
        fbAu = FirebaseAuth.getInstance(); // Firebase Authentication
        ref = fbDB.getReference().child("User"); // Insert new child
        input = new Input();

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // Add into database
                //String key = ref.push().getKey();
                //Log.d(key, "index");
                int p = reg_login();
                //Toast.makeText(Register.this , ""+p, Toast.LENGTH_SHORT).show();
                if(p == 2){
                    login(p);
                    Intent i = new Intent(Register.this, Home.class);
                    startActivity(i);
                    fbUr = fbAu.getCurrentUser();
                    finish();
                }
                else{
                    Toast.makeText(Register.this, "Fail Register Try Again.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        fbUr = fbAu.getCurrentUser();
        if(fbUr != null){
            fbAu.signOut();
            //Toast.makeText(Register.this, "shit happens", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Register.this, Login.class);
            startActivity(i);
        }
    }

    public void getKey(String id){
        //Toast.makeText(Register.this, id, Toast.LENGTH_SHORT).show();
        reg_data(id);
    }

    private int reg_login(){
        int p = checkValid();
        int work = 0;
        final String mail = email.getText().toString();
        final String pwd = pass.getText().toString();
        if(p == 2){
            fbAu.createUserWithEmailAndPassword(mail, pwd)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this, "Login Created", Toast.LENGTH_SHORT).show();

//                            fbUr = fbAu.getCurrentUser();
//                            reg_data(fbUr.getUid());
                            }
                            else if(!task.isSuccessful()){
                                FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                Toast.makeText(Register.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            work = 2;
        }
        else if(p == 1){
            work = 1;
        }
        return work;
    }

    private int login(int prog){
        if(prog == 2){
            final String mail = email.getText().toString();
            final String pwd = pass.getText().toString();
            fbAu.signInWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String uid = fbAu.getCurrentUser().getUid();
                        getKey(uid);
                        x = 0;
                    }
                    else{
                        Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if(prog == 1){
            x = 1;
            //Toast.makeText(Register.this, "", Toast.LENGTH_SHORT).show();
        }
        return x;
    }

    private void getValue(){
        //int x = ; //Detect input format and empty inputs
        input.setFname(fname.getText().toString());
        input.setLname(lname.getText().toString());
        input.setAge(age.getText().toString());
        input.setGender(radio.getText().toString());
        input.setEmail(email.getText().toString());
        input.setUsername(username.getText().toString());
        input.setPass(pass.getText().toString());
    }

    public void reg_data(String id){
        getValue();
        ref.child(id).setValue(input);
        Toast.makeText(Register.this, "Registered, Sign in Now.", Toast.LENGTH_LONG).show();;
    }

    private int checkValid() {
        if (fname.getText().toString().length() != 0) {
            if (lname.getText().toString().length() != 0) {
                if (genderG.getCheckedRadioButtonId() != -1) {
                    int id = genderG.getCheckedRadioButtonId();
                    radio = (RadioButton) findViewById(id);
                    if (age.getText().toString().length() != 0) {
                        int num = Integer.parseInt(age.getText().toString());
                        if(num >= 6 && num <= 100){
                            if (username.getText().toString().length() != 0) {
                                if (email.getText().toString().length() != 0) {
                                    if (pass.getText().toString().length() != 0) {
                                        int p1 = pass.getText().toString().length();
                                        if (p1 >= 8){
                                            if (pass2.getText().toString().length() != 0) {
                                                int p2 = pass2.getText().toString().length();
                                                if(p2 >= 8){
                                                    if (pass.getText().toString().equals(pass2.getText().toString())) {
                                                        x = 2;
                                                    } else {
                                                        x = 1;
                                                        pass2.setError("Password Not Match");
                                                        pass2.requestFocus();
                                                    }
                                                }
                                                else if(p2 < 8){
                                                    x = 1;
                                                    pass2.setError("Too Short");
                                                    pass2.requestFocus();
                                                }
                                            } else if (pass2.getText().toString().length() == 0) {
                                                x = 1;
                                                pass2.setError("Empty");
                                                pass2.requestFocus();
                                            }
                                        }
                                        else if(p1 < 8){
                                            x = 1;
                                            pass.setError("Too Short");
                                            pass.requestFocus();
                                        }
                                    } else if (pass.getText().toString().length() == 0) {
                                        x = 1;
                                        pass.setError("Empty");
                                        pass.requestFocus();
                                    }
                                } else if (email.getText().toString().length() == 0) {
                                    x = 1;
                                    email.setError("Empty");
                                    email.requestFocus();
                                }
                            } else if (username.getText().toString().length() == 0) {
                                x = 1;
                                username.setError("Empty");
                                username.requestFocus();
                            }
                        } else{
                            x = 1;
                            age.setError("Invalid Age Number");
                            age.requestFocus();
                        }
                    } else if (age.getText().toString().length() == 0) {
                        x = 1;
                        age.setError("Empty");
                        age.requestFocus();
                    }
                } else if (genderG.getCheckedRadioButtonId() == -1) {
                    x = 1;
                    Toast.makeText(Register.this, "Radio Button is no checked!", Toast.LENGTH_SHORT).show();
                }
            } else if (lname.getText().toString().length() == 0) {
                x = 1;
                lname.setError("Empty");
                lname.requestFocus();
            }
        } else if (fname.getText().toString().length() == 0) {
            x = 1;
            fname.setError("Empty");
            fname.requestFocus();
        }
        return x;
    }
}
