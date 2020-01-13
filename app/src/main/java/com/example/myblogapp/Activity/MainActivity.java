package com.example.myblogapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myblogapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private TextView login;
    private TextView createaccount;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseUser muser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        login=(TextView)findViewById(R.id.login);
        createaccount=(TextView)findViewById(R.id.createaccount);
        mauth=FirebaseAuth.getInstance();
        listener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                muser=firebaseAuth.getCurrentUser();
                if(muser!=null)
                {
                    Toast.makeText( getApplicationContext(),"Signed in already", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,PostListActivity.class));
                    finish();
                }
                else
                    Toast.makeText(MainActivity.this,"Not Signed In",Toast.LENGTH_LONG).show();

            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstring = email.getText().toString();
                String pwd = password.getText().toString();
                if (!emailstring.equals("") && !pwd.equals("")) {
                    mauth.signInWithEmailAndPassword(emailstring, pwd).addOnCompleteListener
                            (MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                   if(task.isSuccessful())
                                   {
                                       Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                                   }
                                   else
                                       Toast.makeText(MainActivity.this, "Either username or password doesnot exist", Toast.LENGTH_SHORT).show();
                                }
                            });
                 //   email.setText("");
                    password.setText("");

                }
                else
                    Toast.makeText(MainActivity.this, "Enter a username and password", Toast.LENGTH_SHORT).show();
            }
        });

createaccount.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      /*  AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
        AlertDialog dialog;
        v=getLayoutInflater().inflate(R.layout.createaccount_dialog,null);
        email2=(EditText)v.findViewById(R.id.email2);
        password2=(EditText)v.findViewById(R.id.password2);
        createaccount2=(TextView)v.findViewById(R.id.createaccount);
        createaccount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/
                String emailstring = email.getText().toString();
                String pwd = password.getText().toString();
                if (!emailstring.equals("") && !pwd.equals("")) {

               mauth.createUserWithEmailAndPassword(emailstring,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful())
                     {
                         Toast.makeText(MainActivity.this, "Account created successfully!!", Toast.LENGTH_SHORT).show();
                     }
                     else
                     {
                         Toast.makeText(MainActivity.this, "Failed creating account!!", Toast.LENGTH_SHORT).show();
                     }
                   }
               });
               //email.setText("");
               password.setText("");
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Enter a username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //alert.setView(v);
        //dialog=alert.create();
        //dialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout) {
            mauth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listener!=null)
        {
mauth.removeAuthStateListener(listener);
        }
    }
}
