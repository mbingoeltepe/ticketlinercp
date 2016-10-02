package at.ticketline.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<E, ID extends Serializable> {

	@Transactional
	public E persist(E entity);

	@Transactional
	public void remove(E entity);

	public E findById(ID id);

	public List<E> findAll();

	public List<E> find(String where);

	@Transactional
	public E merge(E entity);

	public void flush();

	public Long count();
	
	public void removeAll();
	
	/**
	 * holt den aktuellen Zustand der Entity aus der DB
	 * und speichert ihn in t
	 * @param entity, in dem der aktuelle Zustand gespeichert wird
	 */
	public void refresh(E entity);


}