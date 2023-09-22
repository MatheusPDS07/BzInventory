package com.bz.bzinventory.daos;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bz.bzinventory.objects.Equipment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**This class is DAO access to equipment data*/
public class DAOEquipment {

    /**attributes*/
    private final DatabaseReference databaseReference;

    /**constructor*/
    public DAOEquipment() {
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Equipment.class.getSimpleName());
    }

    /**methods responsible for performing query operations in firebase.*/

    public Query get(String key)
    {
        if(key == null)
        {
           return databaseReference.orderByKey().limitToFirst(8);
        }
        return databaseReference.orderByKey().startAfter(key).limitToFirst(8);
    }

    public void add(Equipment equipment) {
       databaseReference.push().setValue(equipment);
    }

    public void remove(@NonNull Equipment equipment, Activity activity) {
        databaseReference.child(equipment.getKey()).removeValue().addOnSuccessListener(mVoid ->
                Toast.makeText(activity, "Removal completed successfully",
                        Toast.LENGTH_LONG).show());
    }

    public void update(@NonNull Equipment equipment, Activity activity){
        databaseReference.child(equipment.getKey()).setValue(equipment).addOnSuccessListener(mVoid ->
                Toast.makeText(activity, "Update completed successfully",
                        Toast.LENGTH_LONG).show());
    }
}
