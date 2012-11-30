package gov.cdc.irdu.healthnews.domain;

import gov.cdc.irdu.healthnews.shared.PersonDTO;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Person extends DomainObject {

	private static final long serialVersionUID = -1420787524719485936L;
	
	@NotNull
    @Size(max = 30)
	private String givenName;
	
	@NotNull
    @Size(max = 30)
	private String familyName;

	@NotNull
    @Size(min = 3, max = 30)
    private String username;

    @Size(max = 30)
    private String password;

    @NotNull
    private Boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<RoleType> roles;
    
    @OneToMany(mappedBy="owner")
    @OrderBy("ordering")
    private List<Category> categories;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<RoleType> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleType> roles) {
		this.roles = roles;
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

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public PersonDTO toDTO() {
		PersonDTO dto = new PersonDTO(getId());
		dto.setGivenName(givenName);
		dto.setFamilyName(familyName);
		dto.setUsername(username);
		
		return dto;
	}
    
}
