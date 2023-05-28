package ch.unisg.tapas.auctionhouse.application.port.out;

public interface TaskAssignedEventPort {
    void handleTaskAssignedEvent(TaskAssignedEvent event);
}
