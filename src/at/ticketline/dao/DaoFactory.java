package at.ticketline.dao;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.Validation;
import javax.validation.Validator;

import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;


public class DaoFactory {
	protected static final Logger log = LogFactory.getLogger(DaoFactory.class);
	protected static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @SuppressWarnings({ "rawtypes" })
    protected static final ThreadLocal<Map<String,GenericDao>> daoMapThreadLocal = new ThreadLocal<Map<String,GenericDao>>();
	
    @SuppressWarnings({ "rawtypes" })
	public static synchronized GenericDao findDaoByEntity(Class clazz) {
		return DaoFactory.findDaoByEntityName(clazz.getSimpleName());
	}
	
    @SuppressWarnings("rawtypes")
	public static synchronized GenericDao findDaoByEntityName(String name) {
    	Map<String,GenericDao> daoMap = null;
    	if (DaoFactory.daoMapThreadLocal.get() != null) {
    		daoMap = DaoFactory.daoMapThreadLocal.get();
    	} else {
    		daoMap = new HashMap<String,GenericDao>();
    		DaoFactory.daoMapThreadLocal.set(daoMap);
    	}

		if (daoMap.containsKey(name) == false) {
			String className = "at.ticketline.dao.jpa." + name + "DaoJpa";
			DaoFactory.log.info("Creating Dao #0",className);
			GenericDao dao = null;
			try {
				dao = (GenericDao)Class.forName(className).newInstance();
				Utils.callLifecycleMethod(PostConstruct.class,dao);
				dao = (GenericDao)TransactionProxy.createTransactionProxy(dao);
			} catch (InstantiationException e) {
				throw new DaoException(e);
			} catch (IllegalAccessException e) {
				throw new DaoException(e);
			} catch (ClassNotFoundException e) {
				throw new DaoException("DAO class for entity " + name + " couldn't be found",e);
			}
			daoMap.put(name,dao);
		}
		return daoMap.get(name);
	}
	
    @SuppressWarnings("rawtypes")
	public synchronized static void destroy() {
    	DaoFactory.log.info("Destroying DaoFactory");
    	Map<String,GenericDao> daoMap = null;
    	if (DaoFactory.daoMapThreadLocal.get() != null) {
    		daoMap = DaoFactory.daoMapThreadLocal.get();
    	} else {
    		return;
    	}
    	for (Entry<String,GenericDao> d : daoMap.entrySet()) {
    		TransactionProxy tp = (TransactionProxy)Proxy.getInvocationHandler(d.getValue());
    		GenericDao dao = (GenericDao)tp.getTargetObject();
    		Utils.callLifecycleMethod(PreDestroy.class,dao);
    	}
    	daoMap.clear();
    	DaoFactory.daoMapThreadLocal.remove();
    }
    
    public static Validator getValidator() {
    	return DaoFactory.validator;
    }
}
