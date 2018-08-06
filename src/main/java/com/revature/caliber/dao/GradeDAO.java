package com.revature.caliber.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.TrainingStatus;

/**
 * Accesses grades from the database
 * 
 * @author Patrick Walsh
 * @author Ateeb Khawaja
 *
 */
@Repository
public class GradeDAO {

	private static final Logger log = Logger.getLogger(GradeDAO.class);
	private SessionFactory sessionFactory;
	private static final String TRAINEE = "trainee";
	private static final String TRAINEE_TRAINING_STATUS = "trainee.trainingStatus";
	private static final String TRAINEE_BATCH = "trainee.batch";

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Saves a grade in the database.
	 * 
	 * @param grade
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(Grade grade) {
		log.debug("Saving grade " + grade);
		sessionFactory.getCurrentSession().saveOrUpdate(grade);
	}

	/**
	 * Updates a grade in the database.
	 * 
	 * @param grade
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(Grade grade) {
		log.debug("Updating grade " + grade);
		sessionFactory.getCurrentSession().saveOrUpdate(grade);
	}

	/**
	 * Returns absolutely all grades for only the most coarsely-grained
	 * reporting. Useful for feeding data into application for statistical
	 * analyses, such as regression analysis, calculating mean, and finding
	 * average ;)
	 * 
	 * @param traineeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Grade> findAll() {
		log.debug("Finding all grades");
		return sessionFactory.getCurrentSession().createCriteria(Grade.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Returns grades for all trainees that took a particular assignment. Great
	 * for finding average/median/highest/lowest grades for a test
	 * 
	 * @param assessmentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Grade> findByAssessment(Long assessmentId) {
		log.debug("Finding grades for assessment: " + assessmentId);
		return sessionFactory.getCurrentSession().createCriteria(Grade.class).createAlias(TRAINEE, TRAINEE)
				.add(Restrictions.eq("assessment.assessmentId", assessmentId))
				.add(Restrictions.ne(TRAINEE_TRAINING_STATUS, TrainingStatus.Dropped))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Returns all grades for a trainee. Useful for generating a full-view of
	 * individual trainee performance.
	 * 
	 * @param traineeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Grade> findByTrainee(Integer traineeId) {
		log.debug("Finding all grades for trainee: " + traineeId);
		List <Grade> grades = sessionFactory.getCurrentSession().createCriteria(Grade.class)
				.createAlias(TRAINEE, TRAINEE)
				.add(Restrictions.gt("score", 0.0))
				.add(Restrictions.eq("trainee.traineeId", traineeId))
				.add(Restrictions.ne(TRAINEE_TRAINING_STATUS, TrainingStatus.Dropped))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		log.debug(grades);
		return grades;
	}

	/**
	 * Returns all grades for a batch. Useful for calculating coarsely-grained
	 * data for reporting.
	 * 
	 * @param batchId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Grade> findByBatch(Integer batchId) {
		log.debug("Finding all grades for batch: " + batchId);
		return sessionFactory.getCurrentSession().createCriteria(Grade.class)
				.createAlias(TRAINEE, TRAINEE)
				.createAlias(TRAINEE_BATCH, "b")
				.add(Restrictions.gt("score", 0.0))
				.add(Restrictions.eq("b.batchId", batchId))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.add(Restrictions.ne(TRAINEE_TRAINING_STATUS, TrainingStatus.Dropped))
				.list();
	}


	/**
	 * Returns all grades for a category. Useful for improving performance time
	 * of company-wide reporting
	 * 
	 * @param batchId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Grade> findByCategory(Integer categoryId) {
		log.debug("Finding all grades for category: " + categoryId);
		return sessionFactory.getCurrentSession().createCriteria(Grade.class).createAlias("assessment", "a")
				.createAlias("a.category", "c").add(Restrictions.eq("c.categoryId", categoryId))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Returns grades for all trainees in the batch on a given week. Used to
	 * load grade data onto the input spreadsheet, as well as tabular/chart
	 * reporting.
	 * 
	 * @param batchId
	 * @param week
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Grade> findByWeek(Integer batchId, Integer week) {
		log.debug("Finding week " + week + " grades for batch: " + batchId);
		return sessionFactory.getCurrentSession().createCriteria(Grade.class).createAlias(TRAINEE, TRAINEE).createAlias(TRAINEE_BATCH, "b")
				.add(Restrictions.eq("b.batchId", batchId)).createAlias("assessment", "a")
				.add(Restrictions.eq("a.week", week.shortValue()))
				.add(Restrictions.ne(TRAINEE_TRAINING_STATUS, TrainingStatus.Dropped))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	/**
	 * Returns all grades issued as acting trainer or cotrainer to a batch.
	 * Useful for calculating coarsely-grained data for reporting. Potential
	 * refactor here.. this queries database twice where we could find way to
	 * simply join.
	 * 
	 * @param trainerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Grade> findByTrainer(Integer trainerId) {
		log.debug("Finding all grades for trainer: " + trainerId);
		List<Grade> astrainer = sessionFactory.getCurrentSession().createCriteria(Grade.class).createAlias(TRAINEE, TRAINEE)
				.createAlias(TRAINEE_BATCH, "b").createAlias("b.trainer", "t")
				.add(Restrictions.eq("t.trainerId", trainerId)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
		List<Grade> ascotrainer = sessionFactory.getCurrentSession().createCriteria(Grade.class)
				.createAlias(TRAINEE_BATCH, "b").createAlias("b.coTrainer", "t")
				.add(Restrictions.eq("t.trainerId", trainerId)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
		astrainer.addAll(ascotrainer);
		return astrainer;
	}

}
