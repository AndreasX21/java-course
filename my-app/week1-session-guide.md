# Week 1 — Java OOP basics (mini-ledger)

**Duration:** 60 minutes  
**Audience:** Python + intermediate SWE; JDK, Maven, IDE, and a running Maven app already set up  
**Project:** `mini-ledger` (plain Java + Maven; no Spring yet)  
**Package base:** `com.example.ledger` (adjust if your `groupId` differs)

---

## Goals

By the end of the session, participants can:

1. Create a class with encapsulated fields and constructors (including chaining).
2. Define and use an `enum` (`Currency`) as a type-safe set of constants.
3. Explain why money uses `BigDecimal`, not `double`.
4. Define an interface and implement it.
5. Use inheritance (`extends`, `abstract`, `@Override`).
6. Recognize polymorphism (interface-typed variables; polymorphic lists).

**Not this week:** real multi-account transfer service, Collections deep dive, Streams, Spring, tests.

---

## Pre-work (participants — before session)

- [ ] JDK 17+ installed (`java -version`)
- [ ] Maven installed (`mvn -version`)
- [ ] IntelliJ (or similar) opened on the course Maven project
- [ ] `main` runs and prints something (Hello World is enough)

---

## Instructor prep (10 min)

- [ ] Same starter repo as participants
- [ ] Cheat copy of finished Week 1 code ready (branch or gist) for when timing slips
- [ ] Board/doc with four headings: **Currency · Account · Transaction · Repository**
- [ ] Confirm package path matches their `src/main/java` layout

**Opening line:**

> Today is OOP in Java: classes, constructors, interfaces, inheritance, polymorphism. We’ll use a tiny wallet ledger. No Spring yet. Follow along in the IDE; pause me if compile breaks.

---

## Target file tree (end of session)

```text
src/main/java/com/example/ledger/
├── App.java                          (or Main.java)
├── domain/
│   ├── Currency.java                 
│   ├── Account.java
│   ├── Transaction.java
│   ├── TransferTransaction.java
│   └── FeeTransaction.java
├── api/
│   └── AccountRepository.java
└── infra/
    └── InMemoryAccountRepository.java
```

---

## Minute-by-minute flow

### 0:00–0:05 — Orient + Python bridge

**Show**

- `src/main/java/.../App.java`
- `pom.xml` (don’t lecture; just “this is the build file”)

**Say**

- One public top-level class per file; packages = folders
- `main` ≈ `if __name__ == "__main__"`
- Types are checked at compile time

**Do**

- Everyone runs `main` once
- Thumbs up when it prints

---

### 0:05–0:20 — Enum, class, fields, constructors (`Currency` + `Account`)

**Create** `domain/Currency.java` first (2–3 min)

```java
package com.example.ledger.domain;

/**
 * Supported ledger currencies.
 * Enum = fixed, type-safe set of named constants (better than raw Strings).
 */
public enum Currency {
    USD,
    CLP
}
```

**Say**

- Enum ≈ Python `Enum`, but a real type the compiler checks
- Use `Currency.USD`, not `"USD"` strings, so typos fail at compile time
- Week 1 rule: **one currency per demo** (both accounts same); FX comes much later

**Create** `domain/Account.java`

```java
package com.example.ledger.domain;

import java.math.BigDecimal;

public class Account {
    private final String id;
    private final String ownerName;
    private final Currency currency;
    private BigDecimal balance;

    public Account(String id, String ownerName, Currency currency, BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("balance cannot be negative");
        }
        this.id = id;
        this.ownerName = ownerName;
        this.currency = currency;
        this.balance = balance;
    }

    /** New accounts start at zero. */
    public Account(String id, String ownerName, Currency currency) {
        this(id, ownerName, currency, BigDecimal.ZERO);
    }

    public String getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("insufficient funds");
        }
        balance = balance.subtract(amount);
    }

    @Override
    public String toString() {
        return "Account{id='%s', owner='%s', currency=%s, balance=%s}"
                .formatted(id, ownerName, currency, balance);
    }
}
```

**Teaching beats**

| Topic | Point to make |
|-------|----------------|
| `enum` | Closed set of values; `Currency.CLP` not `"CLP"` |
| `private` fields | Encapsulation (vs Python convention) |
| `final` on id/name/currency | Identity + currency immutable; balance can change |
| `this(...)` | Constructor chaining |
| `BigDecimal` | Never use `double`/`float` for money |
| `credit` / `debit` | Behavior lives on the type |

