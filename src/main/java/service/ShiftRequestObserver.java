package service;

public class ShiftRequestObserver extends NotificationObserver {
	
	@Override
	public void Notify() {
		System.out.println("Notify Manager with ID --> " + this.getUser().getId());
	}

}
