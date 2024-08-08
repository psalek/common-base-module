package edu.miu.common.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.miu.common.repository.search.SpecificationBuilder;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
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
	
	R findById(I id);
	
	List<R> findAll();

	Page<R> findAll(Pageable pageable);
	
	Long findAllCount();

	Long searchCount(String query);

	List<R> search(String query);

	Page<R> search(String query, Pageable pageable);
	
	R convert(T entity);
	
	R convert(Optional<T> entity);
	
	List<R> convert(List<T> entityList);
	
	Page<R> convert(Page<T> entityPage);

	@Deprecated
	R convertEntityToResponse(T entity);
	
	@Deprecated
	List<R> convertEntityListToResponseList(List<T> entityList);
	
	@Deprecated
	Page<R> convertEntityPageToResponsePage(Page<T> entityList);

}
