package ch.unisg.tapasexecutorpool.pool.application.service;

import ch.unisg.tapasexecutorpool.pool.application.port.out.TaskAssignmentsLock;
import org.springframework.stereotype.Component;

@Component
public class NoOpTaskAssignmentsLock implements TaskAssignmentsLock {
    @Override
    public void lockTaskAssignments() {
        // do nothing
    }

    @Override
    public void releaseTaskAssignments() {
        // do nothing
    }
}
