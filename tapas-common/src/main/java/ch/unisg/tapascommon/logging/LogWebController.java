package ch.unisg.tapascommon.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
public class LogWebController {

    private final ByteArrayOutputStream logMessageStream = new ByteArrayOutputStream();

    @EventListener(ApplicationReadyEvent.class)
    public void initializeWebLogging() {
        final var context = (LoggerContext) LogManager.getContext(false);
        final var config = context.getConfiguration();
        var layout = PatternLayout.newBuilder()
            .withPattern(PatternLayout.SIMPLE_CONVERSION_PATTERN).withFooter("\n").build();
        var appender = OutputStreamAppender.createAppender(
            layout, null, logMessageStream, "web_log_appender", false, true);
        appender.start();
        config.addAppender(appender);
        context.getRootLogger().addAppender(context.getConfiguration().getAppender(appender.getName()));
        context.updateLoggers();
    }

    @GetMapping(path = "/logs")
    public ResponseEntity<String> getAllLogMessages() {
        return new ResponseEntity<>(logMessageStream.toString(), HttpStatus.OK);
    }
}
