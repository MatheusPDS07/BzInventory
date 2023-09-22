package com.bz.bzinventory.objects;

import com.google.firebase.database.Exclude;

/**This class is an object of an equipment*/
public class Equipment {

	/**attributes*/
	@Exclude
	private String key;
	private String id;
	private String name;
	private String type;
	private String System;
	private String Factory;
	private String Model;
	private String Status;
	private String Local;
	private String company;

	/**empty constructor needed for runtime*/
	public Equipment() {}

	/**Getters and setters of equipment*/
	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSystem() {
		return System;
	}

	public void setSystem(String system) {
		System = system;
	}

	public String getFactory() {
		return Factory;
	}

	public void setFactory(String factory) {
		Factory = factory;
	}

	public String getModel() {
		return Model;
	}

	public void setModel(String model) {
		Model = model;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getLocal() {
		return Local;
	}

	public void setLocal(String local) {
		Local = local;
	}

}
