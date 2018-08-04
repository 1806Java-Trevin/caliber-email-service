package com.revature.caliber.beans;

import java.io.Serializable;
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
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Category.
 */
@Entity
@Table(name = "CALIBER_CATEGORY")
//@Cacheable
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {

	private static final long serialVersionUID = 3363756954535297728L;

	@Id
	@Column(name = "CATEGORY_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY_ID_SEQUENCE")
	@SequenceGenerator(name = "CATEGORY_ID_SEQUENCE", sequenceName = "CATEGORY_ID_SEQUENCE")
	@JsonProperty(value = "categoryId")
	private int categoryId;

	@JsonProperty(value = "skillCategory")
	@Column(name = "SKILL_CATEGORY")
	private String skillCategory;
	
	@JsonProperty(value = "active")
	@Column(name = "IS_ACTIVE", nullable=false)
	private boolean active;

	@OneToMany(mappedBy = "category")
	@JsonIgnore
	//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	private Set<Assessment> assessments;

	/**
	 * Instantiates a new Category.
	 */
	public Category() {
		super();
	}

	/**
	 * Create new category
	 * @param skillCategory
	 * @param active
	 */
	public Category(String skillCategory, boolean active) {
		super();
		this.skillCategory = skillCategory;
		this.active = active;
	}

	/**
	 * Gets category id.
	 *
	 * @return the category id
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * Sets category id.
	 *
	 * @param categoryId
	 *            the category id
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Gets skill category.
	 *
	 * @return the skill category
	 */
	public String getSkillCategory() {
		return skillCategory;
	}

	/**
	 * Sets skill category.
	 *
	 * @param skillCategory
	 *            the skill category
	 */
	public void setSkillCategory(String skillCategory) {
		this.skillCategory = skillCategory;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets assessments.
	 *
	 * @return the assessments
	 */
	public Set<Assessment> getAssessments() {
		return assessments;
	}

	/**
	 * Sets assessments.
	 *
	 * @param assessments
	 *            the assessments
	 */
	public void setAssessments(Set<Assessment> assessments) {
		this.assessments = assessments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((skillCategory == null) ? 0 : skillCategory.hashCode());
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
		Category other = (Category) obj;
		if (active != other.active)
			return false;
		if (skillCategory == null) {
			if (other.skillCategory != null)
				return false;
		} else if (!skillCategory.equals(other.skillCategory))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return skillCategory;
	}

}
