package edu.miu.common.repository.search;

import java.io.Serializable;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>This is loaded from the incoming query string. Used for basic search of REST resources. 
 * See {@link EntitySpecification} for the list of allowable operations.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
public class SearchCriteria implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String key;

    private String operation;
    
    private Object value;

	public SearchCriteria(String key, String operation, Object value) {
		super();
		this.key = key;
		this.operation = operation;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
    
}

