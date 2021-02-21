package ensa.application01.androidproject_ginf3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //déclaration des variables
    public TextView register , forgotPassword;
    private EditText user_email,user_password;
    private Button signin;

    //Instance de FirebaseAuth déclarée suivant l'assitant de connexion au projet firebase
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialisation / capture des input via les variables
        register =(TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signin = (Button) findViewById(R.id.login);
        signin.setOnClickListener(this);

        user_email = (EditText) findViewById(R.id.email_login);
        user_password = (EditText) findViewById(R.id.password_login);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword =(TextView) findViewById(R.id.forgotpw);
        forgotPassword.setOnClickListener(this);
    }

    //implémentation des redirection & liaison des vues via des Intent
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register:
                startActivity(new Intent(this,RegisterUser.class));
                break;

            case R.id.login:
                userLogin();
                break;
            case R.id.forgotpw:
                startActivity(new Intent(this,ForgotPassword.class));
                break;

        }
    }

    //implementation de la méthode userLogin
    private void userLogin() {
        //création des variables pour la valdiation, on utilise tostring pour la conversion et Trim pour enlever les espaces , c'est une bonne pratique à suivre
        String mail = user_email.getText().toString().trim();
        String pw = user_password.getText().toString().trim();

        //vérification des champs mail / pw
        if(mail.isEmpty()){
            user_email.setError("Email is Required !");
            user_email.requestFocus();
            progressBar.setVisibility(View.GONE); // enlever la barre du progrès
            return;
        }
        //verification de l'email, on utilise Patterns pour pouvoir verifier l'email puisque son pattern est deja connu dan la classe patterns
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            user_email.setError("Please enter a valid email !");
            user_email.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if(pw.isEmpty()){
            user_password.setError("Password should not be Empty !");
            user_password.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        //vérification de la longueur du mot de passe (selon les critères de Firebase)
        if(user_password.length() < 6){
            user_password.setError("Password should be at least 6 characters");
            user_password.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(mail,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //check email address validation
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        //user redirection to profile
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Account verification required ! please check your email !",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(MainActivity.this,"Failed to login ! Please check your credentials",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }
}