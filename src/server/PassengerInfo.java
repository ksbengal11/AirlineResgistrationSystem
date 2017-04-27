package server;

class PassengerInfo {
	private String firstname;
	private String lastname;
	
	PassengerInfo(String fn, String ln){
		this.firstname = fn;
		this.lastname = ln;
	}
	
	public String getFirstname() {
		return firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String toString(){
		return this.getFirstname() + " " + this.getLastname();
	}
	
}