package ensa.application01.androidproject_ginf3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class ProfileActivity extends AppCompatActivity {

    //déclaration de nos variables
    private Button logout;
    private FirebaseUser user; // utilisateur Firebase
    private DatabaseReference ref; // réference de la base de données
    private String UserId; // ID de l'utilisateur authentifié



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout =(Button) findViewById(R.id.sigbOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser(); // l'utilisateur actuellement connecté
        ref = FirebaseDatabase.getInstance().getReference("Users"); //réference de la base de données, nous allons referencer la collection users.
        UserId = user.getUid(); // prendre l'ID de notre utilisateur connecté

        // pour acceder à ces champs via nos classes ils doivent etre declarés comme final
        final TextView greetingTextView = (TextView) findViewById(R.id.greetingTextView);
        final TextView fullNameTextView = (TextView) findViewById(R.id.profile_Fullname);
        final TextView emailNameTextView = (TextView) findViewById(R.id.emailNameTextView);
        final TextView ageTextView = (TextView) findViewById(R.id.ageTextView);

        //acceder aux données en temps reel via la base de données firebase
        ref.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //objet de la classe utilisateur
                User userprofile = snapshot.getValue(User.class); //snapshot est une instance de type DataSnapshot de firebase , qui permet l'acces aux données firebase, les données lus de firebase sont retournés sous cette forme de snapshot
                if(userprofile != null){
                    String fullName = userprofile.fullname;
                    String emailadd = userprofile.email;
                    String age = userprofile.age;

                    greetingTextView.setText("Welcome, "+ fullName + "!");
                    fullNameTextView.setText(fullName);
                    emailNameTextView.setText(emailadd);
                    ageTextView.setText(age);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Oops, something went wrong !",Toast.LENGTH_LONG).show();
            }
        });

    }
}