# Week 1 session

You build a small ledger and run it from `main`. Ten accounts start at 100 USD. The program creates 100 transfers, sorts them by time, and moves money from one account to the next. If the sender does not have enough, that transfer is skipped and the receiver is left unchanged.

Work in this order.

1. Currency, the checked exception, and the account classes.
2. The repository interface and its in-memory implementation.
3. The transaction record, the processor interface, and the processor.
4. `App.main`, which wires those pieces together.

Packages match folders under `src/main/java`.

```text
src/main/java/com/ledger/
├── app/App.java
├── account/BaseAccount.java
├── account/Account.java
├── exception/InsufficientBalanceException.java
├── model/CVCurrency.java
├── repo/IAccountRepository.java
├── repo/AccountRepository.java
├── transaction/TransactionDto.java
├── transaction/ITransactionProcessor.java
└── transaction/TransactionProcessor.java
```

One public class per file. The file name is the class name.

From the `my-app` directory, after `App` exists, run:

```bash
mvn -q compile exec:java
```

`-q` hides Maven's own log lines. `compile` rebuilds changed sources. `exec:java` starts `com.ledger.app.App`, which is already set as the main class in `pom.xml`.

## Currency

Create `src/main/java/com/ledger/model/CVCurrency.java`.

A currency has to be one of a fixed set. An enum gives you that set as real constants. `CVCurrency.USD` is a value the compiler checks. The string `"USD"` is not, so a typo in a string would compile.

Each constant carries an id and a display name. The constructor runs once for `USD`, once for `EUR`, and once for `CLP`.

```java
package com.ledger.model;

public enum CVCurrency {
    USD(1, "United States Dollar"),
    EUR(2, "Euro"),
    CLP(3, "Chilean Peso");

    private int id;
    private String name;

    CVCurrency(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```

The fields are private, so other classes read them through `getId` and `getName`. This week every account uses `CVCurrency.USD`. The enum still lists `EUR` and `CLP` so a later account can use another constant without a code change to the type.

### Recap

An enum is a closed set of constants. `CVCurrency` stores an integer id and a name on each constant. Private fields keep those values behind getters.

## The insufficient-balance exception

Create `src/main/java/com/ledger/exception/InsufficientBalanceException.java`.

`debit` needs a failure the caller cannot ignore. `InsufficientBalanceException` extends `Exception`, so it is a checked exception. Any method that throws it must declare `throws`, and the caller must catch it or declare `throws` too.

`IllegalArgumentException` would not force that choice. It extends `RuntimeException`, so the compiler lets a caller skip it.

```java
package com.ledger.exception;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
```

`super(message)` passes the text to `Exception`, which stores it for `getMessage`.

### Recap

A class that extends `Exception` is checked. The constructor chains to the superclass with `super`. Callers of `debit` must handle this type, or the program does not compile.

## The account base class

Create `src/main/java/com/ledger/account/BaseAccount.java`.

`BaseAccount` holds the data every account has: id, owner, currency, and balance. The class is abstract, so `new BaseAccount(...)` does not compile. You construct an `Account`, and the shared fields live in one place.

`id`, `ownerName`, and `currency` are `final`. After the constructor, those references do not change. `balance` is not `final`, because `credit` and `debit` replace it.

`credit` and `debit` are abstract. A subclass must implement both. `debit` declares `throws InsufficientBalanceException`, so every implementation and every caller through a `BaseAccount` reference has to deal with that checked exception.

`setBalance` is `protected`. `Account` can call it. A class in another package, such as `com.ledger.app`, cannot. The field stays private.

```java
package com.ledger.account;

import java.math.BigDecimal;
import java.util.UUID;

import com.ledger.model.CVCurrency;
import com.ledger.exception.InsufficientBalanceException;

public abstract class BaseAccount {
    private final UUID id;
    private final String ownerName;
    private final CVCurrency currency;
    private BigDecimal balance;

    public BaseAccount(UUID id, String ownerName, CVCurrency currency, BigDecimal balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.currency = currency;
        this.balance = balance;
    }

    /**
     * Credit the account by the given amount.
     *
     * @param amount the amount to credit
     */
    public abstract void credit(BigDecimal amount);

    /**
     * Debit the account by the given amount. If the account has insufficient
     * balance, throw an InsufficientBalanceException.
     *
     * @param amount the amount to debit
     * @throws InsufficientBalanceException if the account has insufficient balance
     */
    public abstract void debit(BigDecimal amount) throws InsufficientBalanceException;

    protected void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public CVCurrency getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public String toString() {
        return "Account [id=" + id.toString().substring(0, 8) + ", ownerName=" + ownerName + ", currency=" + currency
                + ", balance=" + balance
                + "]";
    }
}
```

