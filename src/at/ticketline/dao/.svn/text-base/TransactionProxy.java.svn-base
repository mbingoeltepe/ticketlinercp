package at.ticketline.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;

import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;

public class TransactionProxy implements InvocationHandler {
	protected transient Logger log = LogFactory
			.getLogger(TransactionProxy.class);
	protected Object target = null;
	protected String targetClassName = null;
	protected Map<Integer, Boolean> methods = new HashMap<Integer, Boolean>();
	protected Field persistenceContextField = null;
	protected Method persistenceContextMethod = null;

	public static Object createTransactionProxy(Object obj) {
		return java.lang.reflect.Proxy.newProxyInstance(obj.getClass()
				.getClassLoader(), obj.getClass().getInterfaces(),
				new TransactionProxy(obj));
	}

	@SuppressWarnings("unchecked")
	public TransactionProxy(Object target) {
		this.target = target;
		this.targetClassName = this.target.getClass().getName();
		
		Class<EntityManager> emClass;
		try {
			emClass = (Class<EntityManager>)Class.forName("javax.persistence.EntityManager");
		} catch (ClassNotFoundException e) {
            throw new DaoException("Couldn't find EntityManager class",e);
		}
		
		List<Field> fields = Utils.getAllFields(this.target.getClass());
		for (Field f : fields) {
			if (f.isAnnotationPresent(PersistenceContext.class)) {
				if (emClass.isAssignableFrom(f.getType())) {
					this.log.trace("PersistenceContext annotation on field #0##1 found",
							this.targetClassName, f.getName());
					this.persistenceContextField = f;
					break;
				} else {
					this.log.trace("Field #0##1 with PersistenceContext annotation hasn't type EntityManager",
							this.targetClassName, f.getName());
				}
			}
		}
		
		if (this.persistenceContextField != null) {
			return;
		}
		
		List<Method> methods = Utils.getAllMethods(this.target.getClass());
		for (Method m : methods) {
			if (m.isAnnotationPresent(PersistenceContext.class)) {

				Class<?>[] parameters = m.getParameterTypes();
				if (parameters.length != 1) {
					this.log.trace("Method #0##1() has PersistenceContext annotation, but has invalid number of parameters - must be one parameter",this.targetClassName, m.getName());
					continue;
				}

				if (emClass.isAssignableFrom(parameters[0])) {
				  this.persistenceContextMethod = m;
					this.log.trace(
							"PersistenceContext annotation on method #0##1() found",
							this.targetClassName, m.getName());
				} else {
					this.log.trace("Parameter of method #0##1() is not compatible to javax.persistence.EntityManager",this.targetClassName, m.getName());
				}
				break;
			}
		}
		if (this.persistenceContextMethod != null) {
			return;
		}
		this.log.trace("No PersistenceContext annotation on #0 found",
				this.targetClassName);
	}

	@Override
	public Object invoke(Object obj, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		boolean localTransaction = false;
		boolean hasException = false;
		EntityManager em = EntityManagerUtil.getCurrentEntityManager();
		EntityTransaction transaction = em.getTransaction();

		if (this.isMethodTransactional(method)) {
			if (transaction.isActive()) {
				this.log.trace("Joining transaction for #0##1()",
						this.targetClassName, method.getName());
			} else {
				this.log.trace("Transaction for #0##1() started",
						this.targetClassName, method.getName());
				transaction.begin();
				localTransaction = true;
			}
		} else {
			this.log.trace("No transaction on #0##1()", this.targetClassName,
					method.getName());
		}

		this.setEntityManager(em);

		try {
			result = method.invoke(this.target, args);
		} catch (InvocationTargetException e) {
			hasException = true;
			Throwable cause = e.getTargetException();
			if ((cause instanceof DaoException) | (cause instanceof ConstraintViolationException)) {
				throw cause;
			} else {
				throw new DaoException(cause);
			}
		} catch (Exception e) {
			hasException = true;
			throw new DaoException(e);
		} finally {
			if ((hasException) && (localTransaction)) {
				this.log.trace("Transaction rollback");
				transaction.rollback();
			} else if ((hasException) && (transaction.isActive())) {
				this.log.trace("Mark transaction to be rollbacked back");
				transaction.setRollbackOnly();
			}
		}

		if (localTransaction) {
			this.log.trace("Committing transaction");
			try {
				if (transaction.isActive()) {
					transaction.commit();
				}
			} catch (RollbackException rbe) {
				throw new DaoException("Transaction failed", rbe);
			}
		}
		return result;
	}

	protected boolean isMethodTransactional(Method m) {
		int hashCode = m.hashCode();
		if (this.methods.containsKey(hashCode) == false) {
			this.methods.put(hashCode, m
					.isAnnotationPresent(Transactional.class));
		}
		return this.methods.get(hashCode);
	}

	protected void setEntityManager(EntityManager em) {
		String unset = "";
		if (em == null) { unset = "un"; }
		if (this.persistenceContextField != null) {
			try {
				this.persistenceContextField.set(this.target,em);
			} catch (Exception e) {
				this.log.error(e,"Could'not #0set EntityManager on #1##2",unset,this.targetClassName,this.persistenceContextField.getName());
				throw new DaoException(e);
			}
            return;
		}
		if (this.persistenceContextMethod != null) {
			try {
				this.persistenceContextMethod.invoke(this.target,em);
			} catch (Exception e) {
				this.log.error(e,"Could'not #0set EntityManager on #1##2()",unset,this.targetClassName,this.persistenceContextMethod.getName());
				throw new DaoException(e);
			}
		}
	}

	protected void unsetEntityManager() {
		this.setEntityManager(null);
	}

	public Object getTargetObject() {
		return this.target;
	}
}
