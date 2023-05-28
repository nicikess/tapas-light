package ch.unisg.tapasroster.roster.application.port.out;

import ch.unisg.tapascommon.common.SelfValidating;
import ch.unisg.tapascommon.tasks.domain.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = false)
@Value
public class ForwardTaskToAuctionHouseEvent extends SelfValidating<ForwardTaskToAuctionHouseEvent> {

    @Getter
    @NotNull
    Task task;

    @Getter
    @NotNull
    Timestamp deadline;

    public ForwardTaskToAuctionHouseEvent(Task task, Timestamp deadline) {
        this.task = task;
        this.deadline = deadline;
        this.validateSelf();
    }
}
