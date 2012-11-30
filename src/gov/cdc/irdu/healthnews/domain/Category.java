/**
 * 
 */
package gov.cdc.irdu.healthnews.domain;

import gov.cdc.irdu.healthnews.shared.CategoryDTO;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

/**
 * This persistent class contains configuration information about a 
 * search category.
 * 
 * @author Joel M. Rives
 * Apr 26, 2011
 */

@Entity
public class Category extends DomainObject {

	private static final long serialVersionUID = 833696387631752747L;

	@ManyToOne
	private Person owner;
	
	private Integer ordering;
	
	@Size(max=50)
	private String title;
	
	private String queryTerms;
	
	private String excludeTerms;
	
	@Size(max=7)
	private String brightColor;
	
	@Size(max=7)
	private String mediumColor;
	
	@Size(max=7)
	private String darkColor;
	
	public Category() { }
	
	public Category(CategoryDTO dto) {
		setId(dto.getId());
		this.title = dto.getTitle();
		this.queryTerms = dto.getQueryTerms();
		this.excludeTerms = dto.getExcludeTerms();
		this.brightColor = dto.getBrightColor();
		this.mediumColor = dto.getMediumColor();
		this.darkColor = dto.getDarkColor();
	}
	
	
	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQueryTerms() {
		return queryTerms;
	}

	public void setQueryTerms(String queryTerms) {
		this.queryTerms = queryTerms;
	}

	public String getExcludeTerms() {
		return excludeTerms;
	}

	public void setExcludeTerms(String excludeTerms) {
		this.excludeTerms = excludeTerms;
	}

	public String getBrightColor() {
		return brightColor;
	}

	public void setBrightColor(String brightColor) {
		this.brightColor = brightColor;
	}

	public String getMediumColor() {
		return mediumColor;
	}

	public void setMediumColor(String mediumColor) {
		this.mediumColor = mediumColor;
	}

	public String getDarkColor() {
		return darkColor;
	}

	public void setDarkColor(String darkColor) {
		this.darkColor = darkColor;
	}

	public CategoryDTO toDTO() {
		CategoryDTO dto = new CategoryDTO(getId());
		
		dto.setOrdering(ordering);
		dto.setTitle(title);
		dto.setQueryTerms(queryTerms);
		dto.setExcludeTerms(excludeTerms);
		dto.setBrightColor(brightColor);
		dto.setMediumColor(mediumColor);
		dto.setDarkColor(darkColor);
		
		return dto;
	}
	
}
