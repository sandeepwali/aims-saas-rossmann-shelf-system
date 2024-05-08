package com.solumesl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "shelf", indexes = {@Index(name = "shelf_idx", columnList = "store, module, shelf, rowno", unique = true)})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shelf implements Serializable {
	private static final long serialVersionUID = 2L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(hidden = true)
	private Long id;
	
	@Column(name = "store", nullable = false, length = 10)
	private String store;
	
	@Column(name = "module", nullable = false, length = 10)
	private String module;
	
	@Column(name = "shelf", nullable = false, length = 10)
	private String shelf;
	
	@Column(name = "rowno", nullable = false, length = 10)
	private Integer rowno;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdate", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Berlin")
	@Schema(hidden = true)
	private Date createdate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedate", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Berlin")
	@Schema(hidden = true)
	private Date updatedate;
	
	@OneToMany(mappedBy = "shelf", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
	@OrderBy("orderno ASC")
	@JsonManagedReference
	private Set<Shelfitem> shelfitem = new HashSet();

	public Shelf() {
	}

	public Shelf(String module, String shelf, Integer rowno) {
		this.module = module;
		this.shelf = shelf;
		this.rowno = rowno;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}
	
	

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getModule() {
		return this.module;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public String getShelf() {
		return this.shelf;
	}

	public Integer getRowno() {
		return this.rowno;
	}

	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}

	public void setShelfitem(Set<Shelfitem> shelfitem) {
		this.shelfitem = shelfitem;
	}

	public Set<Shelfitem> getShelfitem() {
		return this.shelfitem;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public Date getUpdatedate() {
		return this.updatedate;
	}
}