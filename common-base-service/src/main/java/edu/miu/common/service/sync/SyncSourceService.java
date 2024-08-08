package edu.miu.common.service.sync;

import java.util.List;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Provides an abstraction to read a list of elements from a source system. This is used by {@link SyncService}</p>
 *
 * @author Payman Salek
 * 
 * @version 1.2.0
 * @since 1.2.0
 * 
 */
public interface SyncSourceService<E> {
	
	String getName();
	
	List<E> retrieveList();
	
}
