package main;

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
		if(user_id != 0 && user == null)
			user = new User().find(user_id);
		return user;
	}

}
