package edu.miu.common.service.sync;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Provides an abstraction to create, read, update and delete from a target system. 
 * This abstraction is used by {@link SyncService}.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.2.0
 * @since 1.2.0
 * 
 */
public interface SyncTargetService<Element> extends SyncSourceService<Element> {
	
	default Element create(Element element) {
		return null;
	}
	
	default Element update(Element element) {
		return null;
	}
	
	default Element delete(Element element) {
		return null;
	}

	default boolean pauseSync() {
		return false;
	}
	
}
