package ch.unisg.tapasexecutorbase.executor.adapter.in.web;

import ch.unisg.tapasexecutorbase.executor.application.port.in.RegisterExecutorToPoolCommand;
import ch.unisg.tapasexecutorbase.executor.application.port.in.RegisterExecutorToPoolUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RegisterExecutorToPoolWebController {

    private static final Logger LOGGER = LogManager.getLogger(RegisterExecutorToPoolWebController.class);

    private final RegisterExecutorToPoolUseCase registerExecutorToPoolUseCase;

    @GetMapping("/register")
    public ResponseEntity<String> registerToPool() {
        LOGGER.info("Registering to Executor Pool");
        var ok = registerExecutorToPoolUseCase.registerThisExecutorToPool(new RegisterExecutorToPoolCommand());
        return ok ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
