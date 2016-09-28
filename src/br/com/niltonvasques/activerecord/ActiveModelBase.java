package br.com.niltonvasques.activerecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class ActiveModelBase<T extends ActiveModelBase> {
	
	public int id;
	public long createdAt;
	public long updatedAt;
	public boolean active = true;
		
	public List<T> all(){
		return where("active = 1");
	}

	public T find(int id){
		return where("id = ?", id).get(0);
	}
	
	public boolean exists(int id){
		return where("id = ?", id).size() > 0;
	}
	
	public boolean save(){
		updatedAt = System.currentTimeMillis();
		if(isNew()) {
			createdAt = System.currentTimeMillis();
			return insert();
		}
		return update();
	}
	
	public void eagerLoad(){
		List<Field> assoc = getAssociations();
		for(int i = 0; i < assoc.size(); i++){
			try {
				load(assoc.get(0).getName().replaceAll("_id$", ""));
			} catch(Exception e) {
				e.printStackTrace();
			}			
		}
	}

	public boolean isNew(){
		return id == 0;
	}
	
	protected abstract T newInstance();

	protected abstract Class<T> getType();

	protected abstract boolean insert();

	protected abstract boolean update();

	public abstract List<T> where(String whereClause, Object... args);

	public List<Field> getAssociations(){
		List<Field> associationsFields = new ArrayList<Field>();
		Field[] fields = getType().getFields();
		for(int i = 0; i < fields.length; i++){
			Field f = fields[i];
			if(f.getName().matches(".*_id$"))
				associationsFields.add(f);
		}
		return associationsFields;
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
	

	public List<Field> getFieldsWithoutId(){
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

	public boolean isValidType(Class<?> t){
		return (t == String.class || t == boolean.class || t == int.class || t == float.class || t == double.class 
				|| t == long.class);
	}

	private void load(String name){
		try {
			Field idField = this.getType().getField(name+"_id");			
			Field field = this.getType().getDeclaredField(name);
			if(idField.getInt(this) == 0) return;
			Object obj = ((ActiveModelBase)field.getType().newInstance()).find(idField.getInt(this));
			field.setAccessible(true);
			field.set(this, obj);
		} catch (InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
