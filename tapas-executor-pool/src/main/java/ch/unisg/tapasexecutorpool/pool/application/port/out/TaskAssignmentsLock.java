package ch.unisg.tapasexecutorpool.pool.application.port.out;

public interface TaskAssignmentsLock {
    void lockTaskAssignments();
    void releaseTaskAssignments();
}
