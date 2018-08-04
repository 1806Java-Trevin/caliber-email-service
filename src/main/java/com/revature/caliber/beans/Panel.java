package com.revature.caliber.beans;

import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Results of the final panel interview for each associate.
 * @author Patrick Walsh
 *
 */
@Entity
@Table(name = "CALIBER_PANEL")
//@Cacheable
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Panel {

	// Interview Details
	@Id
	@Column(name = "PANEL_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PANEL_ID_SEQUENCE")
	@SequenceGenerator(name = "PANEL_ID_SEQUENCE", sequenceName = "PANEL_ID_SEQUENCE")
	private int id;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "TRAINEE_ID", nullable = false)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Trainee trainee;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "PANELIST_ID", nullable = false)
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Trainer panelist;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INTERVIEW_DATE")
	private Date interviewDate;
	
	@Column(name="DURATION")
	private String duration; // 1hr 30 minutes
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name="INTERVIEW_FORMAT")
	private InterviewFormat format; 
	
	@Column(name="INTERNET_CONNECTIVITY")
	private String internet; // stable/unstable
	
	@NotNull
	@Min(value = 1)
	@Column(name="PANEL_ROUND", nullable=false)
	private int panelRound;
	
	@Enumerated(EnumType.STRING)
	@Column(name="RECORDING_CONSENT")
	private RecordingConsent recordingConsent;
	
	@Column(name="RECORDING_LINK")
	private String recordingLink;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "PANEL_STATUS", nullable = false)
	private PanelStatus status;
	
	// Technical Feedback
	@OneToMany(mappedBy = "panel", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonManagedReference(value = "feedback")
	private Set<PanelFeedback> feedback;
	
	// Comments and General Feedback
	@Column(name = "ASSOCIATE_INTRO", nullable = true)
	private String associateIntro;
	
	@Column(name = "PROJECT_ONE_DESCRIPTION", nullable = true)
	private String projectOneDescription;
	
	@Column(name = "PROJECT_TWO_DESCRIPTION", nullable = true)
	private String projectTwoDescription;
	
	@Column(name = "PROJECT_THREE_DESCRIPTION", nullable = true)
	private String projectThreeDescription;
	
	@Column(name = "COMMUNICATION_SKILLS", nullable = true)
	private String communicationSkills;
	
	@Column(name = "OVERALL_FEEDBACK", nullable = true)
	private String overall;

	public Panel() {
		super();
		this.interviewDate = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Trainee getTrainee() {
		return trainee;
	}

	public void setTrainee(Trainee trainee) {
		this.trainee = trainee;
	}

	public Trainer getPanelist() {
		return panelist;
	}

	public void setPanelist(Trainer panelist) {
		this.panelist = panelist;
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public InterviewFormat getFormat() {
		return format;
	}

	public void setFormat(InterviewFormat format) {
		this.format = format;
	}

	public String getInternet() {
		return internet;
	}

	public void setInternet(String internet) {
		this.internet = internet;
	}

	public int getPanelRound() {
		return panelRound;
	}

	public void setPanelRound(int panelRound) {
		this.panelRound = panelRound;
	}

	public RecordingConsent isRecordingConsent() {
		return recordingConsent;
	}

	public void setRecordingConsent(RecordingConsent recordingConsent) {
		this.recordingConsent = recordingConsent;
	}

	public String getRecordingLink() {
		return recordingLink;
	}

	public void setRecordingLink(String recordingLink) {
		this.recordingLink = recordingLink;
	}

	public PanelStatus getStatus() {
		return status;
	}

	public void setStatus(PanelStatus status) {
		this.status = status;
	}

	public Set<PanelFeedback> getFeedback() {
		return feedback;
	}

	public void setFeedback(Set<PanelFeedback> feedback) {
		this.feedback = feedback;
	}

	public String getAssociateIntro() {
		return associateIntro;
	}

	public void setAssociateIntro(String associateIntro) {
		this.associateIntro = associateIntro;
	}

	public String getProjectOneDescription() {
		return projectOneDescription;
	}

	public void setProjectOneDescription(String projectOneDescription) {
		this.projectOneDescription = projectOneDescription;
	}

	public String getProjectTwoDescription() {
		return projectTwoDescription;
	}

	public void setProjectTwoDescription(String projectTwoDescription) {
		this.projectTwoDescription = projectTwoDescription;
	}

	public String getProjectThreeDescription() {
		return projectThreeDescription;
	}

	public void setProjectThreeDescription(String projectThreeDescription) {
		this.projectThreeDescription = projectThreeDescription;
	}

	public String getCommunicationSkills() {
		return communicationSkills;
	}

	public void setCommunicationSkills(String communicationSkills) {
		this.communicationSkills = communicationSkills;
	}

	public String getOverall() {
		return overall;
	}

	public void setOverall(String overall) {
		this.overall = overall;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((associateIntro == null) ? 0 : associateIntro.hashCode());
		result = prime * result + ((communicationSkills == null) ? 0 : communicationSkills.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((internet == null) ? 0 : internet.hashCode());
		result = prime * result + ((interviewDate == null) ? 0 : interviewDate.hashCode());
		result = prime * result + ((overall == null) ? 0 : overall.hashCode());
		result = prime * result + panelRound;
		result = prime * result + ((panelist == null) ? 0 : panelist.hashCode());
		result = prime * result + ((projectOneDescription == null) ? 0 : projectOneDescription.hashCode());
		result = prime * result + ((projectThreeDescription == null) ? 0 : projectThreeDescription.hashCode());
		result = prime * result + ((projectTwoDescription == null) ? 0 : projectTwoDescription.hashCode());
		result = prime * result + ((recordingConsent == null) ? 0 : recordingConsent.hashCode());
		result = prime * result + ((recordingLink == null) ? 0 : recordingLink.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((trainee == null) ? 0 : trainee.hashCode());
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
		Panel other = (Panel) obj;
		if (associateIntro == null) {
			if (other.associateIntro != null)
				return false;
		} else if (!associateIntro.equals(other.associateIntro))
			return false;
		if (communicationSkills == null) {
			if (other.communicationSkills != null)
				return false;
		} else if (!communicationSkills.equals(other.communicationSkills))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (format != other.format)
			return false;
		if (internet == null) {
			if (other.internet != null)
				return false;
		} else if (!internet.equals(other.internet))
			return false;
		if (interviewDate == null) {
			if (other.interviewDate != null)
				return false;
		} else if (!interviewDate.equals(other.interviewDate))
			return false;
		if (overall == null) {
			if (other.overall != null)
				return false;
		} else if (!overall.equals(other.overall))
			return false;
		if (panelRound != other.panelRound)
			return false;
		if (panelist == null) {
			if (other.panelist != null)
				return false;
		} else if (!panelist.equals(other.panelist))
			return false;
		if (projectOneDescription == null) {
			if (other.projectOneDescription != null)
				return false;
		} else if (!projectOneDescription.equals(other.projectOneDescription))
			return false;
		if (projectThreeDescription == null) {
			if (other.projectThreeDescription != null)
				return false;
		} else if (!projectThreeDescription.equals(other.projectThreeDescription))
			return false;
		if (projectTwoDescription == null) {
			if (other.projectTwoDescription != null)
				return false;
		} else if (!projectTwoDescription.equals(other.projectTwoDescription))
			return false;
		if (recordingConsent != other.recordingConsent)
			return false;
		if (recordingLink == null) {
			if (other.recordingLink != null)
				return false;
		} else if (!recordingLink.equals(other.recordingLink))
			return false;
		if (status != other.status)
			return false;
		if (trainee == null) {
			if (other.trainee != null)
				return false;
		} else if (!trainee.equals(other.trainee))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Panel [id=" + id + ", trainee=" + trainee + ", panelist=" + panelist + ", interviewDate="
				+ interviewDate + ", duration=" + duration + ", format=" + format + ", internet=" + internet
				+ ", panelRound=" + panelRound + ", recordingConsent=" + recordingConsent + ", recordingLink="
				+ recordingLink + ", status=" + status + ", feedback=" + feedback + ", associateIntro=" + associateIntro
				+ ", projectOneDescription=" + projectOneDescription + ", projectTwoDescription="
				+ projectTwoDescription + ", projectThreeDescription=" + projectThreeDescription
				+ ", communicationSkills=" + communicationSkills + ", overall=" + overall + "]";
	}

}
