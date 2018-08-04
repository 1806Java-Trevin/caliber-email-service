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

import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;

@Repository
public class TrainerDAO {

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Initializing Oracle Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); 
		}
	}
	private static final Logger log = Logger.getLogger(TrainerDAO.class);
	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Find all trainers titles to be displayed on front end
	 * 
	 * 
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<String> findAllTrainerTitles() {
		String hql = "select distinct title FROM Trainer";
		return sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	/**
	 * Find a trainer by their email address. Practical for authenticating users
	 * through SSO
	 * 
	 * @param email
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Trainer findByEmail(String email) {
		Trainer trainer = (Trainer) sessionFactory.getCurrentSession().createCriteria(Trainer.class)
				.add(Restrictions.eq("email", email)).uniqueResult();
		log.debug("DAO found trainer by email " + trainer);
		return trainer;
	}

	/**
	 * Find all trainers. Useful for listing available trainers to select as
	 * trainer and cotrainer
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Trainer> findAll() {
		log.debug("Finding all trainers");
		return sessionFactory.getCurrentSession().createCriteria(Trainer.class)
				.add(Restrictions.ne("tier", TrainerRole.ROLE_INACTIVE))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * 
	 * Convenience method only. Not practical in production since trainers must
	 * be registered in the Salesforce with a matching email address.
	 * 
	 * @param trainer
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(Trainer trainer) {
		log.debug("Save trainer " + trainer);
		sessionFactory.getCurrentSession().save(trainer);
	}

	/**
	 * Find trainer by the given identifier
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Trainer findOne(Integer trainerId) {
		log.debug("Find trainer by id: " + trainerId);
		System.out.println("Find trainer by id: " + trainerId);
		return sessionFactory.getCurrentSession().get(Trainer.class, trainerId);
	}

	/**
	 * Updates a trainer in the database.
	 * 
	 * @param trainer
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(Trainer trainer) {
		log.debug("Update trainer " + trainer);
		sessionFactory.getCurrentSession().saveOrUpdate(trainer);
	}

	/**
	 * Convenience method only. Deletes a trainer from the database. Trainer
	 * will still be registered with a Salesforce account.
	 * 
	 * @param trainer
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Trainer trainer) {
		log.debug("Delete trainer " + trainer);
		sessionFactory.getCurrentSession().delete(trainer);
	}

}
