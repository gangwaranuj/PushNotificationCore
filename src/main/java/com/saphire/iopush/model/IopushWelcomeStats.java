package com.saphire.iopush.model;

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

@Entity
@Table(name="iopush_welcome_stats" ,schema="public"
, uniqueConstraints = @UniqueConstraint(columnNames={"welcome_name", "date","fk_welcome_id"}))
public class IopushWelcomeStats {

	private int welcomeStatsId;
	private String welcomeName;
	private Date date;
	private int sent;
	private int click;
	private int productid;
	private int open;
	private IopushWelcome iopushWelcome;






	/**
	 * @return the iopushWelcome
	 */
	  
	public IopushWelcomeStats() {
		// TODO Auto-generated constructor stub
	}


	public IopushWelcomeStats(int welcomeStatsId,IopushWelcome iopushWelcome, String welcomeName, Date date, int sent, int click, int open) {
		super();
		this.welcomeStatsId = welcomeStatsId;
		this.date = date;
		this.sent = sent;
		this.click = click;
		this.open = open;
		this.iopushWelcome =  iopushWelcome;
	}

	
	
	@Id
	@SequenceGenerator(name="iopush_welcome_stats_seq", sequenceName="iopush_welcome_stats_seq",allocationSize=1)
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_welcome_stats_seq")
	@Column(name="welcome_stats_id", unique = true, nullable = false,columnDefinition = "serial")
	public int getWelcomeStatsId() {
		return welcomeStatsId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="date",length = 30)
	public Date getDate() {
		return date;
	}

	@Column(name="sent", columnDefinition = "int default 0" )
	public int getSent() {
		return sent;
	}

	@Column(name="click", columnDefinition = "int default 0")
	public int getClick() {
		return click;
	}

	
	/**
	 * @return the open
	 */
	@Column(name="open", columnDefinition = "int default 0" )
	public int getOpen() {
		return open;
	}


	/**
	 * @param open the open to set
	 */
	
	public void setOpen(int open) {
		this.open = open;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public void setSent(int sent) {
		this.sent = sent;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	    @JoinColumn(name="fk_welcome_id", nullable=false)
	 public IopushWelcome getIopushWelcome() {
	  return iopushWelcome;
	 }


	 /**
	  * @param iopushwelcome the iopushwelcome to set
	  */
	 public void setIopushWelcome(IopushWelcome iopushWelcome) {
	  this.iopushWelcome = iopushWelcome;
	 }

	public void setClick(int click) {
		this.click = click;
	}

	@Column(name="productId")
	public int getProductid() {
		return productid;
	}


	public void setProductid(int productid) {
		this.productid = productid;
	}



	public void setWelcomeStatsId(int welcomeStatsId) {
		this.welcomeStatsId = welcomeStatsId;
	}

	@Column(name="welcome_name",length = 150)
	public String getWelcomeName() {
		return welcomeName;
	}


	public void setWelcomeName(String welcomeName) {
		this.welcomeName = welcomeName;
	}
	
	
}
