package ch.unisg.tapastasks.tasks.application.service;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.application.port.in.RetrieveTaskFromTaskListQuery;
import ch.unisg.tapastasks.tasks.application.port.in.RetrieveTaskFromTaskListUseCase;
import ch.unisg.tapastasks.tasks.application.port.out.LoadTaskPort;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Transactional
@Service("RetrieveTaskFromTaskList")
public class RetrieveTaskFromTaskListService implements RetrieveTaskFromTaskListUseCase {

    private static final Logger LOGGER = LogManager.getLogger(RetrieveTaskFromTaskListService.class);

    private final LoadTaskPort loadTaskFromRepositoryPort;

    @Override
    public Optional<Task> retrieveTaskFromTaskList(RetrieveTaskFromTaskListQuery query) {
        TaskList taskList = TaskList.getTapasTaskList();
        Optional<Task> task = taskList.retrieveTaskById(query.getTaskId());
        Optional<Task> taskFromRepo = Optional.ofNullable(loadTaskFromRepositoryPort.loadTask(query.getTaskId(), taskList.getTaskListName()));

        if (taskFromRepo.isPresent()) {
            LOGGER.info("Retrieved Task from Repository");
            return taskFromRepo;
        }

        LOGGER.info("Retrieved Task from Cache");
        return task;
    }
}