Balance is a `BigDecimal`, not a `double`. A `double` cannot store most decimal fractions exactly. `0.10 + 0.20` prints `0.30000000000000004`. Money uses `BigDecimal` so `add` and `subtract` keep the decimal value you gave them.

`toString` prints the first 8 characters of the UUID. The full id is 36 characters, and the log line only needs enough to tell accounts apart.

### Recap

`BaseAccount` is an abstract class with private state. `final` locks id, owner, and currency. Balance stays mutable. Abstract `credit` and `debit` force the subclass to supply the behavior. `debit` advertises a checked exception. `protected` opens `setBalance` to the subclass only. `BigDecimal` stores the money. `@Override` on `toString` tells the compiler this method replaces `Object.toString`.

## The account

Create `src/main/java/com/ledger/account/Account.java`.

`Account` is the concrete type you can construct. It extends `BaseAccount`, so it inherits the fields and the getters. It does not get a copy of them.

The short constructor is the one `main` uses. `this(...)` chains to the full constructor, generates an id, and starts the balance at zero. The chaining call has to be the first statement.

`super(...)` in the full constructor calls `BaseAccount`'s constructor, which assigns the fields. A subclass constructor cannot assign `id` itself, because that field is private in the parent.

```java
package com.ledger.account;

import java.math.BigDecimal;
import java.util.UUID;

import com.ledger.model.CVCurrency;
import com.ledger.exception.InsufficientBalanceException;

public class Account extends BaseAccount {

    public Account(UUID id, String ownerName, CVCurrency currency, BigDecimal balance) {
        super(id, ownerName, currency, balance);
    }

    public Account(String ownerName, CVCurrency currency) {
        this(UUID.randomUUID(), ownerName, currency, BigDecimal.ZERO);
    }

    @Override
    public void credit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        setBalance(getBalance().add(amount));
    }

    @Override
    public void debit(BigDecimal amount) throws InsufficientBalanceException {
        if (getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    this.toString() + " has insufficient balance to debit " + amount);
        }
        setBalance(getBalance().subtract(amount));
    }
}
```

`credit` rejects a zero or negative amount with `IllegalArgumentException`. That exception is unchecked, because a non-positive credit is a programming mistake, not a case the transfer loop is expected to recover from.

`debit` compares the balance with `compareTo`. Lower than the amount means the account cannot pay. The method throws the checked exception and leaves the balance as it was. `subtract` runs only after that test.

`debit` declares `throws InsufficientBalanceException` because it throws that checked type. It does not catch the exception. Catching it here would let the caller continue as if the debit had succeeded.

### Recap

`extends` makes `Account` a `BaseAccount`. `this` chains constructors. `super` assigns the parent fields. `@Override` marks the two required methods. `credit` changes balance with `BigDecimal.add`. `debit` throws the checked exception instead of changing the balance.

## The repository interface

Create `src/main/java/com/ledger/repo/IAccountRepository.java`.

Callers need to save an account and load it later. They do not need to know that the storage is a `HashMap`. The interface is that contract. `main` will declare its variable as `IAccountRepository`, and the object will be an `AccountRepository`.

`findById` returns `Optional`. The id might not be in the map. `Optional` makes that case a value, `Optional.empty`, instead of a null you can forget to check.

The methods take `BaseAccount`, not `Account`. A repository can store any subclass. Today the only subclass is `Account`.

```java
package com.ledger.repo;

import java.util.Optional;
import java.util.UUID;

import com.ledger.account.BaseAccount;

public interface IAccountRepository {

    void save(BaseAccount account);

    Optional<BaseAccount> findById(UUID id);

    void deleteById(UUID id);
}
```

`main` uses `save`. The processor uses `findById`. Nothing in this session calls `deleteById`. The interface still lists it, so an implementation has to provide a way to remove an account.

