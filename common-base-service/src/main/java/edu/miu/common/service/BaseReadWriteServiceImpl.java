package edu.miu.common.service;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.common.exception.ResourceNotFoundException;
import edu.miu.common.service.mapper.BaseMapper;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>See {@link BaseReadWriteService}.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
@Transactional
public abstract class BaseReadWriteServiceImpl<R extends Serializable, T extends Serializable, I> extends BaseReadServiceImpl<R, T, I> implements BaseReadWriteService<R, T, I> {

	@Autowired
	private BaseMapper<R, T> requestMapper;
	
	@Override
	public R create(R request) {
		T entity = requestMapper.map(request);
		return convert(baseRepository.save(entity));
	}

	@Override
	public R update(I id, R request) {
		T entity = baseRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
		entity = requestMapper.map(request,  entity);
		return convert(baseRepository.save(entity));
	}
	
	@Override
	public void delete(I id) {
		T entity = baseRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
		baseRepository.delete(entity);
	}
	
	@Override
	public void deleteInBatch(Iterable<I> ids) {
		if(Objects.isNull(ids)) {
			throw new ResourceNotFoundException();
		}
		
		baseRepository.deleteAll(baseRepository.findAllById(ids));
	}


}
