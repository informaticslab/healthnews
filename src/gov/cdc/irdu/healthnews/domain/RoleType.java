package gov.cdc.irdu.healthnews.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class RoleType extends DomainObject {

	private static final long serialVersionUID = -333793557645634534L;

	@NotNull
    @Size(max = 50)
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
