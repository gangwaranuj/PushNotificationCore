package com.saphire.iopush.bean;

public class RSSFeedResponseBean {
	String articleid;
    String title;
    String description;
    String link;
    String pubDate;
    String guid;
    String content;
    String thumbnail;
    String enclosure;
    int status;
    String result;
    String campaign;
   	String source;
   	String generic;
   	String name;
   	String url;
   	int productId;
    
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the articleid
	 */
	public String getArticleid() {
		return articleid;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @return the pubDate
	 */
	public String getPubDate() {
		return pubDate;
	}
	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @return the thumbnail
	 */
	public String getThumbnail() {
		return thumbnail;
	}
	/**
	 * @return the enclosure
	 */
	public String getEnclosure() {
		return enclosure;
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
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the campaign
	 */
	public String getCampaign() {
		return campaign;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @return the generic
	 */
	public String getGeneric() {
		return generic;
	}
	/**
	 * @param campaign the campaign to set
	 */
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @param generic the generic to set
	 */
	public void setGeneric(String generic) {
		this.generic = generic;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RSSFeedResponseBean [articleid=" + articleid + ", title=" + title + ", description=" + description
				+ ", link=" + link + ", pubDate=" + pubDate + ", guid=" + guid + ", content=" + content + ", thumbnail="
				+ thumbnail + ", enclosure=" + enclosure + ", status=" + status + ", result=" + result + ", campaign="
				+ campaign + ", source=" + source + ", generic=" + generic + ", name=" + name + ", url=" + url + "]";
	}
	
	
    
}
