package tapaslight.app.application.port.in;

/**
 * Implemented before the use case
 * External API to the use case
 * Serves as Input port
 * By calling sendMoney(), an tapas.adapter outside of our core can invoke this use case
 */

public interface SendMoneyUseCase {
    boolean sendMoney(SendMoneyCommand command);
}