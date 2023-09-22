package com.bz.bzinventory.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bz.bzinventory.R;

public class ItemEquipmentViewHolder extends RecyclerView.ViewHolder
{
	public ImageView imageView;
	public TextView equipmentId;
	public TextView equipmentName;
	public TextView equipmentType;
	public TextView equipmentCompany;
	public View view;

	public ItemEquipmentViewHolder(View view){
		super(view);

		equipmentId = view.findViewById(R.id.nama_barang);
		equipmentName = view.findViewById(R.id.merk_barang);
		equipmentType = view.findViewById(R.id.harga_barang);
		equipmentCompany = view.findViewById(R.id.company_teste);
		imageView = view.findViewById(R.id.imageView_logo);

		this.view = view;
	}
}
