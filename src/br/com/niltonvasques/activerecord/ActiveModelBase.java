package br.com.niltonvasques.activerecord;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class ActiveModelBase<T extends ActiveModelBase> {
	
	public int id;
	public long createdAt;
	public long updatedAt;
	public boolean active;
		
	public List<T> getAll(){
		return where("active = 1");
	}
	
	public List<T> where(String sql, Object... args){
		return delegateQueryToDatabase(sql, args);
	}	

	public T find(int id){
		return where("id = ?", id).get(0);
	}
	
	public boolean exists(int id){
		return where("id = ?", id).size() > 0;
	}
	
	public boolean save(){
		if(isNew()) return insert();
		return update();
	}
	
	public boolean isNew(){
		return id == 0;
	}
	
	private boolean insert(){
		String sqlInsert = "INSERT INTO "+getType().getSimpleName()+" ";
		sqlInsert += "(";
		List<Field> fields = getFieldsWithoutId();
		String values = "VALUES (";
		for(int i = 0; i < fields.size(); i++){
			if(i > 0) {
				sqlInsert += ", ";
				values += ", ";
			}
			Field f = fields.get(i);
			sqlInsert += f.getName();
			try {
				values += f.get(this);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sqlInsert += ") "+values;
		sqlInsert += ");";
		System.err.println(sqlInsert);
		return delegateRawSQLToDatabase(sqlInsert);
	}
	
	private boolean update(){
		String sqlUpdate = "UPDATE "+getType().getSimpleName()+" ";
		sqlUpdate += "SET ";
		List<Field> fields = getFieldsWithoutId();
		for(int i = 0; i < fields.size(); i++){
			if(i > 0) {
				sqlUpdate += ", ";
			}
			Field f = fields.get(i);
			try {
				sqlUpdate += f.getName()+"="+f.get(this);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sqlUpdate += " WHERE id = "+id+";";
		System.err.println(sqlUpdate);
		return delegateRawSQLToDatabase(sqlUpdate);
	}
	

	@Override
	public String toString() {		
		String obj = "{ id: "+id;
		List<Field> fields = getFieldsWithoutId();
		for(int i = 0; i < fields.size(); i++){
			obj += ", ";
			Field f = fields.get(i);
			try {
				obj += f.getName()+": "+f.get(this);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		obj += " }";
		return obj;
	}
	
	private List<Field> getFieldsWithoutId(){
		List<Field> fieldsWithoutId = new ArrayList<Field>();
		Field[] fields = getType().getFields();
		for(int i = 0; i < fields.length; i++){
			Field f = fields[i];
			Class<?> t = f.getType();
			if(isValidType(t) && !f.getName().equals("id")){
				fieldsWithoutId.add(f);				
			}
		}
		return fieldsWithoutId;
	}
	
	private boolean isValidType(Class<?> t){
		return (t == String.class || t == boolean.class || t == int.class || t == float.class || t == double.class 
				|| t == long.class);
	}
	
	private List<Field> getAssociations(){
		List<Field> associationsFields = new ArrayList<Field>();
		Field[] fields = getType().getFields();
		for(int i = 0; i < fields.length; i++){
			Field f = fields[i];
			if(!f.getName().matches("_id$"))
				associationsFields.add(f);
		}
		return associationsFields;
	}
	
	public void populate(){
		List<Field> assoc = getAssociations();
		for(int i = 0; i < assoc.size(); i++){
			try {
				Object object = Class.forName(assoc.get(i).getName()).getConstructor(String.class).newInstance();
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}			
		}
	}
	
	protected abstract T newInstance();
	protected abstract Class<T> getType();
	protected abstract List<T> delegateQueryToDatabase(String sql, Object... args);
	protected abstract boolean delegateRawSQLToDatabase(String sql);
}
