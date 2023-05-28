package ch.unisg.tapasroster.roster.application.port.out;

public interface QueryAvailableExecutorsFromPoolEventHandler {
    boolean retrieveAvailableExecutors(QueryAvailableExecutorsFromPoolEvent event);
}
