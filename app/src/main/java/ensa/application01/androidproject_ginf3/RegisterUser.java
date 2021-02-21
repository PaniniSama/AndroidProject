package ensa.application01.androidproject_ginf3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    //Déclaration des variables
    private TextView banner,registerUser;
    private EditText editTextFullName,editTextAge,editTextEmail,editTextPassword;
    private ProgressBar progressBar;

    //instance firebaseAuth
    private FirebaseAuth mAuth;

    //initialisation / capture des input via FindByView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.title);
        banner.setOnClickListener(this);

        registerUser =(TextView) findViewById(R.id.registeruser);
        registerUser.setOnClickListener(this);

        editTextFullName =(EditText) findViewById(R.id.fullname);
        editTextAge =(EditText) findViewById(R.id.age);
        editTextEmail =(EditText) findViewById(R.id.email);
        editTextPassword =(EditText) findViewById(R.id.password);

        progressBar =(ProgressBar) findViewById(R.id.progressbar);

    }

    //redirections & appel de methode register selon l'input
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.registeruser:
                registerUser();
                break;

        }
    }

    //implémentation de la méthode RegisterUser
    private void registerUser(){
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        String fullname=editTextFullName.getText().toString().trim();
        String age=editTextAge.getText().toString().trim();

        //Verification des champs
        if(fullname.isEmpty()) {
            editTextFullName.setError("Full Name is Required !");
            editTextFullName.requestFocus();
            return;
        }
        if(age.isEmpty()) {
            editTextAge.setError("Age is Required !");
            editTextAge.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            editTextEmail.setError("E-mail is Required !");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("please provide a valid email !");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            editTextPassword.setError("Password is Required !");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError("please provide a password with at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        //
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() { // Listener qui se déclanche après l'opération de la création de l'utilisateur via mAuth
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullname,age,email); // instanciation de l'objet user avec nos paramètres
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //retourne l'ID de l'utilisateur qu'on enregistre
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() { //on fait passer notre objet user à la base de données de firebase puis on ajoute un listener pour suivre la logique d'implementation
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){ // si l'utilisateur est enregistré à firebase
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  // si l'opération se termine, nous allons
                                        user.sendEmailVerification(); // envoi d'un email de verification pour confirmer la procedure du Register
                                        Toast.makeText(RegisterUser.this,"User Registered Successfully, please verify your email to confirm the account !",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE); // enlever la barre du progrès
                                        startActivity(new Intent(RegisterUser.this,MainActivity.class)); // redirection vers l'activité login


                                    }else{
                                        //opération pas terminées, on affiche un message d'erreur
                                        Toast.makeText(RegisterUser.this,"Failed to Register, please try again later",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE); // enlever la barre du progrès
                                    }
                                }
                            });
                        }else{
                            //meme erreur, utilisateur non enregistré
                            Toast.makeText(RegisterUser.this,"Failed to Register, please try again later",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}