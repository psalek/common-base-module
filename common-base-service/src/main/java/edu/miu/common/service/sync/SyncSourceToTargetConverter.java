package edu.miu.common.service.sync;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Provides an abstraction to convert an element of type "Source" to an element of type "Target". 
 * It also needs to provide a "match" and "equate" functionality to compare elements of "Source" and "Target". 
 * Please note that the "Source" elements first need to be converted to "Target" before they can be compared using "match or "equate".
 * This abstraction is used by {@link SyncService}.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.2.0
 * @since 1.2.0
 * 
 */
public interface SyncSourceToTargetConverter<Source, Target> {

	/**
	 * Converts an object of type "Source" to an object of type "Target".
	 *
	 * @param source an element of the source system.
	 * @return converted source
	 */
	Target convert(Source source);
	
	/**
	 * Merges "Source" to "Target"
	 *
	 * @param source an element of the source system
	 * @param target an element of the target system
	 * 
	 * @return merged target object
	 */
	Target merge(Source source, Target target);
	
	/**
	 * <p>Finds out if two objects of type "Target" are actually referring to the same record in the target system. 
	 * It usually does it (although implementation details may vary) by comparing the ids/keys of the two object.</p>
	 *
	 * @param t1 Converted source object 
	 * @param t2 Target object
	 * 
	 * @return true if the two objects match and false if they don't
	 */
	boolean match(Target convertedSource, Target target);

	/**
	 * <p>Finds out if two objects of type "Target" are equivalent. This method is used to find out whether an update is necessary 
	 * to be sent to the "Target" system. It usually does it (although implementation details may vary) by comparing 
	 * all the fields of the two objects.</p>
	 *
	 * @param t1 Target object 
	 * @param t2 Converted source object
	 * 
	 * @return true if the two objects "equate" (see above for description) and false if they don't
	 */
	boolean equate(Target target, Target convertedSource);

}
