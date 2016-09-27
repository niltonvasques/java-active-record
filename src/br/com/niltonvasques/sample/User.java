package br.com.niltonvasques.sample;

import java.util.List;


public class User extends Model<User>{

	public String name;
	public String email;
	public String password;
	
	@Override
	protected User newInstance() {
		return new User();
	}

	@Override
	protected Class<User> getType() {
		return User.class;
	}
	
	public List<Task> getTasks(){
		return new Task().where("user_id = ?", id);
	}

}
