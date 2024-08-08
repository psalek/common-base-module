package edu.miu.common.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.Data;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Domain entity. Simple POJO.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.2.0
 * @since 1.2.0
 * 
 */

@Data
@Embeddable
public class AuditInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(insertable = false, updatable = false)
	private LocalDateTime creationDate; // DB automatically populates this through trigger
	
	@Column(insertable = false, updatable = false)
	private String creationUser; // DB automatically populates this through trigger
	
	@Column(updatable = false)
	private String creationAppUser; // App user, should be populated before INSERT
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime lastUpdateDate; // DB automatically populates this through trigger
	
	@Column(insertable = false, updatable = false)
	private String lastUpdateUser; // DB automatically populates this through trigger
	
	@Column
	private String lastUpdateAppUser; // App user, should be populated before every UPDATE
	
}
