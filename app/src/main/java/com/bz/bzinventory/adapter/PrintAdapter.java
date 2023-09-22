package com.bz.bzinventory.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bz.bzinventory.daos.DAOEquipment;
import com.bz.bzinventory.objects.Equipment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**This class is responsible for generating an adaptation to generate a file with firebase data*/
public class PrintAdapter {

    /**Attributes*/
    private static PrintAdapter instance;
    private FileOutputStream fosExt;
    private final LocalDateTime myDateObj = LocalDateTime.now();
    private final DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
    private final String formattedDate = myDateObj.format(myFormatObj);

    private List<String> result;
    private List<String> result_2;
    private final DAOEquipment daoEquipment = new DAOEquipment();
    private final Equipment equipment = new Equipment();
    private String a = " ";

    /**Private constructor for singleton method*/
    private PrintAdapter() {}

    //singleton method
    public static PrintAdapter getInstance() {
        if (instance == null) {
            instance = new PrintAdapter();
        }
        return instance;
    }

    /**This is a method responsible for making the document*/
    public void printDocument(Context context){
        String diretorioApp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        File fileExt = new File(diretorioApp, "BZInventory "+formattedDate+".csv");

        fosExt = null;
        try {
            fosExt = new FileOutputStream(fileExt);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseApp.initializeApp(context);
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseInstance.getReference(Equipment.class.getSimpleName());

        try {
            String msgtitulo =  "id" + ";" +
                                "name" + ";" +
                                "type" + ";" +
                                "system" + ";" +
                                "factory" + ";" +
                                "model" + ";" +
                                "status" + ";" +
                                "local" + ";" +
                                "company" + "\n";

            fosExt.write(msgtitulo.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }


        mDatabaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){


                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()){
                    Equipment equipment = mDataSnapshot.getValue(Equipment.class);
                    Objects.requireNonNull(equipment).setKey(mDataSnapshot.getKey());

                    String msgtudo =    mDataSnapshot.child("id").getValue(String.class) + ";" +
                                        mDataSnapshot.child("name").getValue(String.class)+ ";" +
                                        mDataSnapshot.child("type").getValue(String.class)+ ";" +
                                        mDataSnapshot.child("system").getValue(String.class)+ ";" +
                                        mDataSnapshot.child("factory").getValue(String.class)+ ";" +
                                        mDataSnapshot.child("model").getValue(String.class)+ ";" +
                                        mDataSnapshot.child("status").getValue(String.class)+ ";" +
                                        mDataSnapshot.child("local").getValue(String.class)+ ";" +
                                        mDataSnapshot.child("company").getValue(String.class)+"\n";

                    try {

                        fosExt.write(msgtudo.getBytes());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                try {
                    fosExt.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                // TODO: Implement this method
                Toast.makeText(context, databaseError.getDetails()+" "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    /**This is a method responsible for upload the document*/
    @SuppressLint("SetTextI18n")
    public void uploadDocument(Activity activity){

        FileChooser fileChooser = new FileChooser(activity);

        fileChooser.setFileListener(file -> {


            try (FileInputStream fis = new FileInputStream(file)) {

                int content;

                while ((content = fis.read()) != -1) {
                    a = a + (char)content+"";

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            result = new ArrayList<>( Arrays.asList(a.split("\\s*\n\\s*")) );


            for (int i = 0; i < result.size(); i++) {
                if(i != 0){

                    result_2 = new ArrayList<>( Arrays.asList(result.get(i).split("\\s*;\\s*")) );

                    //id	name	type	system	factory	model	status	local	company

                    equipment.setId(result_2.get(0));
                    equipment.setName(result_2.get(1));
                    equipment.setType(result_2.get(2));
                    equipment.setSystem(result_2.get(3));
                    equipment.setFactory(result_2.get(4));
                    equipment.setModel(result_2.get(5));
                    equipment.setStatus(result_2.get(6));
                    equipment.setLocal(result_2.get(7));
                    equipment.setCompany(result_2.get(8));

                    daoEquipment.add(equipment);

                    result_2.clear();
                }

            }



        }).showDialog();

    }
}
