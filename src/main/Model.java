package main;

import java.util.ArrayList;
import java.util.List;

import br.com.niltonvasques.activerecord.ActiveModelBase;
import br.com.niltonvasques.activerecord.sql.ActiveModelSQL;

public abstract class Model <T extends Model> extends ActiveModelSQL<T> {

	@Override
	final protected List<T> delegateQueryToDatabase(String sql, Object... args) {
		List list = new ArrayList<>();
		list.add(newInstance());
		list.add(newInstance());
		return list;
	}
	
	final protected boolean delegateRawSQLToDatabase(String sql){
		return true;
	}
}
