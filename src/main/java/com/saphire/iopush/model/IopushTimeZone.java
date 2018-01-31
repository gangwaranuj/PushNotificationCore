package com.saphire.iopush.model;
// Generated 12 Sep, 2016 6:25:54 PM by Hibernate Tools 5.1.0.Alpha1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



/**
 * IcmsNewtab generated by hbm2java
 */
@Entity
@Table(name = "iopush_timezone")
public class IopushTimeZone implements java.io.Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int timezoneID;
	private String timezone;
	private int countryId;
	private String country;
	private Set<IopushCampaign> iopushCampaigns = new HashSet<IopushCampaign>(0);

	public IopushTimeZone() {
	}

	public IopushTimeZone(int timezoneID) {
		this.timezoneID = timezoneID;
	}

	

	 /**
	 * @param countryId
	 * @param timezone
	 */
	public IopushTimeZone(int countryId,String country, String timezone) {
		this.countryId = countryId;
		this.country=country;
		this.timezone = timezone;
	}

//	@Id
//	   @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
//	           @org.hibernate.annotations.Parameter(name = "sequenceName", value = "sequence"),
//	           @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
//	   })
//	@GeneratedValue(generator="sequence",strategy = GenerationType.SEQUENCE)
//	   @Column(name="timezone_id", unique=true, nullable=false,columnDefinition = "serial")
	
	   
		 @Id   
		  @SequenceGenerator(name="iopush_timezone_seq", sequenceName="iopush_timezone_seq",allocationSize=1)
			@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_timezone_seq")
			@Column(name = "timezone_id", unique = true, nullable = false,columnDefinition = "serial")
	
	public int getTimezoneID() {
		return this.timezoneID;
	}

	public void setTimezoneID(int timezoneID) {
		this.timezoneID = timezoneID;
	}

	

	@Column(name = "time_zone", length = 50)
	public String getTimezone() {
		return this.timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	
	/**
	 * @return the countryId
	 */
	@Column(name = "country_id", length = 3)
	public int getCountryId() {
		return countryId;
	}

	/**
	 * @return the country
	 */
	@Column(name = "country", length = 100)
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @param countryId the countryId to set
	 */
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "iopushTimeZone")
	public Set<IopushCampaign> getIopushCampaigns() {
		return this.iopushCampaigns;
	}

	public void setIopushCampaigns(Set<IopushCampaign> iopushCampaigns) {
		this.iopushCampaigns = iopushCampaigns;
	}	
	
}
