/**
 * 
 */
package bookingApp;


/**
 * A class which represents an Admin and stores the admin's personal info.
 * @author Brian Ho, Raunak Mathur, Jack Hu, Ronnie Huang
 *
 */
public class Admin extends Client{
	
	/**
	 * Stores the generated ID for serialization.
	 */
	private static final long serialVersionUID = -2597015818758919103L;
	private String password;
	

	/**
	 * Creates a new Admin with the given last name, first name, email, 
	 * address, credit card number and credit card expiry date.
	 * @param lastName Last name of the Admin.
	 * @param firstName First name of the Admin.
	 * @param email Email address of the Admin.
	 * @param address Home address of the Admin.
	 * @param ccNumber Credit card number of the Admin.
	 * @param expiryDate Credit card expiry date of the Admin.
	 */
	public Admin(String lastName, String firstName, String email,
			String address, String ccNumber, String expiryDate) {
		super(lastName, firstName, email, address, ccNumber, expiryDate);
	}
}
