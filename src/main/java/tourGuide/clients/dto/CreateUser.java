package tourGuide.clients.dto;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateUser {
	private final String userName;
	private String phoneNumber;
	private String emailAddress;

	public CreateUser(String userName) {
		this.userName = userName;
	}

	public CreateUser(String userName, String phoneNumber, String emailAddress) {
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}
}
