package ch.unisg.tapasroster.roster.application.port.in;

public interface ScheduleTaskUseCase {
    boolean scheduleTask(ScheduleTaskCommand command);
}
