package com.bz.bzinventory.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bz.bzinventory.R;
import com.bz.bzinventory.adapter.AdapterEquipmentRecyclerView;
import com.bz.bzinventory.adapter.PrintAdapter;
import com.bz.bzinventory.daos.DAOCompany;
import com.bz.bzinventory.daos.DAOEquipment;
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
import java.util.List;
import java.util.Objects;

/**This class is responsible for defining the first fragment of FragmentActivity*/
public class FirstFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private AdapterEquipmentRecyclerView mAdapter;
    private ArrayList<Equipment> daftarEquipment;
    private EditText equipment_name;
    private EditText equipment_model;
    private Spinner equipment_company_spinner;
    private Spinner equipment_status_spinner;
    private Spinner equipment_type_spinner;
    private Spinner equipment_system_spinner;
    private Spinner equipment_factory_spinner;
    private Spinner equipment_local_spinner;
    private DAOEquipment daoEquipment;

    private final Equipment equipment = new Equipment();

    private int cont = 0;

    private final DAOCompany dao = new DAOCompany();
    private String key =null;
    private final ArrayList<String> list = new ArrayList<>();

    private int id_model;

    /**Required empty public constructor*/
    public FirstFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**A mandatory method of "extends Fragment"*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        daoEquipment = new DAOEquipment();

        /*pesquisa por pousadas*/
        SearchView searchView = view.findViewById(R.id.action_search);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        FirebaseApp.initializeApp(requireContext());
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseInstance.getReference(Equipment.class.getSimpleName());
        mDatabaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                daftarEquipment = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()){
                    Equipment equipment = mDataSnapshot.getValue(Equipment.class);
                    Objects.requireNonNull(equipment).setKey(mDataSnapshot.getKey());
                    daftarEquipment.add(equipment);
                }
                //set adapter RecyclerView
                mAdapter = new AdapterEquipmentRecyclerView(getContext(), daftarEquipment);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                // TODO: Implement this method
                Toast.makeText(getContext(), databaseError.getDetails()+" "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

        FloatingActionButton mFloatingActionButton = view.findViewById(R.id.floatingActionButton4);

        mFloatingActionButton.setOnClickListener(view1 -> addEquipment());

        FloatingActionButton nFloatingActionButton = view.findViewById(R.id.floatingActionButton3);
        nFloatingActionButton.setOnClickListener(v -> {
            PrintAdapter printAdapter = PrintAdapter.getInstance();
            printAdapter.printDocument(getContext());
            Toast.makeText(getContext(), "download", Toast.LENGTH_LONG).show();
        });

        FloatingActionButton FloatingActionButton = view.findViewById(R.id.floatingActionButton2);
        FloatingActionButton.setOnClickListener(v -> {
            PrintAdapter printAdapter = PrintAdapter.getInstance();
            printAdapter.uploadDocument(getActivity());
            Toast.makeText(getContext(), "upload", Toast.LENGTH_LONG).show();
        });

        return view;
    }

    //dialog update barang / update data barang
    private void addEquipment(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        builder.setTitle("add equipment");
        View view = getLayoutInflater().inflate(R.layout.layout_add_equipment, null);

        equipment_name = view.findViewById(R.id.equipment_name);
        equipment_model = view.findViewById(R.id.equipment_model);
        equipment_company_spinner = view.findViewById(R.id.spinner_company);
        equipment_status_spinner = view.findViewById(R.id.spinner_status);
        equipment_type_spinner = view.findViewById(R.id.spinner_type);
        equipment_system_spinner = view.findViewById(R.id.spinner_system);
        equipment_factory_spinner = view.findViewById(R.id.spinner_factory);
        equipment_local_spinner = view.findViewById(R.id.spinner_local);

        FirebaseApp.initializeApp(requireContext());
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference nDatabaseReference = mFirebaseInstance.getReference(CompanyData.class.getSimpleName());
        nDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<>();
                final List<String> areas2 = new ArrayList<>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("corporateName").getValue(String.class);
                    areas.add(areaName);

                    String areaName2 = areaSnapshot.child("name").getValue(String.class);
                    areas2.add(areaName2);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                equipment_company_spinner.setAdapter(areasAdapter);

                ArrayAdapter<String> areasAdapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, areas2);
                areasAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                equipment_local_spinner.setAdapter(areasAdapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        String[] lsStatus = getResources().getStringArray(R.array.list_status);
        equipment_status_spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, lsStatus));

        String[] lsType = getResources().getStringArray(R.array.list_type);
        equipment_type_spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, lsType));

        String[] lsSystem = getResources().getStringArray(R.array.list_system);
        equipment_system_spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, lsSystem));

        String[] lsFactory = getResources().getStringArray(R.array.list_factory);
        equipment_factory_spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, lsFactory));

        builder.setView(view);

        builder.setPositiveButton("submit", (dialog, id) -> {

            dao.get(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot mydata : snapshot.getChildren()) {
                        list.add(mydata.child("corporateName").getValue(String.class));
                        key = mydata.getKey();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.exit(0);
                }
            });




            daoEquipment.get(null).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if (Objects.equals(Objects.requireNonNull(ds.child("company").getValue()).toString(), equipment_company_spinner.getSelectedItem().toString())) {
                            cont++;
                        }
                    }

                    for (int i = 0; i < list.size(); i++) {
                            if (equipment_company_spinner.getSelectedItem().toString().equals(list.get(i))) {
                                id_model = 1000 * (i + 1);
                            } else {
                                equipment.setId((cont) + " company not found");
                            }
                    }

                    if(list.contains(equipment_company_spinner.getSelectedItem().toString())){
                        equipment.setId(String.valueOf((id_model + cont)));
                    }else {
                        equipment.setId((cont) + " company not found");
                    }

                    equipment.setName(equipment_name.getText().toString());
                    equipment.setType(equipment_type_spinner.getSelectedItem().toString());
                    equipment.setSystem(equipment_system_spinner.getSelectedItem().toString());
                    equipment.setFactory(equipment_factory_spinner.getSelectedItem().toString());
                    equipment.setModel(equipment_model.getText().toString());
                    equipment.setStatus(equipment_status_spinner.getSelectedItem().toString());
                    equipment.setLocal(equipment_local_spinner.getSelectedItem().toString());
                    equipment.setCompany(equipment_company_spinner.getSelectedItem().toString());

                    daoEquipment.add(equipment);

                    cont = 0;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(view.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> dialog.dismiss());
        Dialog dialog = builder.create();
        dialog.show();

    }
}