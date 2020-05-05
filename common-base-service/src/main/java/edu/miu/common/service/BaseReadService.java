package edu.miu.common.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.miu.common.exception.ResourceNotFoundException;
import edu.miu.common.repository.search.SpecificationBuilder;

/**
 * <h1>Maharishi University of Management<br/>Computer Science Department</h1>
 * 
 * <p>Provides basic read and search capabilities for a particular type T 
 * and converts it to response type R before returning it to the calling method. 
 * See {@link SpecificationBuilder} for more info on how the search and query work.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
public interface BaseReadService<R, T, I> {
	
	R findById(I id) throws ResourceNotFoundException;
	
	List<R> findAll();

	Page<R> findAll(Pageable pageable);
	
	Long findAllCount();

	Long searchCount(String query);

	List<R> search(String query);

	Page<R> search(String query, Pageable pageable);
	
	R convertEntityToResponse(T entity);
	
	List<R> convertEntityListToResponseList(List<T> entityList);
	
	Page<R> convertEntityPageToResponsePage(Page<T> entityList);

}
