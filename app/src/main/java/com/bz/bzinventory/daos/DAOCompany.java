package com.bz.bzinventory.daos;

import com.bz.bzinventory.objects.CompanyData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**This class is DAO access to company data*/
public class DAOCompany {

    /**attributes*/
    private final DatabaseReference databaseReference;

    /**constructor*/
    public DAOCompany() {
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        databaseReference = db.getReference(CompanyData.class.getSimpleName());
    }

    /**methods responsible for performing query operations in firebase.*/

    public Task<Void> add(CompanyData companyData) {
        return databaseReference.push().setValue(companyData);
    }

    public Query get(String key) {
        if(key == null)
        {
            return databaseReference.orderByKey().limitToFirst(8);
        }
        return databaseReference.orderByKey().startAfter(key).limitToFirst(8);
    }
}
