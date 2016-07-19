package ass04;

public class TestSecureSystem {

	public static void main(String[] args) {

		SecureSystem sys = new SecureSystem(5);		
		boolean loggedIn = sys.login("abc123");
		System.out.print(loggedIn);
	}

}
