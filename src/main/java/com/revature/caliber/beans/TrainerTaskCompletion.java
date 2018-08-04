package com.revature.caliber.beans;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;

/*@Entity
@Table(name = "CALIBER_TASK_COMPLETION")
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class TrainerTaskCompletion {

/*	@Id
	@Column(name = "TASK_COMPLETION_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_COMPLETION_ID_SEQUENCE")
	@SequenceGenerator(name = "TASK_COMPLETION_ID_SEQUENCE", sequenceName = "TASK_COMPLETION_ID_SEQUENCE")*/
	private int id;

	/**
	 * Trainer_id is the trainer being evaluated in the Train the Trainer Program
	 * 
	 * Use LAZY fetching with a join predicate in the Hibernate Criteria when the
	 * data is needed on the UI (only on the VP's Trainer page)
	 */
/*	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRAINER_ID", nullable = false)
	@JsonProperty*/
	private Trainer trainer;

	/**
	 * Checked_by is the trainer(user) that checked off the item as complete.
	 * 
	 * Use LAZY fetching with a join predicate in the Hibernate Criteria when the
	 * data is needed on the UI (only on the VP's Trainer page)
	 */
	/*@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "CHECKED_BY", nullable = false)
	@JsonProperty*/
	private Trainer checkedBy;

	/*@Temporal(TemporalType.DATE)
	@Column(name = "COMPLETION_DATE", nullable = false)*/
	private Date completionDate;

	/**
	 * The task that was completed
	 */
/*	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "TASK_ID", nullable = false)
	@JsonProperty*/
	private TrainerTask taskCompleted;

	public TrainerTaskCompletion() {
		super();
	}

	public TrainerTaskCompletion(Trainer trainer, Trainer checkedBy, Date completionDate, TrainerTask taskCompleted) {
		super();
		this.trainer = trainer;
		this.checkedBy = checkedBy;
		this.completionDate = completionDate;
		this.taskCompleted = taskCompleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	public Trainer getCheckedBy() {
		return checkedBy;
	}

	public void setCheckedBy(Trainer checkedBy) {
		this.checkedBy = checkedBy;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public TrainerTask getTaskCompleted() {
		return taskCompleted;
	}

	public void setTaskCompleted(TrainerTask taskCompleted) {
		this.taskCompleted = taskCompleted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkedBy == null) ? 0 : checkedBy.hashCode());
		result = prime * result + ((completionDate == null) ? 0 : completionDate.hashCode());
		result = prime * result + ((taskCompleted == null) ? 0 : taskCompleted.hashCode());
		result = prime * result + ((trainer == null) ? 0 : trainer.hashCode());
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
		TrainerTaskCompletion other = (TrainerTaskCompletion) obj;
		if (checkedBy == null) {
			if (other.checkedBy != null)
				return false;
		} else if (!checkedBy.equals(other.checkedBy))
			return false;
		if (completionDate == null) {
			if (other.completionDate != null)
				return false;
		} else if (!completionDate.equals(other.completionDate))
			return false;
		if (taskCompleted == null) {
			if (other.taskCompleted != null)
				return false;
		} else if (!taskCompleted.equals(other.taskCompleted))
			return false;
		if (trainer == null) {
			if (other.trainer != null)
				return false;
		} else if (!trainer.equals(other.trainer))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TrainerTaskCompletion [id=" + id + ", trainer=" + trainer + ", checkedBy=" + checkedBy
				+ ", completionDate=" + completionDate + ", taskCompleted=" + taskCompleted + "]";
	}

}