### Recap

An interface names operations and does not store the accounts. `Optional` is the return type for a lookup that can miss. The parameter type is the abstract account, so the repository is not tied to one subclass.

## The in-memory repository

Create `src/main/java/com/ledger/repo/AccountRepository.java`.

This is the class that actually stores accounts. `implements IAccountRepository` means the compiler requires `save`, `findById`, and `deleteById`.

The map key is the account id. `save` puts the same object the caller already holds. Later `credit` and `debit` mutate that object, so a find returns the updated balance without a second save.

```java
package com.ledger.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.ledger.account.BaseAccount;

public class AccountRepository implements IAccountRepository {

    private final Map<UUID, BaseAccount> accounts;

    public AccountRepository() {
        this.accounts = new HashMap<>();
    }

    @Override
    public void save(BaseAccount account) {
        accounts.put(account.getId(), account);
    }

    @Override
    public Optional<BaseAccount> findById(UUID id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public void deleteById(UUID id) {
        accounts.remove(id);
    }
}
```

`Map.get` returns null when the key is absent. `Optional.ofNullable` turns that null into `Optional.empty`, and turns a found account into `Optional.of`.

The field type is `Map`. The object is a `HashMap`. The rest of this class depends on `Map`, so a different map implementation can replace `HashMap` in the constructor.

### Recap

`implements` fills in the interface. A `HashMap` stores one object per UUID. `put` saves it, `get` loads it, and `remove` deletes it. `Optional.ofNullable` converts a missing entry into an empty `Optional`. The field is `final`, and the constructor assigns the map once.

## The transaction record

Create `src/main/java/com/ledger/transaction/TransactionDto.java`.

A transfer needs five facts: its own id, the sender, the receiver, the amount, and the time. None of those change after the transfer is created. A record declares that shape in the constructor header. The compiler generates the constructor, the accessors, `equals`, `hashCode`, and a `toString`.

The accessors are `id()`, `senderAccountId()`, `receiverAccountId()`, `amount()`, and `timestamp()`. There are no setters.

```java
package com.ledger.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDto(
        UUID id,
        UUID senderAccountId,
        UUID receiverAccountId,
        BigDecimal amount,
        LocalDateTime timestamp) {

    @Override
    public String toString() {
        return "TransactionDto [id=" + id.toString().substring(0, 8) + ", senderAccountId="
                + senderAccountId.toString().substring(0, 8) + ", receiverAccountId="
                + receiverAccountId.toString().substring(0, 8) + ", amount=" + amount + ", timestamp="
                + timestamp + "]";
    }
}
```

The generated `toString` would print three full UUIDs. This override keeps the same fields and shortens each id to 8 characters, which is the line you see when a transfer fails.

This record is data. It does not debit or credit anyone. The processor does that.

### Recap

A record is a class with final components and generated accessors. `TransactionDto` overrides `toString`. The amount is a `BigDecimal`. The time is a `LocalDateTime`, which sorts chronologically.

## The processor interface

Create `src/main/java/com/ledger/transaction/ITransactionProcessor.java`.

`main` should depend on "something that processes transfers", the same way it depends on `IAccountRepository`. This interface is that contract. There are two operations. One processes a single transfer. The other processes a list.

Each method has a default body that throws `UnsupportedOperationException`. A default body lets a class compile even if it overrides only one of the two methods. Calling the method it skipped still fails, at run time, with that exception. `TransactionProcessor` overrides both, so those default bodies do not run in this program.

```java
package com.ledger.transaction;

import java.util.List;

public interface ITransactionProcessor {
    default void processTransaction(TransactionDto transactionDto) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default void processTransactions(List<TransactionDto> transactionDtos) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
```

`UnsupportedOperationException` is unchecked. The defaults can throw it without a `throws` clause.

### Recap

An interface can give a method a default body. This contract has two operations, and the default body fails if a class leaves one unimplemented. `main` will use the interface type, and `TransactionProcessor` will be the object.

## The processor

Create `src/main/java/com/ledger/transaction/TransactionProcessor.java`.

The processor applies transfers to accounts. It does not create the repository. The constructor receives an `IAccountRepository`. `main` chooses the implementation and passes it in. The field is `final`, so the processor uses that same repository for its whole life.

