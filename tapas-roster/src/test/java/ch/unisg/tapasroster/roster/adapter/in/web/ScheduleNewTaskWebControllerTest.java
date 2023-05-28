package ch.unisg.tapasroster.roster.adapter.in.web;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasroster.roster.application.port.in.ScheduleTaskCommand;
import ch.unisg.tapasroster.roster.application.port.in.ScheduleTaskUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ScheduleNewTaskWebController.class)
public class ScheduleNewTaskWebControllerTest {

    @MockBean
    private ScheduleTaskUseCase scheduleTaskUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testScheduleTask() throws Exception {

        var taskToSchedule = Task.createTaskWithNameAndType(
                new Task.TaskName("test-task-auction"),
                new Task.TaskType(Task.Type.RANDOMTEXT.name())
        );
        var scheduleTaskCommand = new ScheduleTaskCommand(taskToSchedule);

        Mockito.when(scheduleTaskUseCase.scheduleTask(eq(scheduleTaskCommand))).thenReturn(true);

        var taskToScheduleJson = TaskJsonRepresentation.serialize(taskToSchedule);

        mockMvc.perform(post("/schedule-task/")
                .contentType(TaskJsonRepresentation.MEDIA_TYPE)
                .content(taskToScheduleJson))
                .andExpect(status().isOk());

        then(scheduleTaskUseCase).should(times(1)).scheduleTask(eq(scheduleTaskCommand));
    }
}
