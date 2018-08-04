package com.revature.caliber.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The type Assessment.
 */
@Entity
@Table(name = "CALIBER_ASSESSMENT")
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Assessment implements Serializable {

	private static final long serialVersionUID = 5030264218154828822L;
	
	@Id
	@Column(name = "ASSESSMENT_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASSESSMENT_ID_SEQUENCE")
	@SequenceGenerator(name = "ASSESSMENT_ID_SEQUENCE", sequenceName = "ASSESSMENT_ID_SEQUENCE")
	private long assessmentId;

	/**
	 * Trainer inputted title, can be anything to help identify this assessment
	 */
	@Column(name = "ASSESSMENT_TITLE")
	private String title;

	/**
	 * Batch ID reference
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "BATCH_ID", nullable = false)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Batch batch;

	/**
	 * Raw numerical score before calculations This value is the maximum number
	 * of points that can be earned on this assignment.
	 */
	@Min(value = 1)
	@Column(name = "RAW_SCORE", nullable = false)
	private int rawScore;

	/**
	 * Assessment type, e.g. LMS, Verbal
	 */
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "ASSESSMENT_TYPE", nullable = false)
	private AssessmentType type;

	@Min(value = 1)
	@Column(name = "WEEK_NUMBER", nullable = false)
	private short week;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ASSESSMENT_CATEGORY", nullable = false)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Category category;

	@OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
	@JsonIgnore
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Grade> grades = new HashSet<>();

	public Assessment() {
		super();
	}

	public Assessment(String title, Batch batch, Integer rawScore, AssessmentType type, Integer week,
			Category category) {
		super();
		this.title = title;
		this.batch = batch;
		this.rawScore = rawScore;
		this.type = type;
		this.week = week.shortValue();
		this.category = category;
	}

	public long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getTitle() {
		return this.category.getSkillCategory() + " " + this.type.name();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public int getRawScore() {
		return rawScore;
	}

	public void setRawScore(int rawScore) {
		this.rawScore = rawScore;
	}

	public AssessmentType getType() {
		return type;
	}

	public void setType(AssessmentType type) {
		this.type = type;
	}

	public short getWeek() {
		return week;
	}

	public void setWeek(short week) {
		this.week = week;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<Grade> getGrades() {
		return grades;
	}

	public void setGrades(Set<Grade> grades) {
		this.grades = grades;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batch == null) ? 0 : batch.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + rawScore;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + week;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Assessment other = (Assessment) obj;
		if (batch == null) {
			if (other.batch != null)
				return false;
		} else if (!batch.equals(other.batch))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (rawScore != other.rawScore)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type != other.type)
			return false;
		if (week != other.week)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Assessment [assessmentId= "+assessmentId+" title=" + title + ", batch=" + batch + ", rawScore=" + rawScore + ", type=" + type + ", week=" + week + ", category="
				+ category + "]";
	}
}
