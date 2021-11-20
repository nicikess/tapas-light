package tapaslight.app.application.service;

import lombok.RequiredArgsConstructor;
import tapaslight.app.application.port.in.SendMoneyCommand;
import tapaslight.app.application.port.in.SendMoneyUseCase;

/**
 * Implements the use case
 * Loads source and target account from DB
 * Locks accounts so that no other transaction can take place
 * Makes withdrawal and deposit and writes the new state back to the DB
 */

/*Component: Bean is injected in any components that need a
access to the SendMoneyUseCase input port without having
a dependency on the actual implementation*/

//@Transactional
@RequiredArgsConstructor

public class SendMoneyService implements SendMoneyUseCase {

    //private final LoadAccountPort loadAccountPort;
    //private final UpdateAccountStatePort updateAccountStatePort;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {

        //Account sourceAccount = loadAccountPort.loadAccount(
                //target oder source
                //command.getTargetAccountId());

        //updateAccountStatePort.updateActivities(sourceAccount);

        return true;
    }

}
