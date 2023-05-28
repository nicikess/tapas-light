package ch.unisg.tapasroster.roster.adapter.in.web;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapasroster.roster.application.port.in.ScheduleTaskCommand;
import ch.unisg.tapasroster.roster.application.port.in.ScheduleTaskUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;

@RequiredArgsConstructor
@RestController
public class ScheduleNewTaskWebController {

    private final ScheduleTaskUseCase scheduleTaskUseCase;

    @PostMapping(path = { "/schedule-task", "/schedule-task/" }, consumes = { TaskJsonRepresentation.MEDIA_TYPE })
    public ResponseEntity<Void> scheduleNewTask(@RequestBody TaskJsonRepresentation task) {
        try {
            var command = new ScheduleTaskCommand(task.deserialize());
            var isScheduled = scheduleTaskUseCase.scheduleTask(command);
            return isScheduled ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
