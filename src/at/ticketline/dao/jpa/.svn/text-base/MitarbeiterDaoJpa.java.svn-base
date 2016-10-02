package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.interfaces.MitarbeiterDao;
import at.ticketline.entity.LoginStatus;
import at.ticketline.entity.Mitarbeiter;

public class MitarbeiterDaoJpa extends GenericDaoJpa<Mitarbeiter,Integer> implements MitarbeiterDao {
    
    	private Mitarbeiter loggedInMitarbeiter;
    
	@Override
	public LoginStatus logIn(String username, String password) {
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Mitarbeiter> query = builder.createQuery(Mitarbeiter.class);
		Root<Mitarbeiter> rootMitarbeiter = query.from(Mitarbeiter.class);
		List<Predicate> wherePredicates = new ArrayList<Predicate>();
		
		if((username != null) && (password != null))
		{
			wherePredicates.add(builder.equal(rootMitarbeiter.<String>get("username"), username));
//			wherePredicates.add(builder.equal(rootMitarbeiter.<String>get("passwort"), password));
		}
		Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));
		query.where(whereClause);
		try{
		    	if(this.entityManager.createQuery(query).getSingleResult().getPasswort().equals(password))
		    	{
		    	    loggedInMitarbeiter = this.entityManager.createQuery(query).getSingleResult();
		    	    return LoginStatus.SUCCEEDED;
		    	}
		    	else{
		    	    return LoginStatus.WRONG_PASSWORD;
		    	}
			
		} catch (NoResultException ex){
			return LoginStatus.WRONG_USERNAME;
		}
		
	}
	
	@Override
	public boolean logOut(Mitarbeiter mitarbeiter) {
	    if(loggedInMitarbeiter.getId() == mitarbeiter.getId())
	    {
		loggedInMitarbeiter = null;
		return true;
	    }
	    else{
		return false;
	    }
	    
	    
	}

	@Override
	public Mitarbeiter getLoggedInMitarbeiter() {
	    return loggedInMitarbeiter;
	}

}
