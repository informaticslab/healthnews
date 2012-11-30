/**
 * 
 */
package gov.cdc.irdu.healthnews.shared;

/**
 *
 * @author Joel M. Rives
 * Apr 26, 2011
 */
public class CategoryDTO extends BaseDTO {

	private static final long serialVersionUID = -1701874097194130617L;

	private Integer ordering;
	private String title;
	private String queryTerms;
	private String excludeTerms;
	private String brightColor;
	private String mediumColor;
	private String darkColor;
	
	public CategoryDTO() { }
	
	public CategoryDTO(Long id) {
		setId(id);
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

	
}
