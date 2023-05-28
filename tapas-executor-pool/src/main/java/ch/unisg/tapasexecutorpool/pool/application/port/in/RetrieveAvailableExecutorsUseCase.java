package ch.unisg.tapasexecutorpool.pool.application.port.in;

import ch.unisg.tapascommon.pool.domain.Executor;
import java.util.List;

public interface RetrieveAvailableExecutorsUseCase {
    List<Executor> retrieveAvailableExecutorsFromPool(RetrieveAvailableExecutorsQuery query);
}
