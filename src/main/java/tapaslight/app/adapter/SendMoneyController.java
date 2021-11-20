package tapaslight.app.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tapaslight.app.application.port.in.SendMoneyCommand;
import tapaslight.app.application.port.in.SendMoneyUseCase;
import tapaslight.app.domain.Account;
import tapaslight.app.domain.Money;

/**
 * - Reads parameters form the request path
 * - Puts them into a SendMoneyCommand and invokes the use case
 */

@RestController
@RequiredArgsConstructor

class SendMoneyController {

    private final SendMoneyUseCase sendMoneyUseCase;

    @PostMapping(path = "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    void sendMoney(
            @PathVariable("sourceAccountId") Long sourceAccountId,
            @PathVariable("targetAccountId") Long targetAccountId,
            @PathVariable("amount") Long amount) {

        SendMoneyCommand command = new SendMoneyCommand(
                new Account.AccountId(sourceAccountId),
                new Account.AccountId(targetAccountId),
                Money.of(amount));

        sendMoneyUseCase.sendMoney(command);
    }

}
