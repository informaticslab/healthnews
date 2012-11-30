/**
 * 
 */
package gov.cdc.irdu.healthnews.shared;


/**
 *
 * @author Joel M. Rives
 * Apr 26, 2011
 */
public class PersonDTO extends BaseDTO {

	private static final long serialVersionUID = -1701874097194130617L;

	private String givenName;
	private String familyName;
    private String username;
	
	public PersonDTO() { }
	
	public PersonDTO(Long id) {
		setId(id);
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
