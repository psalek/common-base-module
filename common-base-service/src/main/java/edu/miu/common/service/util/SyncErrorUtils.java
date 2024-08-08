package edu.miu.common.service.util;

import edu.miu.common.domain.SyncError;
import edu.miu.common.repository.SyncErrorRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(value = "common.sync.bootstrap", havingValue = "true")
public class SyncErrorUtils {

    private final SyncErrorRepository syncErrorRepository;

    public SyncErrorUtils(SyncErrorRepository syncErrorRepository) {
        this.syncErrorRepository = syncErrorRepository;
    }

    public void generateSyncError(String sourceServiceName, String targetServiceName, FeignException e) {

        SyncError syncError = new SyncError();
        syncError.setSourceName(sourceServiceName);
        syncError.setTargetName(targetServiceName);
        syncError.setErrorMessage(buildErrorLog(e));

        Optional<SyncError> savedSyncError = Optional.of(syncErrorRepository.save(syncError));

        savedSyncError.ifPresentOrElse(error -> log.debug("Sync Error saved successfully: {}", error.getId()),
                () -> log.error("Error saving Sync Error: {}", e.getMessage()));

    }


    private String buildErrorLog(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().getName()).append(": ").append(e.getMessage());

        StackTraceElement[] elements = e.getStackTrace();
        List<String> generalElements = new ArrayList<>();
        List<String> specificElements = new ArrayList<>();

        for (StackTraceElement element : elements) {
            String elementString = "\n\tat " + element.toString();
            if (element.getClassName().startsWith("edu.miu") && specificElements.size() < 10) {
                specificElements.add(elementString);
            } else if (generalElements.size() < 20 && specificElements.isEmpty()){
                generalElements.add(elementString);
            }
        }

        generalElements.forEach(sb::append);
        specificElements.forEach(sb::append);

        return sb.toString();
    }


}