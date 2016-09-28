package br.com.niltonvasques.activerecord.sql;

import java.lang.reflect.Field;
import java.util.List;

import br.com.niltonvasques.activerecord.ActiveModelBase;

public class SQLReflect {
	private ActiveModelBase<?> object;
	
	public SQLReflect(ActiveModelBase<?> object) {
		this.object = object;
	}
	
	public String insert(){
		String sqlInsert = "INSERT INTO "+object.getClass().getSimpleName()+" ";
		sqlInsert += "(";
		List<Field> fields = object.getFieldsWithoutId();
		String values = "VALUES (";
		for(int i = 0; i < fields.size(); i++){
			if(i > 0) {
				sqlInsert += ", ";
				values += ", ";
			}
			Field f = fields.get(i);
			sqlInsert += f.getName();
			try {
				Object value = f.get(object);
				if(value != null && f.getType() == String.class) values += "'";
				values += castValue(value);
				if(value != null && f.getType() == String.class) values += "'";
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sqlInsert += ") "+values;
		sqlInsert += ");";
		System.err.println(sqlInsert);
		return sqlInsert;
	}
	
	public String update(){
		String sqlUpdate = "UPDATE "+object.getClass().getSimpleName()+" ";
		sqlUpdate += "SET ";
		List<Field> fields = object.getFieldsWithoutId();
		for(int i = 0; i < fields.size(); i++){
			if(i > 0) {
				sqlUpdate += ", ";
			}
			Field f = fields.get(i);
			try {
				sqlUpdate += f.getName()+"="+f.get(object);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sqlUpdate += " WHERE id = "+object.id+";";
		System.err.println(sqlUpdate);
		return sqlUpdate;
	}
	
	public Object castValue(Object value){
		if(value == null) return null;
		
//		System.out.println("castvalue: "+value+" class "+value.getClass());
		if(value.getClass() == Boolean.class){
			if(((boolean)value)) return 1;
			else return 0;
		}
		return value;
	}
}
