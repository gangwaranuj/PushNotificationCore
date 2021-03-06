package com.saphire.iopush.model;
// Generated 24 Jan, 2017 10:27:42 PM by Hibernate Tools 3.4.0.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * IopushActivesubscribers generated by hbm2java
 */
@Entity
@Table(name="iopush_rssfeed_activesubscribers"
    ,schema="public"
    , uniqueConstraints = @UniqueConstraint(columnNames={"iopush_token", "fk_article_id"}) 
)
public class IopushRssFeedActivesubscribers  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
     private Integer articleclick;
     private Integer articleclose;
     private Integer articleopen;
     private Integer articlesent;
     private int fkCityid;
     private int fkGeoid;
     private int fkPlatformid;
     private int status;
     
     private String iopushToken;
     private String fcmToken;
     private String fkarticleId;

    public IopushRssFeedActivesubscribers() {
    }

	
    public IopushRssFeedActivesubscribers(int id, int fkCityid, int fkGeoid, int fkPlatformid, int status, String fkarticleId) {
        this.id = id;
        this.fkCityid = fkCityid;
        this.fkGeoid = fkGeoid;
        this.fkPlatformid = fkPlatformid;
        this.status = status;
        this.fkarticleId = fkarticleId;
    }
    public IopushRssFeedActivesubscribers(int id, Integer articleclick, Integer articleclose, Integer articleopen, Integer articlesent, int fkCityid, int fkGeoid, int fkPlatformid, int status, String iopushToken, String fkarticleId) {
       this.id = id;
       this.articleclick = articleclick;
       this.articleclose = articleclose;
       this.articleopen = articleopen;
       this.articlesent = articlesent;
       this.fkCityid = fkCityid;
       this.fkGeoid = fkGeoid;
       this.fkPlatformid = fkPlatformid;
       this.status = status;
       this.iopushToken = iopushToken;
       this.fkarticleId = fkarticleId;
    }
   
    @Id   
    @SequenceGenerator(name="iopush_rssfeed_activesubscribers_seq", sequenceName="iopush_rssfeed_activesubscribers_seq",allocationSize=1)
 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_rssfeed_activesubscribers_seq")
 @Column(name = "id", unique = true, nullable = false,columnDefinition = "serial")
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }


	/**
	 * @return the articleclick
	 */
    @Column(name="article_click", columnDefinition= "int default 0")
	public Integer getArticleclick() {
		return articleclick;
	}


	/**
	 * @return the articleclose
	 */
    @Column(name="article_close", columnDefinition= "int default 0")
	public Integer getArticleclose() {
		return articleclose;
	}


	/**
	 * @return the articleopen
	 */
    @Column(name="article_open", columnDefinition= "int default 0")
	public Integer getArticleopen() {
		return articleopen;
	}


	/**
	 * @return the articlesent
	 */
    @Column(name="article_sent", columnDefinition= "int default 0")
	public Integer getArticlesent() {
		return articlesent;
	}


	/**
	 * @return the fkCityid
	 */
    @Column(name="fk_city_id")
	public int getFkCityid() {
		return fkCityid;
	}


	/**
	 * @return the fkGeoid
	 */
    @Column(name="fk_geo_id")
	public int getFkGeoid() {
		return fkGeoid;
	}


	/**
	 * @return the fkPlatformid
	 */
    @Column(name="fk_platform_id")
	public int getFkPlatformid() {
		return fkPlatformid;
	}


	/**
	 * @return the status
	 */
    @Column(name="status", columnDefinition= "int default 0")
	public int getStatus() {
		return status;
	}


	/**
	 * @return the iopushToken
	 */
    @Column(name="iopush_token")
	public String getIopushToken() {
		return iopushToken;
	}


	/**
	 * @return the fkarticleId
	 */
    @Column(name="fk_article_id",length=100)
	public String getFkarticleId() {
		return fkarticleId;
	}


	/**
	 * @param articleclick the articleclick to set
	 */
	public void setArticleclick(Integer articleclick) {
		this.articleclick = articleclick;
	}


	/**
	 * @param articleclose the articleclose to set
	 */
	public void setArticleclose(Integer articleclose) {
		this.articleclose = articleclose;
	}


	/**
	 * @param articleopen the articleopen to set
	 */
	public void setArticleopen(Integer articleopen) {
		this.articleopen = articleopen;
	}


	/**
	 * @param articlesent the articlesent to set
	 */
	public void setArticlesent(Integer articlesent) {
		this.articlesent = articlesent;
	}


	/**
	 * @param fkCityid the fkCityid to set
	 */
	public void setFkCityid(int fkCityid) {
		this.fkCityid = fkCityid;
	}


	/**
	 * @param fkGeoid the fkGeoid to set
	 */
	public void setFkGeoid(int fkGeoid) {
		this.fkGeoid = fkGeoid;
	}


	/**
	 * @param fkPlatformid the fkPlatformid to set
	 */
	public void setFkPlatformid(int fkPlatformid) {
		this.fkPlatformid = fkPlatformid;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}


	/**
	 * @param iopushToken the iopushToken to set
	 */
	public void setIopushToken(String iopushToken) {
		this.iopushToken = iopushToken;
	}


	/**
	 * @param fkarticleId the fkarticleId to set
	 */
	public void setFkarticleId(String fkarticleId) {
		this.fkarticleId = fkarticleId;
	}


	/**
	 * @return the fcmToken
	 */
	 @Column(name="fcm_token",length=255)
	public String getFcmToken() {
		return fcmToken;
	}


	/**
	 * @param fcmToken the fcmToken to set
	 */
	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

   

}


