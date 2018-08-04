package com.revature.caliber.beans;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Batch.
 */
@Entity
@Table(name = "CALIBER_BATCH")
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Batch implements Serializable {

	private static final long serialVersionUID = -5755409643112884001L;

	@Id
	@Column(name = "BATCH_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BATCH_ID_SEQUENCE")
	@SequenceGenerator(name = "BATCH_ID_SEQUENCE", sequenceName = "BATCH_ID_SEQUENCE")
	private int batchId;

	@Column(name = "RESOURCE_ID")
	private String resourceId;

	@NotNull
	@Column(name = "TRAINING_NAME")
	private String trainingName;

	@NotNull
	@JsonProperty
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "TRAINER_ID", nullable = false)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Trainer trainer;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "CO_TRAINER_ID")
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Trainer coTrainer;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "SKILL_TYPE")
	private SkillType skillType;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "TRAINING_TYPE")
	private TrainingType trainingType;

	@JsonFormat(timezone="EST", pattern = "yyyy-MM-dd")
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", nullable = false)
	private Date startDate;

	@JsonFormat(timezone="EST", pattern = "yyyy-MM-dd")
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", nullable = false)
	private Date endDate;

	@NotEmpty
	@Column(name = "LOCATION", nullable = false)
	private String location;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ADDRESS_ID")
	private Address address;

	/**
	 * Anything above this grade is GREEN
	 */
	@Min(value = 1)
	@Column(name = "GOOD_GRADE_THRESHOLD")
	private short goodGradeThreshold;

	/**
	 * Anything above this grade but below goodGradeThreshold is YELLOW Anything
	 * below this grade is RED
	 */
	@Min(value = 1)
	@Column(name = "BORDERLINE_GRADE_THRESHOLD")
	private short borderlineGradeThreshold;

	@OneToMany(mappedBy = "batch", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonManagedReference(value = "traineeAndBatch")
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Trainee> trainees;

	@Column(name = "NUMBER_OF_WEEKS", nullable = false)
	private int weeks;
	
	@Column(name = "GRADED_WEEKS")
	private int gradedWeeks;

	@JsonIgnore
	@OneToMany(mappedBy = "batch")
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Note> notes;
	
	public Batch() {
		super();
		this.weeks = 1;
		this.gradedWeeks = 7;
		this.goodGradeThreshold = 80;
		this.borderlineGradeThreshold = 70;
		this.trainingType = TrainingType.Revature;
		this.trainees = new HashSet<>();
		this.notes = new HashSet<>();
	}

	/**
	 * Constructor mostly used for testing. Defaults TrainingType - Revature,
	 * SkillType - J2EE, Good grade - 80, and Borderline grade - 70
	 *
	 * @param trainingName
	 * @param trainer
	 * @param startDate
	 * @param endDate
	 * @param location
	 */
	public Batch(String trainingName, Trainer trainer, Date startDate, Date endDate, String location) {
		this();
		this.trainingName = trainingName;
		this.trainer = trainer;
		this.skillType = SkillType.J2EE;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = location;
	}

	public Batch(String resourceId, String trainingName, Trainer trainer, Trainer coTrainer, SkillType skillType,
			TrainingType trainingType, Date startDate, Date endDate) {
		super();
		this.resourceId = resourceId;
		this.trainingName = trainingName;
		this.trainer = trainer;
		this.coTrainer = coTrainer;
		this.skillType = skillType;
		this.trainingType = trainingType;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getTrainingName() {
		return trainingName;
	}

	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}

	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	public Trainer getCoTrainer() {
		return coTrainer;
	}

	public void setCoTrainer(Trainer coTrainer) {
		this.coTrainer = coTrainer;
	}

	public SkillType getSkillType() {
		return skillType;
	}

	public void setSkillType(SkillType skillType) {
		this.skillType = skillType;
	}

	public TrainingType getTrainingType() {
		return trainingType;
	}

	public void setTrainingType(TrainingType trainingType) {
		this.trainingType = trainingType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public short getGoodGradeThreshold() {
		return goodGradeThreshold;
	}

	public void setGoodGradeThreshold(short goodGradeThreshold) {
		this.goodGradeThreshold = goodGradeThreshold;
	}

	public short getBorderlineGradeThreshold() {
		return borderlineGradeThreshold;
	}

	public void setBorderlineGradeThreshold(short borderlineGradeThreshold) {
		this.borderlineGradeThreshold = borderlineGradeThreshold;
	}

	public Set<Trainee> getTrainees() {
		return trainees;
	}

	public void setTrainees(Set<Trainee> trainees) {
		this.trainees = trainees;
	}

	public int getWeeks() {
		return weeks;
	}

	public void setWeeks(int weeks) {
		this.weeks = weeks;
	}

	public Set<Note> getNotes() {
		return notes;
	}

	public void setNotes(Set<Note> notes) {
		this.notes = notes;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getGradedWeeks() {
		return gradedWeeks;
	}

	public void setGradedWeeks(int gradedWeeks) {
		this.gradedWeeks = gradedWeeks;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + borderlineGradeThreshold;
		result = prime * result + ((coTrainer == null) ? 0 : coTrainer.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + goodGradeThreshold;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((skillType == null) ? 0 : skillType.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((trainer == null) ? 0 : trainer.hashCode());
		result = prime * result + ((trainingName == null) ? 0 : trainingName.hashCode());
		result = prime * result + ((trainingType == null) ? 0 : trainingType.hashCode());
		result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
		result = prime * result + weeks;
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
		Batch other = (Batch) obj;
		if (borderlineGradeThreshold != other.borderlineGradeThreshold)
			return false;
		if (coTrainer == null) {
			if (other.coTrainer != null)
				return false;
		} else if (!coTrainer.equals(other.coTrainer))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (goodGradeThreshold != other.goodGradeThreshold)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (skillType != other.skillType)
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (trainer == null) {
			if (other.trainer != null)
				return false;
		} else if (!trainer.equals(other.trainer))
			return false;
		if (trainingName == null) {
			if (other.trainingName != null)
				return false;
		} else if (!trainingName.equals(other.trainingName))
			return false;
		if (resourceId == null) {
			if (other.resourceId != null)
				return false;
		} else if (!resourceId.equals(other.resourceId))
			return false;
		if (trainingType != other.trainingType)
			return false;
		if (weeks != other.weeks)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Batch [batchId=" + batchId + ", trainingName=" + trainingName + ", skillType=" + skillType
				+ ", trainingType=" + trainingType +", location==" + location + "]";
	}

}
