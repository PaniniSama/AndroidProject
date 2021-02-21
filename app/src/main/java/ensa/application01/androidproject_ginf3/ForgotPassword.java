package ensa.application01.androidproject_ginf3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    //declaration des variables

    private EditText emailEditText;
    private Button resetpwButton;
    private ProgressBar progressBar;
    FirebaseAuth mAuth; //objet de firebaseAuth

    //capture des input utilisateur
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText =(EditText) findViewById(R.id.email_fp);
        resetpwButton =(Button) findViewById(R.id.resetpw);
        progressBar =(ProgressBar) findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        //implementation du listener où on fait un appel de la methode resetpassword
        resetpwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    //implémentation du ResetPassword, notre méthode qui represente cette fonctionnalité
    private void resetPassword() {
        String Email= emailEditText.getText().toString().trim();

        //vérification des champs
        if(Email.isEmpty()){
            emailEditText.setError("Email is required !");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            emailEditText.setError("Provide a Valid Email !");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //appel de methode resetpassword à partir de l'objet firebaseauth
        mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check your Email address to reset your password !",Toast.LENGTH_LONG);
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(ForgotPassword.this,"Oops, Something went wrong, please try again !", Toast.LENGTH_LONG);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}