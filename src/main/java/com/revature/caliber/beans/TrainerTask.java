package com.revature.caliber.beans;

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Tasks that need to be completed by trainers for the Train the Trainer
 * Program. This measures quality of trainers before they begin a batch.
 * 
 * @author Patrick Walsh
 *
 */
/*@Entity
@Table(name = "CALIBER_TASK")
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class TrainerTask {

	/*@Id
	@Column(name = "TASK_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_ID_SEQUENCE")
	@SequenceGenerator(name = "TASK_ID_SEQUENCE", sequenceName = "TASK_ID_SEQUENCE")*/
	private int id;

	//@Column(name = "TASK_DESCRIPTION")
	private String description;

	/**
	 * Priority is used to rank the order the tasks need to appear on the UI
	 */
	//@Column(name = "TASK_PRIORITY")
	private int priority;

	/**
	 * Only active tasks are displayed on the UI
	 */
	
	//@Column(name = "IS_ACTIVE")
	private int active;

//	@OneToMany(mappedBy = "taskCompleted")
//	@JsonIgnore
	private Set<TrainerTaskCompletion> evaluations;

	public TrainerTask() {
		super();
	}

	public TrainerTask(String description, int priority, int active) {
		super();
		this.description = description;
		this.priority = priority;
		this.active = active;
	}
	
	public TrainerTask(String description, int priority) {
		super();
		this.description = description;
		this.priority = priority;
		this.active = 1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int isActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + priority;
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
		TrainerTask other = (TrainerTask) obj;
		if (active != other.active)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (priority != other.priority)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TrainerTask [id=" + id + ", description=" + description + ", priority=" + priority + ", active="
				+ active + "]";
	}

}
