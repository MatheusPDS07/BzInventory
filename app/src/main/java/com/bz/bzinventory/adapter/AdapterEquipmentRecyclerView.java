package com.bz.bzinventory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bz.bzinventory.R;
import com.bz.bzinventory.model.ItemEquipmentViewHolder;
import com.bz.bzinventory.objects.Equipment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdapterEquipmentRecyclerView extends RecyclerView.Adapter<ItemEquipmentViewHolder> implements Filterable {

	/**Attributes*/
	private final ArrayList<Equipment> listEquipment;

	private final List<Equipment> exampleListFull;

	private final FirebaseDataListener listener;

	/**constructor*/
	public AdapterEquipmentRecyclerView(Context context, ArrayList<Equipment> listEquipment){
		this.listEquipment = listEquipment;

		exampleListFull = new ArrayList<>(listEquipment);
		this.listener = (FirebaseDataListener)context;
	}

	@NonNull
	@Override
	public ItemEquipmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		// TODO: Implement this method
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
		return new ItemEquipmentViewHolder(view);
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(ItemEquipmentViewHolder holder, @SuppressLint("RecyclerView") int position) {
		// TODO: Implement this method
		holder.equipmentId.setText("Id   : "+ listEquipment.get(position).getId());
		holder.equipmentName.setText("Name     : "+ listEquipment.get(position).getName());
		holder.equipmentType.setText("Type   : "+ listEquipment.get(position).getType());
		holder.equipmentCompany.setText("Company   : "+listEquipment.get(position).getCompany());

		for (int i = 0; i < listEquipment.size(); i++) {
			if (position == i){
				StorageReference storageReference = FirebaseStorage.getInstance().getReference(listEquipment.get(position).getCompany() + ".jpeg");
				try {
					File localfile = File.createTempFile("tempFile",".jpeg");
					storageReference.getFile(localfile).addOnSuccessListener(taskSnapshot -> {

						Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
						holder.imageView.setImageBitmap(bitmap);
					}).addOnFailureListener(e -> {
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		holder.view.setOnClickListener(v ->
				listener.onDataClick(listEquipment.get(position), position));
	}

	@Override
	public int getItemCount()
	{
		// TODO: Implement this method
		return listEquipment.size();
	}

	//interface data listener
	public interface FirebaseDataListener {
		void onDataClick(Equipment equipment, int position);
	}

	@Override
	public Filter getFilter() {
		return exampleFilter;
	}

	private final Filter exampleFilter = new Filter() {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			List<Equipment> filteredList = new ArrayList<>();

			if (constraint == null || constraint.length() == 0) {
				filteredList.addAll(exampleListFull);
			} else {
				String filterPattern = constraint.toString().toLowerCase().trim();

				for (Equipment item : exampleListFull) {
					if (item.getCompany().toLowerCase().contains(filterPattern) ||
						item.getFactory().toLowerCase().contains(filterPattern) ||
						item.getId().toLowerCase().contains(filterPattern) ||
						item.getLocal().toLowerCase().contains(filterPattern) ||
						item.getModel().toLowerCase().contains(filterPattern) ||
						item.getType().toLowerCase().contains(filterPattern) ||
						item.getStatus().toLowerCase().contains(filterPattern) ||
						item.getSystem().toLowerCase().contains(filterPattern)
					) {
						filteredList.add(item);
					}
				}
			}

			FilterResults results = new FilterResults();
			results.values = filteredList;

			return results;
		}

		@SuppressWarnings("unchecked")
		@SuppressLint("NotifyDataSetChanged")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			listEquipment.clear();
			listEquipment.addAll((List<Equipment>)results.values);
			notifyDataSetChanged();
		}
	};

}
