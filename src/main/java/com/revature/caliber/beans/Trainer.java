package com.revature.caliber.beans;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Trainer.
 */
@Entity
@Table(name = "CALIBER_TRAINER")
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Trainer implements Serializable {

	private static final long serialVersionUID = -2546407792912483570L;

	@Id
	@Column(name = "TRAINER_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAINER_ID_SEQUENCE")
	@SequenceGenerator(name = "TRAINER_ID_SEQUENCE", sequenceName = "TRAINER_ID_SEQUENCE")
	@JsonProperty
	private int trainerId;

	@NotEmpty
	@Column(name = "NAME", nullable = false)
	@JsonProperty
	private String name;

	@NotEmpty
	@Column(name = "TITLE", nullable = false)
	@JsonProperty
	private String title;

	@NotEmpty
	@Email
	@Column(name = "EMAIL", nullable = false, unique = true, updatable = true)
	@JsonProperty
	private String email;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "TIER")
	private TrainerRole tier;

	@Column(name = "TRAINER_PASSWORD")
	private String password;

	@OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
	@JsonIgnore
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Batch> batches;

	/**
	 * Any evaluations that the trainer has undergone
	 */
	/*
	 * @OneToMany(mappedBy="trainer")
	 * 
	 * @JsonIgnore
	 */
	// private Set<TrainerTaskCompletion> evaluations;

	/**
	 * Any evaluations that the user has checked off on behalf of another trainer
	 */
	/*
	 * @OneToMany(mappedBy="checkedBy")
	 * 
	 * @JsonIgnore
	 */
	// private Set<TrainerTaskCompletion> checkOffs;

	public Trainer() {
		super();
	}

	public Trainer(String name, String title, String email, TrainerRole tier) {
		super();
		this.name = name;
		this.title = title;
		this.email = email;
		this.tier = tier;
	}

	public int getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public TrainerRole getTier() {
		return tier;
	}

	public void setTier(TrainerRole tier) {
		this.tier = tier;
	}

	public Set<Batch> getBatches() {
		return batches;
	}

	public void setBatches(Set<Batch> batches) {
		this.batches = batches;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/*
	 * public Set<TrainerTaskCompletion> getEvaluations() { return evaluations; }
	 * 
	 * public void setEvaluations(Set<TrainerTaskCompletion> evaluations) {
	 * this.evaluations = evaluations; }
	 * 
	 * public Set<TrainerTaskCompletion> getCheckOffs() { return checkOffs; }
	 * 
	 * public void setCheckOffs(Set<TrainerTaskCompletion> checkOffs) {
	 * this.checkOffs = checkOffs; }
	 */
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tier == null) ? 0 : tier.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Trainer other = (Trainer) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tier != other.tier)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Trainer [trainerId=" + trainerId + ", name=" + name + ", title=" + title + ", email=" + email
				+ ", tier=" + tier + "]";
	}

}
