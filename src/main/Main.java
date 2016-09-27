package main;

import java.util.List;

public class Main {

	public static void main(String args[]){
		List<User> users = new User().getAll();
		System.out.println(users);
		System.out.println(new User().find(10));
		User user = new User();
		System.out.println(user.save());
		User user2 = new User();
		user2.id = 10;
		System.out.println(user2.save());
		
		System.out.println(new User().where("name = ?", "João"));
		
		System.out.println(new User().getTasks());
	}
}
