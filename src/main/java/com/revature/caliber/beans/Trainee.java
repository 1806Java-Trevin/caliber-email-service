package com.revature.caliber.beans;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The type Trainee
 */
@Entity
@Table(name = "CALIBER_TRAINEE")
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Trainee implements Serializable {

	private static final long serialVersionUID = -9090223980655307018L;

	@Id
	@Column(name = "TRAINEE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAINEE_ID_SEQUENCE")
	@SequenceGenerator(name = "TRAINEE_ID_SEQUENCE", sequenceName = "TRAINEE_ID_SEQUENCE")
	private int traineeId;

	@Column(name = "RESOURCE_ID")
	private String resourceId;

	@NotEmpty
	@Column(name = "TRAINEE_NAME")
	private String name;

	@NotEmpty
	@Email
	@Column(name = "TRAINEE_EMAIL", nullable = false)
	private String email;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "TRAINING_STATUS")
	private TrainingStatus trainingStatus;

	@NotNull
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "BATCH_ID", nullable = false)
	@JsonBackReference(value = "traineeAndBatch")
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Batch batch;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "SKYPE_ID")
	private String skypeId;

	@Column(name = "PROFILE_URL")
	private String profileUrl;

	// new columns
	@Column(name = "RECRUITER_NAME")
	private String recruiterName;

	@Column(name = "COLLEGE")
	private String college;

	@Column(name = "DEGREE")
	private String degree;

	@Column(name = "MAJOR")
	private String major;

	@Column(name = "TECH_SCREENER_NAME")
	private String techScreenerName;

	@Column(name = "REVPRO_PROJECT_COMPLETION")
	private String projectCompletion;
	// end of new columns

	@Enumerated(EnumType.STRING)
	@Column(name = "FLAG_STATUS")
	private TraineeFlag flagStatus;

	@Length(min = 0, max = 4000)
	@Column(name = "FLAG_NOTES", length = 4000)
	private String flagNotes;

	@JsonIgnore
	@OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Grade> grades;

	@JsonIgnore
	@OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Note> notes;

	@JsonIgnore
	@OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy(clause = "INTERVIEW_DATE DESC")
	private Set<Panel> panelInterviews = new TreeSet<>();

	public Trainee() {
		super();
		this.flagStatus = TraineeFlag.NONE;
	}

	/**
	 * Constructor used mostly for testing. Default TrainingStatus as Training
	 * 
	 * @param name
	 * @param resourceId
	 * @param email
	 * @param batch
	 */
	public Trainee(String name, String resourceId, String email, Batch batch) {
		super();
		this.name = name;
		this.resourceId = resourceId;
		this.email = email;
		this.trainingStatus = TrainingStatus.Training;
		this.batch = batch;
	}

	public Trainee(String name, String email, TrainingStatus trainingStatus, String phoneNumber, String skypeId,
			String profileUrl, String recruiterName, String college, String degree, String major,
			String techScreenerName, String projectCompletion) {
		super();
		this.name = name;
		this.email = email;
		this.trainingStatus = trainingStatus;
		this.phoneNumber = phoneNumber;
		this.skypeId = skypeId;
		this.profileUrl = profileUrl;
		this.recruiterName = recruiterName;
		this.college = college;
		this.degree = degree;
		this.major = major;
		this.techScreenerName = techScreenerName;
		this.projectCompletion = projectCompletion;
	}

	public int getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(int traineeId) {
		this.traineeId = traineeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public TrainingStatus getTrainingStatus() {
		return trainingStatus;
	}

	public void setTrainingStatus(TrainingStatus trainingStatus) {
		this.trainingStatus = trainingStatus;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Set<Grade> getGrades() {
		return grades;
	}

	public void setGrades(Set<Grade> grades) {
		this.grades = grades;
	}

	public Set<Note> getNotes() {
		return notes;
	}

	public void setNotes(Set<Note> notes) {
		this.notes = notes;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Set<Panel> getPanelInterviews() {
		return panelInterviews;
	}

	public void setPanelInterviews(Set<Panel> panelInterviews) {
		this.panelInterviews = panelInterviews;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((profileUrl == null) ? 0 : profileUrl.hashCode());
		result = prime * result + ((skypeId == null) ? 0 : skypeId.hashCode());
		result = prime * result + ((trainingStatus == null) ? 0 : trainingStatus.hashCode());
		result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
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
		Trainee other = (Trainee) obj;
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
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (profileUrl == null) {
			if (other.profileUrl != null)
				return false;
		} else if (!profileUrl.equals(other.profileUrl))
			return false;
		if (skypeId == null) {
			if (other.skypeId != null)
				return false;
		} else if (!skypeId.equals(other.skypeId))
			return false;
		if (trainingStatus != other.trainingStatus)
			return false;
		if (resourceId == null) {
			if (other.resourceId != null)
				return false;
		} else if (!resourceId.equals(other.resourceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Trainee [traineeId=" + traineeId + ", name=" + name + ", email=" + email + ", trainingStatus="
				+ trainingStatus + ", major=" + major + "]";
	}
	

	public String getRecruiterName() {
		return recruiterName;
	}

	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getTechScreenerName() {
		return techScreenerName;
	}

	public void setTechScreenerName(String techScreenerName) {
		this.techScreenerName = techScreenerName;
	}

	public String getProjectCompletion() {
		return projectCompletion;
	}

	public void setProjectCompletion(String projectCompletion) {
		this.projectCompletion = projectCompletion;
	}
	
	public TraineeFlag getFlagStatus() {
		return flagStatus;
	}

	public void setFlagStatus(TraineeFlag flagStatus) {
		this.flagStatus = flagStatus;
	}

	public String getFlagNotes() {
		return flagNotes;
	}

	public void setFlagNotes(String flagNotes) {
		this.flagNotes = flagNotes;
	}
}
