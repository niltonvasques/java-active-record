package br.com.niltonvasques.sample;

import java.util.List;

public class Main {

	public static void main(String args[]){
		//Fetching all
		List<User> users = new User().all();
		System.out.println(users);
		
		//Find by id
		System.out.println(new User().find(10));
		
		//Saving
		User user = new User();
		System.out.println(user.save());
		
		// Eager loading associations
		User user2 = new User();
		user2.id = 10;
		user2.eagerLoad();		
		System.out.println(user2.save());
		
		// Queries
		System.out.println(new User().where("name = ?", "João"));
		
		// Get associations
		System.out.println(new User().getTasks());
		
		// Eager loading associations
		Task t = new Task();
		t.user_id = 1;
		t.eagerLoad();		
		System.out.println(t.getUser());
	}
}
