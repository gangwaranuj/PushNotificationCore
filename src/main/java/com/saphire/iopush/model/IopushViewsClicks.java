package com.saphire.iopush.model;
// Generated 24 Jan, 2017 10:27:42 PM by Hibernate Tools 3.4.0.CR1


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * IopushViewsClicks generated by hbm2java
 */
@Entity
@Table(name="iopush_views_clicks"
,schema="public", uniqueConstraints = @UniqueConstraint(columnNames={"fk_campaign_id", "date"})
		)
public class IopushViewsClicks  implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int viewsclicksId;
	private IopushCampaign iopushCampaign;
	private int clickHits;
	private int viewHits;
	private Date date;
	private IopushProduct productID;

	public IopushViewsClicks() {
	}

	public IopushViewsClicks(int viewsclicksId, IopushCampaign iopushCampaign, int clickHits, int viewHits, Date date) {
		this.viewsclicksId = viewsclicksId;
		this.iopushCampaign = iopushCampaign;
		this.clickHits = clickHits;
		this.viewHits = viewHits;
		this.date = date;
	}

	@Id   
	@SequenceGenerator(name="iopush_views_clicks_seq", sequenceName="iopush_views_clicks_seq",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_views_clicks_seq")
	@Column(name = "views_clicks_id", unique = true, nullable = false,columnDefinition = "serial")
	public int getViewsclicksId() {
		return this.viewsclicksId;
	}

	public void setViewsclicksId(int viewsclicksId) {
		this.viewsclicksId = viewsclicksId;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fk_campaign_id", nullable=false)
	public IopushCampaign getIopushCampaign() {
		return this.iopushCampaign;
	}

	public void setIopushCampaign(IopushCampaign iopushCampaign) {
		this.iopushCampaign = iopushCampaign;
	}


	@Column(name="click_hits", nullable=false)
	public int getClickHits() {
		return this.clickHits;
	}

	public void setClickHits(int clickHits) {
		this.clickHits = clickHits;
	}


	@Column(name="view_hits", nullable=false)
	public int getViewHits() {
		return this.viewHits;
	}

	public void setViewHits(int viewHits) {
		this.viewHits = viewHits;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date", nullable=false, length=13)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fk_product_id", nullable=false)
	public IopushProduct getProductID() {
		return productID;
	}

	public void setProductID(IopushProduct productID) {
		this.productID = productID;
	}

	
}