**Smoke test in `App`**

```java
Account a = new Account("acc-1", "Ana", Currency.CLP, new BigDecimal("100.00"));
System.out.println(a);
System.out.println(a.getCurrency()); // CLP
```

**Checkpoint:** everyone compiles and runs. Fix package/import issues before moving on.

---

### 0:20–0:35 — Interface + polymorphism (repository)

**Create** `api/AccountRepository.java`

```java
package com.example.ledger.api;

import com.example.ledger.domain.Account;

import java.util.Optional;

public interface AccountRepository {
    void save(Account account);

    Optional<Account> findById(String id);
}
```

**Create** `infra/InMemoryAccountRepository.java`

```java
package com.example.ledger.infra;

import com.example.ledger.api.AccountRepository;
import com.example.ledger.domain.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> store = new HashMap<>();

    @Override
    public void save(Account account) {
        store.put(account.getId(), account);
    }

    @Override
    public Optional<Account> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
```

**Wire in `App`**

```java
AccountRepository repo = new InMemoryAccountRepository(); // declared type = interface
repo.save(new Account("acc-1", "Ana", Currency.CLP, new BigDecimal("100.00")));
repo.findById("acc-1").ifPresent(System.out::println);
```

**Say**

> Polymorphism: the variable is `AccountRepository`, the object is `InMemoryAccountRepository`. Callers depend on the contract. Spring DI is this idea with annotations later.

**Python bridge:** interface ≈ protocol / ABC; `implements` is explicit.

#### Optional stretch (only if pace is good): second impl

```java
package com.example.ledger.infra;

import com.example.ledger.api.AccountRepository;
import com.example.ledger.domain.Account;

import java.util.Optional;

/** Tiny fake impl — same interface, different behavior. */
public class SingleAccountRepository implements AccountRepository {
    private final Account only;

    public SingleAccountRepository(Account only) {
        this.only = only;
    }

    @Override
    public void save(Account account) {
        // ignore for demo
    }

    @Override
    public Optional<Account> findById(String id) {
        return only.getId().equals(id) ? Optional.of(only) : Optional.empty();
    }
}
```

Swap the binding in `main` once to show both impls satisfy the same type.

---

### 0:35–0:50 — Inheritance + polymorphism (transactions)

**Create** `domain/Transaction.java`

```java
package com.example.ledger.domain;

import java.math.BigDecimal;

public abstract class Transaction {
    private final String id;
    private final BigDecimal amount;

    protected Transaction(String id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    /** Human-readable summary; subclasses must implement. */
    public abstract String describe();
}
```

**Create** `domain/TransferTransaction.java`

```java
package com.example.ledger.domain;

import java.math.BigDecimal;

public class TransferTransaction extends Transaction {
    private final String fromAccountId;
    private final String toAccountId;

    public TransferTransaction(
            String id,
            BigDecimal amount,
            String fromAccountId,
            String toAccountId
    ) {
        super(id, amount);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    @Override
    public String describe() {
        return "Transfer %s -> %s: %s".formatted(fromAccountId, toAccountId, getAmount());
    }
}
```

**Create** `domain/FeeTransaction.java`

```java
package com.example.ledger.domain;

import java.math.BigDecimal;

public class FeeTransaction extends Transaction {
    private final String accountId;

    public FeeTransaction(String id, BigDecimal amount, String accountId) {
        super(id, amount);
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    @Override
    public String describe() {
        return "Fee on %s: %s".formatted(accountId, getAmount());
    }
}
```

**Polymorphic loop in `App`**

```java
List<Transaction> txns = List.of(
        new TransferTransaction("tx-1", new BigDecimal("20.00"), "acc-1", "acc-2"),
        new FeeTransaction("tx-2", new BigDecimal("1.50"), "acc-1")
);

for (Transaction tx : txns) {
    System.out.println(tx.describe()); // runtime dispatch
}
```

**Say**

- `extends` = is-a; shared state/constructor in the parent
- `abstract` method = subclasses must implement
- Prefer **shallow** inheritance; use interfaces for capabilities (repository)
- `@Override` catches signature mistakes at compile time

**If behind at ~0:45:** skip `FeeTransaction`; only demo `TransferTransaction` + `describe()`.

---

### 0:50–0:58 — Full demo + recap

**Target `App` story**

1. Create `AccountRepository` (in-memory)
2. Save two accounts
3. Build a `List<Transaction>` with transfer + fee
4. Print accounts and each `describe()`