`processTransactions` sorts the list, then processes each element. `List.sort` changes the list you passed in. `Comparator.comparing(TransactionDto::timestamp)` puts the earliest time first. `thenComparing(TransactionDto::id)` breaks ties by id, so two transfers with the same timestamp always run in the same order. Time is the primary key because a later deposit must not be applied before an earlier payment.

```java
package com.ledger.transaction;

import com.ledger.repo.IAccountRepository;
import com.ledger.exception.InsufficientBalanceException;
import java.util.List;
import java.util.Comparator;

public class TransactionProcessor implements ITransactionProcessor {

    private final IAccountRepository accountRepository;

    public TransactionProcessor(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void processTransaction(TransactionDto transactionDto) {
        var senderAccount = accountRepository.findById(transactionDto.senderAccountId());
        var receiverAccount = accountRepository.findById(transactionDto.receiverAccountId());

        if (senderAccount.isEmpty() || receiverAccount.isEmpty()) {
            System.out.println("Sender or receiver account not found");
            return;
        }

        try {
            senderAccount.get().debit(transactionDto.amount());
            receiverAccount.get().credit(transactionDto.amount());
        } catch (InsufficientBalanceException e) {
            System.out.println("Transaction " + transactionDto.toString() + " failed");
        }
    }

    @Override
    public void processTransactions(List<TransactionDto> transactionDtos) {
        // sort by timestamp and accounts
        transactionDtos.sort(Comparator.comparing(TransactionDto::timestamp)
                .thenComparing(TransactionDto::id));

        transactionDtos.stream().forEach(this::processTransaction);
    }
}
```

`findById` returns an `Optional`. `isEmpty` means that id was never saved. The method prints a line and returns before any balance changes.

`debit` runs first. It throws `InsufficientBalanceException` when the sender cannot pay, and the `catch` prints the failure. `credit` is the next statement, so it does not run when `debit` throws. The receiver does not gain money the sender did not lose.

`senderAccount` is a `BaseAccount` inside the `Optional`. The object is an `Account`. The call `debit` runs `Account.debit`, because Java picks the method from the object's class, not from the variable's class.

`TransactionDto::timestamp` is a method reference. It means the same as a lambda that calls `timestamp()` on each element. `this::processTransaction` passes the processor's own method to `forEach`.

### Recap

The processor depends on the repository interface, and the constructor receives the object. `sort` orders the batch by timestamp, then by id. `Optional` stops a transfer when an account is missing. The `try` block debits before it credits, and the `catch` handles the checked exception from `debit`. The variable type is `BaseAccount`. The method that runs is `Account.debit`.

## Wire the pieces in main

Replace `src/main/java/com/ledger/app/App.java` with the program below.

`main` is the entry point. It is the only place that decides which classes get constructed. The repository variable is an `IAccountRepository`. The processor variable is an `ITransactionProcessor`. The objects are `AccountRepository` and `TransactionProcessor`. The processor is given the repository it should read.

```java
package com.ledger.app;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import com.ledger.account.Account;
import com.ledger.account.BaseAccount;
import com.ledger.model.CVCurrency;
import com.ledger.repo.AccountRepository;
import com.ledger.repo.IAccountRepository;
import com.ledger.transaction.ITransactionProcessor;
import com.ledger.transaction.TransactionProcessor;
import com.ledger.transaction.TransactionDto;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        IAccountRepository accountRepository = new AccountRepository();
        ITransactionProcessor transactionProcessor = new TransactionProcessor(accountRepository);

        List<BaseAccount> accounts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            accounts.add(new Account("John_Dow_" + i, CVCurrency.USD));
        }

        for (var account : accounts) {
            account.credit(BigDecimal.valueOf(100));
            accountRepository.save(account);
        }

        List<TransactionDto> transactions = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            transactions.add(new TransactionDto(
                    UUID.randomUUID(),
                    accounts.get(i % accounts.size()).getId(),
                    accounts.get((i + 1) % accounts.size()).getId(),
                    BigDecimal.valueOf(Math.random() * 35),
                    LocalDateTime.of(2026, 1, 1, 0, 0, 0).plusDays((int) Math.floor(Math.random() * 365))));
        }

        transactionProcessor.processTransactions(transactions);

        accounts.stream().forEach(System.out::println);
    }
}
```

