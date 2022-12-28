package project;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String usersRequest = "hello";
		String id = usersRequest.substring(0,usersRequest.length()-1);

		System.out.println(usersRequest.substring(usersRequest.length()-1));
		System.out.println(usersRequest.substring(0,usersRequest.length()-1));

	}

}
