package Concordia.domain;

import java.io.Serializable;

/*---------------------------------------------------------------------------------------
 * The 'User' class is used to hold details for said users of the system 
 ---------------------------------------------------------------------------------------*/

public class User implements Serializable{
	private int userId,companyId;
	private String username,password;

	public User(int userId,int companyId,String username,String password){
		this.userId=userId;
		this.companyId=companyId;
		this.password=password;
	}

	public void ListUsers(){
		// ...
	}

	public void changePassw(){
		// ...
	}

	public void login(){
		// ...
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
}
