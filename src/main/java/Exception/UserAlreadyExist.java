package Exception;

public class UserAlreadyExist extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UserAlreadyExist(String str) {
		super(str);
	}
}
