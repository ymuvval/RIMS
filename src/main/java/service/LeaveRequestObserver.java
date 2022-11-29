package service;

public class LeaveRequestObserver extends NotificationObserver {
	
	@Override
	public void Notify() {
		System.out.println("Notify Manager with ID -->  " + this.getUser().getId());
	}
	
}
