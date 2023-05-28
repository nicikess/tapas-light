package ch.unisg.tapastasks.tasks.adapter.out.persistence.mongodb;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.application.port.out.AddTaskPort;
import ch.unisg.tapastasks.tasks.application.port.out.LoadTaskPort;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements AddTaskPort, LoadTaskPort {

    private static final Logger LOGGER = LogManager.getLogger(TaskPersistenceAdapter.class);

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public void addTask(Task task) {
        try {
            var mongoTaskDocument = taskMapper.mapToMongoDocument(task);
            taskRepository.save(mongoTaskDocument);
            LOGGER.info("Saved Task to MongoDB");
        } catch (Exception e) {
            LOGGER.warn("Failed to add Task to MongoDB");
        }
    }

    @Override
    public Task loadTask(Task.TaskId taskId, TaskList.TaskListName taskListName) {
        try{
            var mongoTaskDocument = taskRepository.findByTaskId(taskId.getValue(),taskListName.getValue());
            LOGGER.info("Loaded Task from MongoDB");
            return taskMapper.mapToDomainEntity(mongoTaskDocument);
        } catch (Exception e) {
            LOGGER.warn("Failed to load Task from MongoDB");
        }
        return null;
    }
}
