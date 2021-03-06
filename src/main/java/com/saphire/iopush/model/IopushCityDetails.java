package com.saphire.iopush.model;
// Generated 24 Jan, 2017 10:27:42 PM by Hibernate Tools 3.4.0.CR1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * IopushCityDetails generated by hbm2java
 */
@Entity
@Table(name="iopush_city_details"
    ,schema="public"
)
public class IopushCityDetails  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cityId;
     private IopushGeoDetails iopushGeoDetails;
     private String cityCode;
     private Double cityLatitude;
     private Double cityLongititude;
     private String cityName;
     private String createdBy;
     private Date creationDate;
     private Date modificationDate;
     private String modificedBy;
     private Set<IopushSubscribers> iopushSubscriberses = new HashSet<IopushSubscribers>(0);

    public IopushCityDetails() {
    }

	
    public IopushCityDetails(int cityId, IopushGeoDetails iopushGeoDetails, Date modificationDate) {
        this.cityId = cityId;
        this.iopushGeoDetails = iopushGeoDetails;
        this.modificationDate = modificationDate;
    }
    public IopushCityDetails(int cityId, IopushGeoDetails iopushGeoDetails, String cityCode, Double cityLatitude, Double cityLongititude, String cityName, String createdBy, Date creationDate, Date modificationDate, String modificedBy, Set<IopushSubscribers> iopushSubscriberses) {
       this.cityId = cityId;
       this.iopushGeoDetails = iopushGeoDetails;
       this.cityCode = cityCode;
       this.cityLatitude = cityLatitude;
       this.cityLongititude = cityLongititude;
       this.cityName = cityName;
       this.createdBy = createdBy;
       this.creationDate = creationDate;
       this.modificationDate = modificationDate;
       this.modificedBy = modificedBy;
       this.iopushSubscriberses = iopushSubscriberses;
    }
   
    @Id   
    @SequenceGenerator(name="iopush_city_details_seq", sequenceName="iopush_city_details_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_city_details_seq")
    @Column(name = "city_id", unique = true, nullable = false,columnDefinition = "serial")
    public int getCityId() {
        return this.cityId;
    }
    
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fk_geo_id", nullable=false)
    public IopushGeoDetails getIopushGeoDetails() {
        return this.iopushGeoDetails;
    }
    
    public void setIopushGeoDetails(IopushGeoDetails iopushGeoDetails) {
        this.iopushGeoDetails = iopushGeoDetails;
    }

    
    @Column(name="city_code", length=10)
    public String getCityCode() {
        return this.cityCode;
    }
    
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    
    @Column(name="city_latitude", precision=17, scale=17)
    public Double getCityLatitude() {
        return this.cityLatitude;
    }
    
    public void setCityLatitude(Double cityLatitude) {
        this.cityLatitude = cityLatitude;
    }

    
    @Column(name="city_longititude", precision=17, scale=17)
    public Double getCityLongititude() {
        return this.cityLongititude;
    }
    
    public void setCityLongititude(Double cityLongititude) {
        this.cityLongititude = cityLongititude;
    }

    
    @Column(name="city_name", length=50)
    public String getCityName() {
        return this.cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    
    @Column(name="created_by", length=50)
    public String getCreatedBy() {
        return this.createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="creation_date", length=29)
    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modification_date", nullable=false, length=29)
    public Date getModificationDate() {
        return this.modificationDate;
    }
    
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    
    @Column(name="modificed_by", length=50)
    public String getModificedBy() {
        return this.modificedBy;
    }
    
    public void setModificedBy(String modificedBy) {
        this.modificedBy = modificedBy;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="iopushCityDetails")
    public Set<IopushSubscribers> getIopushSubscriberses() {
        return this.iopushSubscriberses;
    }
    
    public void setIopushSubscriberses(Set<IopushSubscribers> iopushSubscriberses) {
        this.iopushSubscriberses = iopushSubscriberses;
    }




}


