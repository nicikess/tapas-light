package ch.unisg.tapasexecutorpool.pool.adapter.in.web;

import ch.unisg.tapasexecutorpool.pool.application.service.ExecutorPoolLoader;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("!test")
@RequiredArgsConstructor
@RestController
public class DebugWebController {

    private final ExecutorPoolLoader executorPoolLoader;

    @GetMapping(path = "/executors/clear")
    public ResponseEntity<String> clearExecutors() {
        ExecutorPool.getTapasExecutorPool().clearAllExecutors();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/executors/load")
    public ResponseEntity<String> loadExecutors() {
        executorPoolLoader.loadExecutorsFromRepository();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
