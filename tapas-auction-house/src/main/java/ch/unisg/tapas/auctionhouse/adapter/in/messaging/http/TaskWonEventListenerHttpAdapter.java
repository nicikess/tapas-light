package ch.unisg.tapas.auctionhouse.adapter.in.messaging.http;

import ch.unisg.tapas.auctionhouse.application.port.in.TaskWonEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.TaskWonEventHandler;
import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class TaskWonEventListenerHttpAdapter {

    private final TaskWonEventHandler taskWonEventHandler;

    @PostMapping(path = { "/taskwinner", "/taskwinner/" }, consumes = TaskJsonRepresentation.MEDIA_TYPE)
    public ResponseEntity<String> receiveWonTask(
        @RequestBody TaskJsonRepresentation taskJsonRepresentation)
    {
        var event = new TaskWonEvent(taskJsonRepresentation);
        var status = taskWonEventHandler.handleAuctionWon(event);

        HttpStatus responseStatusCode;

        switch (status) {
            case OK:
                responseStatusCode = HttpStatus.ACCEPTED;
                break;
            case CANNOT_EXECUTE:
            default:
                responseStatusCode = HttpStatus.NOT_ACCEPTABLE;
                break;
        }

        return new ResponseEntity<>(responseStatusCode);
    }
}
