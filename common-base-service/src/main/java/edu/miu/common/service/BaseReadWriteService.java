package edu.miu.common.service;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Provides basic crteate/update/delete capabilities on top of read and search capabilities 
 * already provided by {@link BaseReadService}.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
public interface BaseReadWriteService<R, T, I> extends BaseReadService<R, T, I> {

	R create(R request);

	R update(I id, R request);
	
	void delete(I id);
	
	void deleteInBatch(Iterable<I> ids);

}