The account list is declared as `List<BaseAccount>` and built as an `ArrayList`. Each element is an `Account` from the short constructor, so it starts at zero with a random UUID and the name `John_Dow_0` through `John_Dow_9`. `credit` then sets the balance to 100. `BigDecimal.valueOf(100)` takes an integer, so that starting balance is the exact decimal 100. `save` puts the same object into the map.

The transfer loop builds a ring. Transfer `i` sends from account `i % 10` to account `(i + 1) % 10`. Account 9 pays account 0. The amount is `Math.random() * 35`, a `double` from 0 up to but not including 35, wrapped with `BigDecimal.valueOf`. That wrapper keeps the `double` rounding, so these demo amounts are not exact two-place decimals. The timestamp is 1 January 2026 plus a random number of days from 0 through 364.

`processTransactions` sorts that list and applies it. Some lines will say a transaction failed. Those senders have already paid earlier transfers, so the balance is below the next amount. The accounts were not short at the start. They became short because the batch runs oldest first.

The last loop prints each account. `var` in the credit loop asks the compiler to use the element type, `BaseAccount`.

Run it.

```bash
mvn -q compile exec:java
```

You should see zero or more `Transaction ... failed` lines, then ten `Account [...]` lines. Failed transfers keep the shortened ids from `TransactionDto.toString`. Account lines come from `BaseAccount.toString`.

### Recap

`main` constructs the repository and the processor and types both variables as interfaces. The account list is typed as `List<BaseAccount>` and holds `Account` objects. Constructor chaining starts each account at zero, and `credit` loads the starting 100. The transfer loop builds the batch. The processor sorts and applies it. `System.out::println` is a method reference that prints each account.

## Principles applied in this session

### Files and packages

Each public class lives in its own file. The folder under `src/main/java` is the package name.

### Account state

`CVCurrency` is the closed set `USD`, `EUR`, and `CLP`. Each constant holds a private id and name, read through getters.

Account fields are private. Other classes use `getId`, `getOwnerName`, `getCurrency`, and `getBalance`. `setBalance` is `protected`, so a subclass is what changes the balance.

Id, owner, currency, the repository map, and the processor's repository field are `final`. Balance stays assignable because `credit` and `debit` replace it.

`BaseAccount` assigns the fields. `Account(UUID, String, CVCurrency, BigDecimal)` calls that constructor with `super`. `Account(String, CVCurrency)` calls the longer constructor with `this`, a new UUID, and `BigDecimal.ZERO`.

Balance and transfer amount use `BigDecimal`. `credit` adds. `debit` subtracts. The starting balance uses `BigDecimal.valueOf(100)`. The random demo amounts pass through a `double` first.

### Inheritance

`BaseAccount` cannot be constructed. It holds the shared fields and leaves `credit` and `debit` abstract.

`Account extends BaseAccount` and implements both abstract methods. `@Override` makes a wrong signature a compile error.

### Checked exceptions

`InsufficientBalanceException extends Exception`. `debit` declares `throws` and throws it when the balance is too low. `TransactionProcessor` catches that type. A failed debit does not continue into `credit`. `credit` uses unchecked `IllegalArgumentException` for a non-positive amount.

### Interfaces and polymorphism

`IAccountRepository` lists `save`, `findById`, and `deleteById`. `ITransactionProcessor` lists the two process methods and gives each a default body that throws `UnsupportedOperationException`.

`AccountRepository implements IAccountRepository` and stores accounts in a `HashMap`. `TransactionProcessor implements ITransactionProcessor`.

`IAccountRepository accountRepository = new AccountRepository()` and `ITransactionProcessor transactionProcessor = new TransactionProcessor(...)` keep the caller on the contract. `List<BaseAccount>` holds `Account` objects. Calling `debit` on an element runs `Account.debit`. `Map` is the field type and `HashMap` is the object. `List` is the field type and `ArrayList` is the object.

`findById` returns `Optional.ofNullable`. The processor checks `isEmpty` before it touches a balance.

The processor does not create a repository. `main` passes one into the constructor.

### Records

`TransactionDto` fixes five components at construction and overrides `toString`.

`Comparator.comparing(TransactionDto::timestamp).thenComparing(TransactionDto::id)` sorts oldest first, then by id. `List.sort` reorders the caller's list.
