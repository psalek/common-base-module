package edu.miu.common.service.contract;

import lombok.Data;

import java.io.Serializable;

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
public class SyncErrorResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String sourceName;

	private String targetName;

	private String errorMessage;

	private String createOnFormatted;

	private CommonAuditData auditData;

}