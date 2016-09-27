package br.com.niltonvasques.sample;

public class Task extends Model<Task>{
	
	public int user_id;
	public String description;
	public boolean done;
	
	private User user;
	
	@Override
	protected Task newInstance() {
		return new Task();
	}

	@Override
	protected Class<Task> getType() {
		return Task.class;
	}
	
	public User getUser(){
		return user;
	}
	
	public void setUser(User user){
		this.user = user;
		this.user_id = user.id;
	}

}
