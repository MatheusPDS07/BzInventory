package com.bz.bzinventory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button signin;
    private EditText email;
    private EditText passw;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);


        if(isOnline()){

            firebaseAuth = FirebaseAuth.getInstance();
            if(firebaseAuth.getCurrentUser()!=null){
                finish();
                startActivity(new Intent(getApplicationContext(),FragmentActivity.class));

            }
            signin = findViewById(R.id.buttonLogin);
            email = findViewById(R.id.editTextEmail);
            passw = findViewById(R.id.editTextPassword);

            signin.setOnClickListener(v -> {
                if(v == signin)
                {
                userLogin();
                }
            });
        }else{
            //Uncomment the below code to Set the message and title from the strings.xml file
            builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

            //Setting message manually and performing action on button click
            builder.setMessage("You are offline. Do you want to close this application ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        finish();
                        Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(getIntent());
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("AlertDialogExample");
            alert.show();
        }
    }


        private void userLogin(){
            String e_mai = email.getText().toString().trim();
            String P_assw = passw.getText().toString().trim();

            if(TextUtils.isEmpty(e_mai))
            {
                Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(P_assw))
            {
                Toast.makeText(this,"Enter pass",Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(e_mai,P_assw)
                    .addOnCompleteListener(this, task -> {

                        if(task.isSuccessful()){
                            //Start profile FragmentActivity (Dashboard)
                            finish();
                            startActivity(new Intent(getApplicationContext(), FragmentActivity.class));

                        }
                    });
        }


        private Boolean isOnline() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
        }

}
