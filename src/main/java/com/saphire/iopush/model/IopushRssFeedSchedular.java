package com.saphire.iopush.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="iopush_rssfeed_scheduler",schema="public")
public class IopushRssFeedSchedular implements java.io.Serializable{
	private int id;
	private int rssfeedid;
    private String articleid;
    private int productid;
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String guid;
    private String content;
    private String thumbnail;
    private String enclosure;
    private Date reqDate;
    
	/**
	 * 
	 */
	public IopushRssFeedSchedular() {
	}
	
	/**
	 * @param rssUrl
	 */
	

	/**
	 * @return the id
	 */
    @Id   
    @SequenceGenerator(name="iopush_rssfeed_scheduler_seq", sequenceName="iopush_rssfeed_scheduler_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_rssfeed_scheduler_seq")
    @Column(name = "id", unique = true, nullable = false,columnDefinition = "serial")
	public int getId() {
		return id;
	}
	
	

	/**
	 * @return the rssfeedid
	 */
    @Column(name="rssfeed_id", length=10)
	public int getRssfeedid() {
		return rssfeedid;
	}

	/**
	 * @return the productid
	 */
    @Column(name="product_id", length=10)
	public int getProductid() {
		return productid;
	}

	/**
	 * @param rssfeedid the rssfeedid to set
	 */
	public void setRssfeedid(int rssfeedid) {
		this.rssfeedid = rssfeedid;
	}

	/**
	 * @param productid the productid to set
	 */
	public void setProductid(int productid) {
		this.productid = productid;
	}

	/**
	 * @return the articleid
	 */
    @Column(name="article_id", length=200)
	public String getArticleid() {
		return articleid;
	}
	/**
	 * @return the title
	 */
    @Column(name="title", length=1000)
	public String getTitle() {
		return title;
	}
	/**
	 * @return the description
	 */
    @Column(name="description", length=1000)
	public String getDescription() {
		return description;
	}
	/**
	 * @return the link
	 */
    @Column(name="link", length=1000)
	public String getLink() {
		return link;
	}
	/**
	 * @return the pubDate
	 */
    @Column(name="pub_date", length=75)
	public String getPubDate() {
		return pubDate;
	}
	/**
	 * @return the guid
	 */
    @Column(name="guid", length=1000)
	public String getGuid() {
		return guid;
	}
	/**
	 * @return the content
	 */
    @Column(name="content", length=1000)
	public String getContent() {
		return content;
	}
	/**
	 * @return the thumbnail
	 */
    @Column(name="thumbnail", length=1000)
	public String getThumbnail() {
		return thumbnail;
	}
	/**
	 * @return the enclosure
	 */
    @Column(name="enclosure", length=1000)
	public String getEnclosure() {
		return enclosure;
	}
    
    
    
    
    
	/**
	 * @return the reqDate
	 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="req_date", nullable=false, length=29)
	public Date getReqDate() {
		return reqDate;
	}

	/**
	 * @param reqDate the reqDate to set
	 */
	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @param articleid the articleid to set
	 */
	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @param pubDate the pubDate to set
	 */
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	/**
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	/**
	 * @param enclosure the enclosure to set
	 */
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"IopushRssFeedSchedular [id=%s, rssfeedid=%s, articleid=%s, productid=%s, title=%s, description=%s, link=%s, pubDate=%s, guid=%s, content=%s, thumbnail=%s, enclosure=%s, reqDate=%s]",
				id, rssfeedid, articleid, productid, title, description, link, pubDate, guid, content, thumbnail,
				enclosure, reqDate);
	}
    
  
    
}
