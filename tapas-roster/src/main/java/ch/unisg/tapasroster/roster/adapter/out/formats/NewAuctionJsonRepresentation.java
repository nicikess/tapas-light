package ch.unisg.tapasroster.roster.adapter.out.formats;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapascommon.tasks.domain.Task;

import java.sql.Timestamp;

public class NewAuctionJsonRepresentation {
    public static final String MEDIA_TYPE = "application/auction+json";

    public static String serialize(Task task, Timestamp deadline) {

        // {
        //  "taskUri" : "http://example.org",
        //  "taskType" : "taskType1",
        //  "deadline" : "2021-12-24 12:00:00"
        // }

        return "{\n" +
                "  \"taskUri\": \"" +
                ServiceConfiguration.getTaskListAddress() + "/tasks/" + task.getTaskId().getValue() +
                "\"," +
                "  \"taskType\": \"" +
                task.getTaskType().getValue() +
                "\"," +
                "  \"deadline\": \"" +
                deadline.toString() +
                "\"\n" +
                "}";
    }
}
