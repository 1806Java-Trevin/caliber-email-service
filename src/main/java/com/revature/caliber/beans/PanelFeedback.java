package com.revature.caliber.beans;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Notes and results for each category that the panelist
 * goes over with the associate.
 * 
 * @author Patrick Walsh
 *
 */
@Entity
@Table(name = "CALIBER_PANEL_FEEDBACK")
//@Cacheable
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class PanelFeedback {

	@Id
	@Column(name = "PANEL_FEEDBACK_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PANEL_FEEDBACK_ID_SEQUENCE")
	@SequenceGenerator(name = "PANEL_FEEDBACK_ID_SEQUENCE", sequenceName = "PANEL_FEEDBACK_ID_SEQUENCE")
	private long id;
	
	@NotNull
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	@JoinColumn(name="CATEGORY_ID", nullable=false)
	private Category technology;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "PANEL_STATUS", nullable = false)
	private PanelStatus status;
	
	@Min(0)
	@Max(10)
	@NotNull
	@Column(name = "PANEL_RESULT")
	private int result;
	
	@Column(name = "PANELIST_COMMENTS")
	private String comment;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="PANEL_ID", nullable=false)
	@JsonBackReference(value = "feedback")
	private Panel panel;

	public PanelFeedback() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Category getTechnology() {
		return technology;
	}

	public void setTechnology(Category technology) {
		this.technology = technology;
	}

	public PanelStatus getStatus() {
		return status;
	}

	public void setStatus(PanelStatus status) {
		this.status = status;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Panel getPanel() {
		return panel;
	}

	public void setPanel(Panel panel) {
		this.panel = panel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hashCodeResult = 1;
		hashCodeResult = prime * hashCodeResult + ((comment == null) ? 0 : comment.hashCode());
		hashCodeResult = prime * hashCodeResult + ((panel == null) ? 0 : panel.hashCode());
		hashCodeResult = prime * hashCodeResult + this.result;
		hashCodeResult = prime * hashCodeResult + ((status == null) ? 0 : status.hashCode());
		hashCodeResult = prime * hashCodeResult + ((technology == null) ? 0 : technology.hashCode());
		return hashCodeResult;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PanelFeedback other = (PanelFeedback) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (panel == null) {
			if (other.panel != null)
				return false;
		} 
		if (result != other.result)
			return false;
		if (status != other.status)
			return false;
		if (technology == null) {
			if (other.technology != null)
				return false;
		} else if (!technology.equals(other.technology))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PanelFeedback [id=" + id + ", technology=" + technology + ", status=" + status + ", result=" + result
				+ ", comment=" + comment + "]";
	}
	
}
