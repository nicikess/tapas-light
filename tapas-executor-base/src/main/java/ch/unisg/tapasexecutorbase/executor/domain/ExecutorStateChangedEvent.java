package ch.unisg.tapasexecutorbase.executor.domain;

import ch.unisg.tapascommon.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Value
public class ExecutorStateChangedEvent extends SelfValidating<ExecutorStateChangedEvent> {
    @Getter
    @NotNull
    String id;

    @Getter
    @NotNull
    String state;

    public ExecutorStateChangedEvent(String id, String state) {
        this.id = id;
        this.state = state;
        this.validateSelf();
    }
}
