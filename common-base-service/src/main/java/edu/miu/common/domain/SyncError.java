package edu.miu.common.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@Entity
@Table(name = "[Synchronization].[dbo].[SyncError]")
public class SyncError implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "SourceName", length = 200, nullable = false)
	private String sourceName;

	@Column(name = "TargetName", length = 200, nullable = false)
	private String targetName;

	@Column(name = "ErrorMessage", length = 4000, nullable = false)
	private String errorMessage;

	@Embedded
	private AuditData auditData = new AuditData();

}