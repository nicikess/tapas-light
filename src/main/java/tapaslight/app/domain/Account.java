package tapaslight.app.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class Account {

    @Getter private final AccountId id;

    //Balance at the start of the activity window
    @Getter private final Money balance;

    public boolean withdraw(Money money, AccountId targetAccountId) {

        this.balance.minus(money);
        return true;

    }

    public boolean deposit(Money money, AccountId sourceAccountId) {

        this.balance.plus(money);
        return true;
    }

    /**
     * Creates an {@link Account} entity with an ID. Use to reconstitute a persisted entity.
     */
    public static Account withId(
            AccountId accountId,
            Money baselineBalance) {
        return new Account(accountId, baselineBalance);
    }

    @Value
    public static class AccountId {
        private Long value;
    }

}

