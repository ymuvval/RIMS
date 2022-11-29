package service;

public class LeaveApprovalObserver extends NotificationObserver {

	@Override
	public void Notify() {
		System.out.println("Notify Employee with ID --> " + this.getUser().getId());
	}

}
