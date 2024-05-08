package com.solumesl.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "shelfitem", indexes = {
		@Index(name = "shelfitem_idx", columnList = "articleno, length, orderno", unique = false),
		@Index(name = "shelfitem_article_no_idx", columnList = "articleno", unique = false)})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shelfitem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(hidden = true)
	private Long id;
	
	@Column(name = "orderno", nullable = false, length = 10)
	private Integer orderno;
	
	@Column(name = "articleno", nullable = false, length = 10)
	private String articleno;
	
	@Column(name = "length", length = 10)
	private String length;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "itemcreatedate", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Berlin")
	@Schema(hidden = true)
	private Date itemcreatedate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "itemupdatedate", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Berlin")
	@Schema(hidden = true)
	private Date itemupdatedate;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "shelf_id", nullable = false)
	@JsonBackReference
	@Schema(hidden = true)
	private Shelf shelf;

	public Shelfitem() {
	}

	public Shelfitem(Integer order_no, String article_no, String length) {
		this.orderno = this.orderno;
		this.articleno = this.articleno;
		this.length = length;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}

	public void setArticleno(String articleno) {
		this.articleno = articleno;
	}

	public String getArticleno() {
		return this.articleno;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getLength() {
		return this.length;
	}

	public void setShelf(Shelf shelf) {
		this.shelf = shelf;
	}

	public Shelf getShelf() {
		return this.shelf;
	}

	public Date getItemcreatedate() {
		return this.itemcreatedate;
	}

	public Date getItemupdatedate() {
		return this.itemupdatedate;
	}
}