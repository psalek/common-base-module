package edu.miu.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.common.exception.ResourceNotFoundException;
import edu.miu.common.repository.BaseRepository;
import edu.miu.common.repository.search.SpecificationBuilder;
import edu.miu.common.service.mapper.BaseMapper;

/**
 * <h1>Maharishi University of Management<br/>Computer Science Department</h1>
 * 
 * <p>See {@link BaseReadService}.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
@Component
@Transactional
public abstract class BaseReadServiceImpl<R extends Serializable, T, I> implements BaseReadService<R, T, I> {
	
	@Autowired
	protected BaseRepository<T, I> baseRepository;
	
	@Autowired
	protected BaseMapper<T, R> responseMapper;
	
	@Override
	public R findById(I id) throws ResourceNotFoundException {
		T entity = baseRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
		return responseMapper.map(entity);
	}
	
	@Override
	public List<R> findAll() {
		return convertEntityListToResponseList(baseRepository.findAll());		
	}

	@Override
	public Long findAllCount() {
		return baseRepository.count();
	}

	@Override
	public Page<R> findAll(Pageable pageable) {
		return baseRepository.findAll(pageable).map(responseMapper::map);
	}

	@Override
	public Long searchCount(String query) {
		return baseRepository.count(new SpecificationBuilder<T>(query).build());
	}

	@Override
	public List<R> search(String query) {
		return convertEntityListToResponseList(baseRepository.findAll(new SpecificationBuilder<T>(query).build()));		
	}

	@Override
	public Page<R> search(String query, Pageable pageable) {
		return baseRepository.findAll(new SpecificationBuilder<T>(query).build(), pageable).map(responseMapper::map);
	}

	@Override
	public R convertEntityToResponse(T entity) {
		if(null == entity) {
			return null;
		}
		else {
			return responseMapper.map(entity);
		}
	}

	@Override
	public List<R> convertEntityListToResponseList(List<T> entityList) {
		if(null == entityList) {
			return null;
		}
		else {
			return entityList.stream()
					.map(responseMapper::map)
					.collect(Collectors.toList());
		}
	}

	@Override
	public Page<R> convertEntityPageToResponsePage(Page<T> entityPage) {
		if(null == entityPage) {
			return null;
		}
		else {
			return entityPage.map(responseMapper::map);
		}
	}
	
}
