package service;

public class ShiftApprovalObserver extends NotificationObserver {

	@Override
	public void Notify() {
		System.out.println("Notify Employee with ID --> " + this.getUser().getId());
	}

}
