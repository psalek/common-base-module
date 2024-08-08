package edu.miu.common.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "[Synchronization].[dbo].[SyncEvent]")
public class SyncEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "SourceName", length = 200, nullable = false)
	private String sourceName;
	
	@Column(name = "TargetName", length = 200, nullable = false)
	private String targetName;
	
	@Column(name = "StartDateTime", nullable = false)
	private LocalDateTime startDateTime = LocalDateTime.now();
	
	@Column(name = "EndDateTime")
	private LocalDateTime endDateTime;
	
	@Column(name = "SourceCount", nullable = false)
	private Integer sourceCount = 0;
	
	@Column(name = "CreateCount", nullable = false)
	private Integer createCount = 0;
	
	@Column(name = "UpdateCount", nullable = false)
	private Integer updateCount = 0;
	
	@Column(name = "DeleteCount", nullable = false)
	private Integer deleteCount = 0;
	
	@Embedded
	private AuditInfo auditInfo = new AuditInfo();

	public SyncEvent(String sourceName, String targetName) {
		super();
		this.sourceName = sourceName;
		this.targetName = targetName;
	}
	
	public Integer incrementCreateCount() {
		return ++createCount;
	}
	
	public Integer incrementUpdateCount() {
		return ++updateCount;
	}
	
	public Integer incrementDeleteCount() {
		return ++deleteCount;
	}
	
	public void finish() {
		endDateTime = LocalDateTime.now();
	}
	
}
