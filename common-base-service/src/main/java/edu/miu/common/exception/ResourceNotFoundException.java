package edu.miu.common.exception;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>This exception is thrown when a resource cannot be found. 
 * This should result in an HTTP 400 level error. Extends {@link Exception} functionality.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