**Example `App.java`**

```java
package com.example.ledger;

import com.example.ledger.api.AccountRepository;
import com.example.ledger.domain.Account;
import com.example.ledger.domain.Currency;
import com.example.ledger.domain.FeeTransaction;
import com.example.ledger.domain.Transaction;
import com.example.ledger.domain.TransferTransaction;
import com.example.ledger.infra.InMemoryAccountRepository;

import java.math.BigDecimal;
import java.util.List;

public class App {
    public static void main(String[] args) {
        AccountRepository repo = new InMemoryAccountRepository();

        repo.save(new Account("acc-1", "Ana", Currency.CLP, new BigDecimal("100.00")));
        repo.save(new Account("acc-2", "Ben", Currency.CLP, new BigDecimal("50.00")));

        repo.findById("acc-1").ifPresent(System.out::println);
        repo.findById("acc-2").ifPresent(System.out::println);

        List<Transaction> txns = List.of(
                new TransferTransaction("tx-1", new BigDecimal("20.00"), "acc-1", "acc-2"),
                new FeeTransaction("tx-2", new BigDecimal("1.50"), "acc-1")
        );

        for (Transaction tx : txns) {
            System.out.println(tx.describe());
        }
    }
}
```

**Board recap**

1. **Enum** — fixed constants (`Currency.CLP`) instead of magic strings  
2. **Constructor** — how objects are created and validated  
3. **Interface** — contract; many implementations  
4. **Inheritance** — shared behavior, then specialize  
5. **Polymorphism** — one static type, many runtime behaviors  

Keep Q&A short: real debit+credit across accounts is Week 2.

---

### 0:58–1:00 — Homework

**Due before Week 2 (~30–45 min)**

1. Finish any classes not completed live (`Currency` + `Account.currency` should already be done).
2. `main`: two accounts **in the same** `Currency` (e.g. both `Currency.CLP`), one transfer + one fee, print all `describe()` lines.
3. Stretch: create a third account in the *other* currency and print it — still no cross-currency transfer (we reject that later).
4. README note (5–10 lines): *interface vs abstract class* in your own words; one sentence on why `Currency` is an enum.

**Not required:** a `TransferService` that actually moves balances (Week 2).

---

## Timing escape hatches

| Situation | Cut |
|-----------|-----|
| IDE / package friction | Drop `SingleAccountRepository` |
| OO discussion runs long | Drop `FeeTransaction` |
| Group is fast | Call `debit`/`credit` once in `main` after printing (still no full transfer service) |

---

## Common failures

| Symptom | Likely cause |
|---------|----------------|
| Cannot find symbol / wrong package | Class in default package or folder ≠ package declaration |
| Missing imports | `BigDecimal`, `List`, `Optional`, `Currency` |
| `Currency.Clp` / `"CLP"` | Enum constants are exact (`Currency.CLP`); not strings |
| `new AccountRepository()` | Interfaces cannot be instantiated |
| Compile error in subclass | Missing `super(...)` as first constructor statement |
| Someone uses `double` | Redirect to `BigDecimal` once; don’t debate |
| Account ctor arity errors | New signature needs `Currency` before/with `BigDecimal` |

---

## Done checklist

- [ ] `Currency` enum (`USD`, `CLP`)
- [ ] `Account` with `currency` field, constructors + `credit`/`debit`
- [ ] `AccountRepository` + `InMemoryAccountRepository`
- [ ] `Transaction` + `TransferTransaction` (+ `FeeTransaction` if time)
- [ ] `App` prints accounts (with currency) and polymorphic `describe()` lines
- [ ] Homework assigned

---

## Tease Week 2

> Next week: `TransferService` — real debit + credit, insufficient funds, still no Spring. Same interfaces; more behavior.

---

## Quick reference — what each concept mapped to

| Concept | Where we used it |
|---------|------------------|
| Enum | `Currency` (`USD`, `CLP`) on `Account` |
| Class + constructor | `Account`, `TransferTransaction`, `FeeTransaction` |
| Constructor chaining | `Account(id, owner, currency)` → `this(..., ZERO)` |
| Encapsulation | private fields, getters, mutators |
| Interface | `AccountRepository` |
| Implementation | `InMemoryAccountRepository` |
| Inheritance | `TransferTransaction` / `FeeTransaction` extend `Transaction` |
| Abstract type | `Transaction` + `describe()` |
| Polymorphism | `AccountRepository repo = ...`; `for (Transaction tx : txns)` |
