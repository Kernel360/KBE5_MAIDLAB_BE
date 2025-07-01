package kernel.maidlab.admin.logs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogMonitoringService logMonitoringService;

    @GetMapping("/full")
    public ResponseEntity<ResponseDto<String>> getFullLog() {
        log.info("Admin request to get full log content");
        String logContent = logMonitoringService.getFullLogContent();
        return ResponseDto.success(logContent);
    }

    @GetMapping("/tail")
    public ResponseEntity<ResponseDto<String>> getTailLog(
            @RequestParam(defaultValue = "100") int lines) {
        log.info("Admin request to get tail log content with {} lines", lines);
        String logContent = logMonitoringService.getTailLogContent(lines);
        return ResponseDto.success(logContent);
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseDto<LogInfo>> getLogInfo() {
        log.info("Admin request to get log info");
        LogInfo info = new LogInfo();
        info.setWebSocketEndpoint("/admin/logs/stream");
        info.setCommands(new String[]{"get_full_log", "get_tail_log"});
        info.setDescription("Connect to WebSocket for real-time log monitoring");
        return ResponseDto.success(info);
    }

    public static class LogInfo {
        private String webSocketEndpoint;
        private String[] commands;
        private String description;

        public String getWebSocketEndpoint() { return webSocketEndpoint; }
        public void setWebSocketEndpoint(String webSocketEndpoint) { this.webSocketEndpoint = webSocketEndpoint; }

        public String[] getCommands() { return commands; }
        public void setCommands(String[] commands) { this.commands = commands; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
