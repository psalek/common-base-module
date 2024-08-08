package edu.miu.common.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.common.domain.SyncEvent;

@Repository
@Transactional
@ConditionalOnProperty(value = "common.sync.bootstrap", havingValue = "true")
public interface SyncEventRepository extends BaseRepository<SyncEvent, Integer> {

    @Query(
            "FROM SyncEvent as s WHERE s.endDateTime >= :startOfTheDay " +
            "AND s.endDateTime <= :endOfTheDay " +
            "AND s.sourceName = :sourceName " +
            "AND s.targetName = :targetName"
    )
    List<SyncEvent> findAllSyncEventsByDateAndLocation(LocalDateTime startOfTheDay, LocalDateTime endOfTheDay, String sourceName, String targetName);

    Page<SyncEvent> findAllBySourceName(String sourceName, Pageable pageable);

    @Query("SELECT s FROM SyncEvent s WHERE LOWER(s.sourceName) LIKE LOWER(CONCAT('%', :eventName, '%')) OR LOWER(s.targetName) LIKE LOWER(CONCAT('%', :eventName, '%'))")
    Page<SyncEvent> findAllBySourceOrTargetName(String eventName, Pageable pageable);


    @Query(value = "SELECT se.* FROM [Synchronization].[dbo].[SyncEvent] se " +
            "INNER JOIN ( " +
            "SELECT MAX(StartDateTime) as MaxDateTime, SourceName, TargetName " +
            "FROM [Synchronization].[dbo].[SyncEvent] " +
            "GROUP BY SourceName, TargetName) groupedse " +
            "ON se.SourceName = groupedse.SourceName AND se.TargetName = groupedse.TargetName " +
            "AND se.StartDateTime = groupedse.MaxDateTime " +
            "ORDER BY se.StartDateTime DESC, se.SourceName ASC",
            nativeQuery = true)
    List<SyncEvent> findAllLatestEventsBySourceAndTarget();


}
