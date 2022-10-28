package service;

import model.User;

public interface SignUp {
	public void IsUserPresent(String name);
	public void CreateUser(User user);
}
