package com.bz.bzinventory.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bz.bzinventory.R;
import com.bz.bzinventory.daos.DAOCompany;
import com.bz.bzinventory.objects.CompanyData;
import com.bz.bzinventory.objects.Equipment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**This class is responsible for defining the second fragment of FragmentActivity*/
public class SecondFragment extends Fragment {

    /**attributes*/
    private EditText txtName,txtCnpj,txtCorporateName, txtemailgerencia, txtgerencianame;
    private TextView txtValue;
    private TextView txtValue_1;
    private TextView txtValue_2;
    private TextView txtValue_3;
    private TextView txtValue_4;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private DAOCompany dao;
    private String key =null;
    private int cont = 0;
    private int cont_1 = 0;
    private int cont_2 = 0;
    private int cont_3 = 0;
    private int cont_4 = 0;
    private final CompanyData company = new CompanyData();


    /**Required empty public constructor*/
    public SecondFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**A mandatory method of "extends Fragment"*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        FloatingActionButton btnAdd = view.findViewById(R.id.btnAdd);
        txtValue = view.findViewById(R.id.txtValue);

        txtValue_1 = view.findViewById(R.id.txtValue_1);
        txtValue_2 = view.findViewById(R.id.txtValue_2);
        txtValue_3 = view.findViewById(R.id.txtValue_3);
        txtValue_4 = view.findViewById(R.id.txtValue_4);

        Spinner spinner = view.findViewById(R.id.spinner3);

        dao = new DAOCompany();

        list = new ArrayList<>();
        adapter =new ArrayAdapter<>(this.getContext(),android.R.layout.simple_spinner_dropdown_item,list);
        callData();
        spinner.setAdapter(adapter);

        /*This is the action of the button to add a company in the data*/
        btnAdd.setOnClickListener(v -> {

            CompanyData companyData_edit = (CompanyData)requireActivity().
                    getIntent().getSerializableExtra("EDIT");
            insertdata(companyData_edit);

        });


        FirebaseApp.initializeApp(requireContext());
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference nDatabaseReference = mFirebaseInstance.getReference(Equipment.class.getSimpleName());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                nDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            if(Objects.equals(Objects.requireNonNull(ds.child("company").getValue()).toString(), spinner.getSelectedItem().toString())){
                                cont ++;
                                if(Objects.equals(Objects.requireNonNull(ds.child("type").getValue()).toString(), "printer")){
                                    cont_1 ++;

                                }
                                if(Objects.equals(Objects.requireNonNull(ds.child("type").getValue()).toString(), "desktop")){
                                    cont_2 ++;
                                }
                                if(Objects.equals(Objects.requireNonNull(ds.child("type").getValue()).toString(), "notebook")){
                                    cont_4 ++;
                                }
                                if(Objects.equals(Objects.requireNonNull(ds.child("type").getValue()).toString(), "router")){
                                    cont_3 ++;
                                }
                            }
                        }
                        txtValue.setText(String.valueOf(cont));
                        txtValue_1.setText(String.valueOf(cont_1));
                        txtValue_2.setText(String.valueOf(cont_2));
                        txtValue_3.setText(String.valueOf(cont_3));
                        txtValue_4.setText(String.valueOf(cont_4));


                        cont = 0;
                        cont_1 = 0;
                        cont_2 = 0;
                        cont_3 = 0;
                        cont_4 = 0;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return view;
    }

    /**The method responsible for entering the new company into the data*/
    public void insertdata(CompanyData companyData_edit) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        builder.setTitle("add company");
        View view2 = getLayoutInflater().inflate(R.layout.layout_add_company, null);

        txtName = view2.findViewById(R.id.cnpj);
        txtCnpj = view2.findViewById(R.id.name_company);
        txtCorporateName = view2.findViewById(R.id.name_fantasy);
        txtemailgerencia = view2.findViewById(R.id.email_gerencia);
        txtgerencianame = view2.findViewById(R.id.gerencia_name);

        builder.setView(view2);

        builder.setPositiveButton("submit", (dialog, id) -> {

            company.setCnpj(txtCnpj.getText().toString());
            company.setName(txtName.getText().toString());
            company.setGerencia(txtgerencianame.getText().toString());
            company.setEmail(txtemailgerencia.getText().toString());
            company.setCorporateName(txtCorporateName.getText().toString());

            if(companyData_edit==null) {
                dao.add(company).addOnSuccessListener(suc ->
                        Toast.makeText(getContext(), "Record is inserted", Toast.LENGTH_SHORT).show()).addOnFailureListener(er ->
                        Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        builder.setNegativeButton("cancel", (dialog, id) -> dialog.dismiss());
        Dialog dialog = builder.create();
        dialog.show();

    }

    /**The method responsible for calling companies from the data*/
    public void callData() {
        dao.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mydata : snapshot.getChildren()){
                    list.add(mydata.child("corporateName").getValue(String.class));
                    key = mydata.getKey();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.exit(0);
            }
        });
    }
}