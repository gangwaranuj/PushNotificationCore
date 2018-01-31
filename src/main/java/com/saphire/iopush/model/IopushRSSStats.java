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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="iopush_rss_stats" ,schema="public"
, uniqueConstraints = @UniqueConstraint(columnNames={"rss_name", "date"}))
public class IopushRSSStats {

	private int IopushRSSStatsId;
	private String rssName;
	private Date date;
	private String rssURL;
	private int sent;
	private int click;
	private int pid;
	
	@Column(name="productId")
	public int getPid() {
		return pid;
	}


	public void setPid(int pid) {
		this.pid = pid;
	}


	public IopushRSSStats() {
		// TODO Auto-generated constructor stub
	}


	public IopushRSSStats(int iopushRSSStatsId, String rssName, Date date, String rssURL, int sent, int click) {
		super();
		IopushRSSStatsId = iopushRSSStatsId;
		this.rssName = rssName;
		this.date = date;
		this.rssURL = rssURL;
		this.sent = sent;
		this.click = click;
	}

	@Id
	@SequenceGenerator(name="iopush_rss_stats_seq", sequenceName="iopush_rss_stats_seq",allocationSize=1)
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_rss_stats_seq")
	@Column(name="rss_stats_id", unique = true, nullable = false,columnDefinition = "serial")
	public int getIopushRSSStatsId() {
		return IopushRSSStatsId;
	}


	@Column(name="rss_name",length = 150)
	public String getRssName() {
		return rssName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="date",length = 30)
	public Date getDate() {
		return date;
	}

	@Column(name="rss_url",length = 200)
	public String getRssURL() {
		return rssURL;
	}

	@Column(name="sent", columnDefinition = "int default 0" )
	public int getSent() {
		return sent;
	}

	@Column(name="click", columnDefinition = "int default 0")
	public int getClick() {
		return click;
	}


	public void setIopushRSSStatsId(int iopushRSSStatsId) {
		IopushRSSStatsId = iopushRSSStatsId;
	}


	public void setRssName(String rssName) {
		this.rssName = rssName;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public void setRssURL(String rssURL) {
		this.rssURL = rssURL;
	}


	public void setSent(int sent) {
		this.sent = sent;
	}


	public void setClick(int click) {
		this.click = click;
	}
	
	
}
