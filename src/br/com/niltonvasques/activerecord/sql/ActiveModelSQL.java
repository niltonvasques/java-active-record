package br.com.niltonvasques.activerecord.sql;

import java.util.List;

import br.com.niltonvasques.activerecord.ActiveModelBase;

public abstract class ActiveModelSQL <T extends ActiveModelSQL> extends ActiveModelBase<T>{

	private SQLReflect reflect = new SQLReflect(this);
		
	@Override
	protected boolean insert() {
		return delegateRawSQLToDatabase(reflect.insert());
	}

	@Override
	protected boolean update() {
		return delegateRawSQLToDatabase(reflect.update());
	}
	
	@Override
	public List<T> where(String whereClause, Object... args){
		for(int i = 0; i < args.length; i++){
			whereClause = whereClause.replaceFirst("\\?", reflect.castValue(args[i]).toString());
		}
		String sql = "SELECT * FROM "+getType().getSimpleName()+" WHERE "+whereClause;
		System.out.println(sql);
		return delegateQueryToDatabase(sql, args);
	}	
	
	protected abstract List<T> delegateQueryToDatabase(String sql, Object... args);
	protected abstract boolean delegateRawSQLToDatabase(String sql);

}
