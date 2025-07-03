package kernel.maidlab.admin.logs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogMonitoringService {

    @Value("${LOG_DIR:logs}")
    private String logDir;

    private volatile ScheduledExecutorService scheduler;
    private volatile long lastPosition = 0;

    public interface LogUpdateListener {
        void onLogUpdate(String newContent);
    }

    public String getFullLogContent() {
        try {
            Path logPath = getLogPath();
            if (!Files.exists(logPath)) {
                return "Log file not found at: " + logPath.toString();
            }
            
            List<String> lines = Files.readAllLines(logPath);
            return String.join("\n", lines);
        } catch (IOException e) {
            log.error("Error reading log file", e);
            return "Error reading log file: " + e.getMessage();
        }
    }

    public String getTailLogContent(int lines) {
        try {
            Path logPath = getLogPath();
            if (!Files.exists(logPath)) {
                return "Log file not found at: " + logPath.toString();
            }

            List<String> allLines = Files.readAllLines(logPath);
            int start = Math.max(0, allLines.size() - lines);
            List<String> tailLines = allLines.subList(start, allLines.size());
            
            return String.join("\n", tailLines);
        } catch (IOException e) {
            log.error("Error reading log file", e);
            return "Error reading log file: " + e.getMessage();
        }
    }

    public void startMonitoring(LogUpdateListener listener) {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                Path logPath = getLogPath();
                if (Files.exists(logPath)) {
                    lastPosition = Files.size(logPath);
                }
            } catch (IOException e) {
                log.error("Error initializing log monitoring", e);
            }
        });

        scheduler.scheduleWithFixedDelay(() -> {
            try {
                String newContent = checkForNewContent();
                if (!newContent.isEmpty()) {
                    listener.onLogUpdate(newContent);
                }
            } catch (Exception e) {
                log.error("Error monitoring log file", e);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void stopMonitoring() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        lastPosition = 0;
    }

    private String checkForNewContent() {
        try {
            Path logPath = getLogPath();
            if (!Files.exists(logPath)) {
                return "";
            }

            long currentSize = Files.size(logPath);
            if (currentSize <= lastPosition) {
                return "";
            }

            byte[] bytes = Files.readAllBytes(logPath);
            String newContent = new String(bytes, (int) lastPosition, (int) (currentSize - lastPosition));
            lastPosition = currentSize;

            return newContent;
        } catch (IOException e) {
            log.error("Error checking for new log content", e);
            return "";
        }
    }

    private Path getLogPath() {
        return Paths.get(logDir, "info.log");
    }
}