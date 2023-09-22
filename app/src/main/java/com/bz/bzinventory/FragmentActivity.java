package com.bz.bzinventory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bz.bzinventory.adapter.AdapterEquipmentRecyclerView;
import com.bz.bzinventory.daos.DAOEquipment;
import com.bz.bzinventory.fragments.FirstFragment;
import com.bz.bzinventory.fragments.SecondFragment;
import com.bz.bzinventory.objects.CompanyData;
import com.bz.bzinventory.objects.Equipment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FragmentActivity extends AppCompatActivity implements AdapterEquipmentRecyclerView.FirebaseDataListener {

	private EditText equipment_name;
	private EditText equipment_model;

	private Spinner equipment_company_spinner;
	private Spinner equipment_status_spinner;
	private Spinner equipment_type_spinner;
	private Spinner equipment_system_spinner;
	private Spinner equipment_factory_spinner;
	private Spinner equipment_local_spinner;

	private Fragment fragment = null;

	private DAOEquipment daoEquipment;

	@SuppressLint("SuspiciousIndentation")
	@Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
     	setContentView(R.layout.activity_fragment);

		FirebaseApp.initializeApp(this);
		daoEquipment = new DAOEquipment();

		TabLayout tabLayout = findViewById(R.id.tabLayout);
		findViewById(R.id.frameLayout);

		fragment = new SecondFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.frameLayout, fragment);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		fragmentTransaction.commit();

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {

				switch (tab.getPosition()) {
					case 0:
						fragment = new SecondFragment();
						break;
					case 1:
						fragment = new FirstFragment();
						break;
				}
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.frameLayout, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				ft.commit();
			}


			@Override
			public void onTabUnselected(TabLayout.Tab tab) {}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {}
		});
    }
	

	@Override
	public void onDataClick(final Equipment equipment, int position){

		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
		builder.setTitle("SELECT NEW ACTION");
		
		builder.setPositiveButton("UPDATE", (dialog, id) -> dialogUpdate(equipment));
		builder.setNegativeButton("DELETE", (dialog, id) -> daoEquipment.remove(equipment,this));
		builder.setNeutralButton("CANCEL", (dialog, id) -> dialog.dismiss());
		
		Dialog dialog = builder.create();
		dialog.show();
	}

	private void dialogUpdate(final Equipment equipment){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
		builder.setTitle("Edit Data Equipment");
		Toast.makeText(FragmentActivity.this, "Edit Data Equipment", Toast.LENGTH_SHORT).show();
		View view = getLayoutInflater().inflate(R.layout.layout_edit_equipment, null);

		equipment_name = view.findViewById(R.id.equipment_name_2);
		equipment_model = view.findViewById(R.id.equipment_model_2);

		equipment_company_spinner = view.findViewById(R.id.spinner_company_2);
		equipment_status_spinner = view.findViewById(R.id.spinner_status_2);
		equipment_type_spinner = view.findViewById(R.id.spinner_type_2);
		equipment_system_spinner = view.findViewById(R.id.spinner_system_2);
		equipment_factory_spinner = view.findViewById(R.id.spinner_factory_2);
		equipment_local_spinner = view.findViewById(R.id.spinner_local_2);

		equipment_name.setText(equipment.getName());
		equipment_model.setText(equipment.getModel());

		ArrayList<String> list = new ArrayList<>();
		ArrayAdapter<String> adapter = new ArrayAdapter<>(FragmentActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
		equipment_company_spinner.setAdapter(adapter);

		String[] lsStatus = getResources().getStringArray(R.array.list_status);
		equipment_status_spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lsStatus));

		String[] lsType = getResources().getStringArray(R.array.list_type);
		equipment_type_spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lsType));

		String[] lsSystem = getResources().getStringArray(R.array.list_system);
		equipment_system_spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lsSystem));

		String[] lsFactory = getResources().getStringArray(R.array.list_factory);
		equipment_factory_spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lsFactory));

		FirebaseApp.initializeApp(this);
		FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
		DatabaseReference nDatabaseReference = mFirebaseInstance.getReference(CompanyData.class.getSimpleName());
		nDatabaseReference.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				/*Is better to use a List, because you don't know the size of
				  the iterator returned by dataSnapshot.getChildren() to initialize the array*/
				final List<String> areas = new ArrayList<>();
				final List<String> areas_2 = new ArrayList<>();

				for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
					String areaName = areaSnapshot.child("corporateName").getValue(String.class);
					areas.add(areaName);
					String areaName_2 = areaSnapshot.child("name").getValue(String.class);
					areas_2.add(areaName_2);
				}

				ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(FragmentActivity.this, android.R.layout.simple_spinner_item, areas);
				areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				equipment_company_spinner.setAdapter(areasAdapter);

				ArrayAdapter<String> areasAdapter_2 = new ArrayAdapter<>(FragmentActivity.this, android.R.layout.simple_spinner_item, areas_2);
				areasAdapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				equipment_local_spinner.setAdapter(areasAdapter_2);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Toast.makeText(FragmentActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

		builder.setView(view);

		builder.setPositiveButton("UPDATE", (dialog, id) -> {

			equipment.setName(equipment_name.getText().toString());
			equipment.setType(equipment_type_spinner.getSelectedItem().toString());
			equipment.setSystem(equipment_system_spinner.getSelectedItem().toString());
			equipment.setFactory(equipment_factory_spinner.getSelectedItem().toString());
			equipment.setModel(equipment_model.getText().toString());
			equipment.setStatus(equipment_status_spinner.getSelectedItem().toString());
			equipment.setLocal(equipment_local_spinner.getSelectedItem().toString());
			equipment.setCompany(equipment_company_spinner.getSelectedItem().toString());

			daoEquipment.update(equipment,this);

		});
		builder.setNegativeButton("CANCEL", (dialog, id) -> dialog.dismiss());
		Dialog dialog = builder.create();
		dialog.show();

	}

	public void logout(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
		//Uncomment the below code to Set the message and title from the strings.xml file
		builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

		//Setting message manually and performing action on button click
		builder.setMessage("Do you want to exit ?")
				.setCancelable(false)
				.setPositiveButton("Yes", (dialog, id) -> {
					FirebaseAuth.getInstance().signOut();
					startActivity(new Intent(getApplicationContext(),LoginActivity.class));
					Toast.makeText(getApplicationContext(),"Logout",
							Toast.LENGTH_SHORT).show();
				})
				.setNegativeButton("No", (dialog, id) -> {
					//  Action for 'NO' Button
					dialog.cancel();
				});
		//Creating dialog box
		AlertDialog alert = builder.create();
		//Setting the title manually
		alert.setTitle("Exit");
		alert.show();
	}
}
