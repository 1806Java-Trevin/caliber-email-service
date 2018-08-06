package com.revature.caliber.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.caliber.beans.Assessment;
import com.revature.caliber.beans.TrainingStatus;

/**
 * 
 * @author Patrick Walsh
 * @author Ateeb Khawaja
 *
 */
@Repository
public class AssessmentDAO {

	private static final Logger log = Logger.getLogger(AssessmentDAO.class);
	private SessionFactory sessionFactory;
	private static final String BATCH = "batch";

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void save(Assessment assessment) {
		log.debug("Saving assessment " + assessment);
		sessionFactory.getCurrentSession().save(assessment);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Assessment findOne(long id) {
		log.debug("Finding one assessment " + id);
		return (Assessment) sessionFactory.getCurrentSession().get(Assessment.class, id);
	}

	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Assessment> findAll() {
		log.debug("Find all assessment");
		return sessionFactory.getCurrentSession().createCriteria(Assessment.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Assessment> findByWeek(Integer batchId, Integer week) {
		log.debug("Find assessment by week number " + week + " for batch " + batchId + " ");
		return sessionFactory.getCurrentSession().createCriteria(Assessment.class)
				.createAlias(BATCH, BATCH).createAlias("batch.trainees", "t", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne("t.trainingStatus", TrainingStatus.Dropped))
				.add(Restrictions.and(Restrictions.eq("batch.batchId", batchId),
						Restrictions.eq("week", week.shortValue())))
				.createAlias("grades", "grades", JoinType.LEFT_OUTER_JOIN)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Assessment> findByBatchId(Integer batchId) {
		log.debug("Find assessment by batchId" + batchId + " ");
		return sessionFactory.getCurrentSession().createCriteria(Assessment.class)
				.createAlias(BATCH, "b", JoinType.LEFT_OUTER_JOIN)
				.createAlias("b.trainees", "t", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.and(Restrictions.eq("b.batchId", batchId)))
				.add(Restrictions.ne("t.trainingStatus", TrainingStatus.Dropped))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void update(Assessment assessment) {
		log.debug("Updating assessment " + assessment + "  IN THE DAO");
		sessionFactory.getCurrentSession().update(assessment);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void delete(Assessment assessment) {
		log.debug("Deleting assessment " + assessment);
		sessionFactory.getCurrentSession().delete(assessment);
	}	
}
