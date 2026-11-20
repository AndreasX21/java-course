# Week 2 session

You already write Python. A Python `list`, `set`, or `dict` is a single type. In Java the variable and the object can have two different types. The type of the variable is an interface. The interface lists the methods you are allowed to call. The object is a class that provides those methods.

```java
List<String> words = new ArrayList<>();
```

`words` has type `List<String>`. `new ArrayList<>()` chooses which class stores the elements. `add` and `get` are methods on `List`. Any code that receives `words` compiles against `List`. That code keeps compiling if the `new` expression changes to another `List` class. Week 1 did the same thing with `IAccountRepository accountRepository = new AccountRepository()`. The variable had the interface type. `new` picked the class, in one place.

`Set`, `Queue`, and `Map` work the same way. This week is those four interfaces. A stream is a pipeline you run over a collection once. You collect the result back into one of the four. You write that version of Fizz Buzz at the end.

Work in this order.

1. One short program that shows what `List`, `Set`, `Queue`, and `Map` do.
2. One LeetCode problem for each interface.
3. Lambdas and method references on a list.
4. Fizz Buzz written again as a stream.
5. The methods on `com.ledger.exercises.Exercises`, written with streams.

Put each program in its own file under `src/main/java`. One public class per file. The file name is the class name.

```text
src/main/java/com/ledger/collections/
├── list/FizzBuzz.java
├── set/ContainsDuplicate.java
├── queue/RecentCounter.java
└── map/TwoSum.java
```

From the `week-2-collections-streams` directory, run a class with:

```bash
mvn -q compile exec:java -Dexec.mainClass=com.ledger.collections.list.FizzBuzz
```

`-q` hides Maven's own log lines. `compile` rebuilds changed sources. `exec:java` starts a `main`. `pom.xml` already points that plugin at `com.ledger.app.App`. `-Dexec.mainClass` points it at the class you just wrote. Swap the class name for the program you want to run.

## The four interfaces

`List`, `Set`, and `Queue` extend `Collection`. `Map` does not extend `Collection`.

```text
Collection
├── List      ArrayList     ordered, duplicates allowed, index access
├── Set       HashSet       no duplicates, no index
└── Queue     ArrayDeque    add at the back, poll from the front

Map           HashMap       lookup by key
```

`ArrayDeque` implements `Deque`. `Deque` extends `Queue`. You can store an `ArrayDeque` in a `Queue` variable. `Queue` provides `add`, `poll`, and `peek`. `Deque` also provides `push` and `pop`.

These match types you already use. `List` is a `list`. `Set` is a `set`. `Queue` is a `collections.deque` used with `append` and `popleft`. `Map` is a `dict`. A `List<String>` rejects an `int` at compile time. A Python list accepts that `int`.

Declare the variable with the interface, and construct it with the class:

```java
List<String> words = new ArrayList<>();
Set<Integer> seen = new HashSet<>();
Queue<Integer> calls = new ArrayDeque<>();
Map<Integer, Integer> indexByValue = new HashMap<>();
```

Code that receives `words`, `seen`, `calls`, or `indexByValue` depends on `List`, `Set`, `Queue`, and `Map`. Switching `HashMap` for another `Map` class is a change on the `new` expression.

`HashSet` and `HashMap` compare elements with `equals` and `hashCode`. `String` and `Integer` already implement both. The keys and set elements in this session are strings and integers. `HashSet` compares a class you write by identity until you add `equals` and `hashCode`. Until then, two objects with the same fields both stay in the set.

`Map` has no `stream()` method. A pipeline over a map starts at `values()` or `entrySet()`. Both are collections.

### Recap

`List`, `Set`, and `Queue` are collections. `Map` is not a collection. The interface is the variable type. `ArrayList`, `HashSet`, `ArrayDeque`, and `HashMap` are the objects. A pipeline over a map starts at `values()` or `entrySet()`.

## One behavior each

Replace `src/main/java/com/ledger/app/App.java` with the program below. Each block calls one method and prints the result. You see what that interface does before you use it in a problem. This file is the demo. The four problems go in files under `com.ledger.collections`. `ListOperations.java` goes there too. Without `-Dexec.mainClass`, `mvn -q compile exec:java` still starts `com.ledger.app.App`. That class is `mainClass` in `pom.xml`.

```java
package com.ledger.app;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        List<String> fees = new ArrayList<>();
        fees.add("fee");
        fees.add("fee");
        System.out.println(fees);

        Set<String> seenIds = new HashSet<>();
        seenIds.add("acc-1");
        seenIds.add("acc-1");
        System.out.println(seenIds.size());

        Queue<String> outbound = new ArrayDeque<>();
        outbound.add("first");
        outbound.add("second");
        System.out.println(outbound.poll());
        System.out.println(outbound);

        Map<String, Integer> balances = new HashMap<>();
        balances.put("acc-1", 100);
        System.out.println(balances.get("acc-1"));
    }
}
```

Run it.

```bash
mvn -q compile exec:java
```

`fees` prints `[fee, fee]`. A list keeps both adds, in insertion order. `seenIds.size()` prints `1`. The second `add` finds `"acc-1"` already present and leaves the set unchanged. `poll` prints `first` and removes it, so the queue then prints `[second]`. `get("acc-1")` prints `100`. That is a lookup by key, not a scan of the entries.

`Set.add` returns a `boolean`. The result is `true` when the set changed. The result is `false` when the value was already there. Python's `set.add` returns `None`. The check has to be a separate `in` test. The Contains Duplicate solution below is that boolean.

`ArrayDeque` rejects `null`. `poll` on an empty deque returns `null`. That `null` means the deque is empty. A stored `null` cannot be in the deque. Python's `deque` can hold `None`.

`Map.get` returns `null` when the key is absent. `List.get` is an index. `Map.get` is a key. Python uses `[]` for both.

### Recap

A list keeps duplicates and order. A set keeps one copy of a repeated element. A queue returns the oldest element and removes it. `Map.get` returns the value for a key. The four variables are interfaces. The four objects are `ArrayList`, `HashSet`, `ArrayDeque`, and `HashMap`.

## List: Fizz Buzz

Create `src/main/java/com/ledger/collections/list/FizzBuzz.java`.

This is LeetCode 412. Given `n`, return the words for `1` through `n`, in that order. A multiple of 3 and 5 is `"FizzBuzz"`. A multiple of 3 is `"Fizz"`. A multiple of 5 is `"Buzz"`. Anything else is the number as a string. `"Fizz"` can appear many times. Position matters. The result is a `List`. A `Set` would drop the repeated words and the order.

```java
package com.ledger.collections.list;

import java.util.ArrayList;
import java.util.List;

public class FizzBuzz {
    public List<String> fizzBuzz(int n) {
        List<String> answer = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (i % 15 == 0) {
                answer.add("FizzBuzz");
            } else if (i % 3 == 0) {
                answer.add("Fizz");
            } else if (i % 5 == 0) {
                answer.add("Buzz");
            } else {
                answer.add(Integer.toString(i));
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(new FizzBuzz().fizzBuzz(15));
    }
}
```

`answer` is a `List<String>`. The object is an `ArrayList`. `add` appends. Index 0 holds the word for 1. The last index holds the word for `n`.

The `% 15` test comes before `% 3`. Fifteen is divisible by 3. The other order would store `"Fizz"` for 15. It would never store `"FizzBuzz"`.

`Integer.toString(i)` is the number as text. `answer` holds only strings. That is what `List<String>` requires.

Run it.

```bash
mvn -q compile exec:java -Dexec.mainClass=com.ledger.collections.list.FizzBuzz
```

`n` of 15 prints a list of 15 strings. Index 0 is `"1"`. The entries for 3, 6, 9, and 12 are `"Fizz"`. The entries for 5 and 10 are `"Buzz"`. The last entry is `"FizzBuzz"`.

### Recap

Fizz Buzz builds an ordered result. The word `"Fizz"` can appear more than once. `answer` has type `List<String>`. The object is an `ArrayList`. The method returns `List<String>`. The code that calls `fizzBuzz` depends on `List`.

## Set: Contains Duplicate

Create `src/main/java/com/ledger/collections/set/ContainsDuplicate.java`.

This is LeetCode 217. Return `true` when any value appears at least twice. A set cannot hold the same value twice. `add` returns whether the value was new.

```java
package com.ledger.collections.set;

import java.util.HashSet;
import java.util.Set;

public class ContainsDuplicate {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int n : nums) {
            if (!seen.add(n)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        ContainsDuplicate solution = new ContainsDuplicate();
        System.out.println(solution.containsDuplicate(new int[] { 1, 2, 3, 1 }));
        System.out.println(solution.containsDuplicate(new int[] { 1, 2, 3, 4 }));
    }
}
```

`seen.add(n)` boxes the `int` into an `Integer`. `HashSet` uses `Integer.equals` and `Integer.hashCode` to tell whether the value is already there. Those methods compare the numeric value. You do not write those methods for `Integer`.

`add` returns `false` when the set already contained `n`. `!seen.add(n)` is then true, and the method returns. When the value is new, `add` returns `true`. The value stays in the set. The loop continues. If the loop finishes, every value was new. The method returns `false`.

In Python you write `if n in seen` and then `seen.add(n)`. `add` returns `None`. Here the `add` call is the test.

Run it. The first line is `true` because `1` is added twice. The second line is `false`.

### Recap

Contains Duplicate is a `Set<Integer>` stored in a `HashSet`. `add` returns `false` when the value is already present. That return value is the answer. The set has no index. This problem does not need one.

## Queue: Number of Recent Calls

Create `src/main/java/com/ledger/collections/queue/RecentCounter.java`.

This is LeetCode 933. `ping(t)` records a call at time `t` and returns how many calls arrived in the window `[t - 3000, t]`. The times you receive are increasing. The oldest call is always at the front. That is a queue. `add` puts the new time at the back. `poll` takes from the front.

```java
package com.ledger.collections.queue;

import java.util.ArrayDeque;
import java.util.Queue;

public class RecentCounter {
    private final Queue<Integer> calls = new ArrayDeque<>();

    public int ping(int t) {
        calls.add(t);
        while (calls.peek() < t - 3000) {
            calls.poll();
        }
        return calls.size();
    }

    public static void main(String[] args) {
        RecentCounter counter = new RecentCounter();
        System.out.println(counter.ping(1));
        System.out.println(counter.ping(100));
        System.out.println(counter.ping(3001));
        System.out.println(counter.ping(3002));
    }
}
```

The field type is `Queue<Integer>`. The object is an `ArrayDeque`. `final` means `ping` cannot point `calls` at a different deque. `add` and `poll` still change what is inside it.

`add(t)` puts the new time at the back. `peek` reads the front and leaves it there. `poll` reads the front and removes it. The `while` drops every time strictly older than `t - 3000`. `size` is whatever survived.

`peek` is safe here because `add(t)` ran first. `t < t - 3000` is false. The time you just added is never polled. The deque is empty only before the first `ping`. `peek` returns an `Integer`. The `<` comparison unboxes it. A `null` from `peek` throws `NullPointerException`. That `null` shows up only on an empty deque. This loop does not reach an empty deque.

The four prints are `1`, `2`, `3`, and `3`. On `ping(3002)` the front is still `1`. `1 < 2`, so `poll` removes it. When the call returns, the front is `100`. `100` is still inside the window.

`add` records the new time. `peek` reads the oldest. `poll` removes it when it falls outside the window. A stream can read the timestamps that are already stored. A stream does not remove from the front while the window is too wide. `poll` does that removal.

### Recap

Number of Recent Calls is a `Queue` of timestamps. `add` records the new call. `peek` reads the oldest. `poll` drops it when it falls outside the 3000 millisecond window. `size` is the answer. `ArrayDeque` is the class.

## Map: Two Sum

Create `src/main/java/com/ledger/collections/map/TwoSum.java`.

This is LeetCode 1. Return the indexes of the two numbers that add up to `target`. There is exactly one answer. You may not use the same index twice. A map from value to index tells you whether you have already seen the complement. You do not scan the front of the array again.

```java
package com.ledger.collections.map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[] { seen.get(complement), i };
            }
            seen.put(nums[i], i);
        }
        return new int[0];
    }

    public static void main(String[] args) {
        int[] indexes = new TwoSum().twoSum(new int[] { 2, 7, 11, 15 }, 9);
        System.out.println(Arrays.toString(indexes));
    }
}
```

`seen` is a `Map<Integer, Integer>`. The key is the number. The value is the index where you saw it. The object is a `HashMap`.

`containsKey` returns whether the complement is already a key. `get` then reads the index. A missing key makes `get` return `null`. The line `int index = seen.get(complement)` unboxes that `null` into an `int`. Unboxing `null` throws `NullPointerException`. `containsKey` runs first, so `get` runs only when an index is present. That includes index `0`.

Python's `complement in seen` is the same question. `seen[complement]` raises `KeyError` when the key is absent.

The code calls `put` after the test. On `[3, 3]` with target `6`, the first `3` is stored at index `0`. The second `3` finds its complement already in the map. If you call `put` first, a value can match itself at the same index.

`return new int[0]` makes every path return an `int[]`. The problem statement guarantees one pair. A correct input does not reach that return.

Run it. `[2, 7, 11, 15]` with target `9` prints `[0, 1]`.

The `containsKey` test has to run before `put`. That order is the solution. Keep the test inside the loop.

### Recap

Two Sum is a `Map` from number to index, stored in a `HashMap`. `containsKey` returns whether the complement was seen. `get` then reads that index. `put` records the current number after the test. A value is not paired with itself.

## Functional interfaces

A lambda is legal where the compiler already knows the target type. That type has one abstract method. Each type below is the argument of one named method. The interface in front of the method is the interface that declares it.

`Predicate<T>` has `boolean test(T)`. `T` is the element type. `test` returns `true` or `false`. `Collection.removeIf` takes a `Predicate`. `List`, `Set`, and `Queue` inherit `removeIf`. `removeIf` drops every element for which `test` returns `true`. `Stream.filter` also takes a `Predicate`. `filter` keeps the element when `test` returns `true`.

`Function<T, R>` has `R apply(T)`. `T` is the input type. `R` is the result type. `List`, `Set`, and `Queue` have no method that takes a `Function`. `Stream.map` takes a `Function`. `map` calls `apply` on each element and passes that result to the next stage of the pipeline. `IntStream.mapToObj` takes an `IntFunction`. `IntFunction` has a primitive `int` input and an object result. The Fizz Buzz stream later in this file calls `mapToObj`.

`Consumer<T>` has `void accept(T)`. `accept` returns nothing. `Iterable.forEach` takes a `Consumer`. `List`, `Set`, and `Queue` inherit `forEach`. `forEach` calls `accept` once per element. `Stream.forEach` takes a `Consumer` as well.

`Comparator<T>` has `int compare(T, T)`. A negative `int` means the first argument comes first. Zero means the two arguments count as equal. A positive `int` means the second argument comes first. `List.sort` takes a `Comparator`. `Set` and `Queue` have no `sort`. `Stream.sorted` takes a `Comparator` as well. Week 1 built one with `Comparator.comparing(TransactionDto::timestamp)`.

`UnaryOperator<T>` is a `Function<T, T>`. The input type and the result type are the same. `List.replaceAll` takes a `UnaryOperator`. `replaceAll` calls it with the element and writes the returned value back into the same index. `Set` and `Queue` have no `replaceAll`. `Map.replaceAll` takes a `BiFunction`, with the key and the stored value as the two arguments.

`BinaryOperator<T>` takes two values of type `T` and returns a `T`. `Stream.reduce` takes a `BinaryOperator`. `reduce` combines the elements into one result. `Map.merge` takes a `BiFunction` as its third argument. A `BinaryOperator` is a `BiFunction` whose two arguments and result are the same type, so `merge` accepts one. When the key is already present, `merge` calls it with the stored value and the new value.

A method reference is a lambda that only calls one method. These four pairs are the same call:

```java
s -> System.out.println(s)    // Consumer<String>
System.out::println

s -> s.toUpperCase()          // UnaryOperator<String>
String::toUpperCase

(a, b) -> Integer.sum(a, b)   // BinaryOperator<Integer>
Integer::sum

s -> "Fizz".equals(s)         // Predicate<String>
"Fizz"::equals
```

`System.out::println` is a reference to a method on one object. `String::toUpperCase` is a reference to an instance method on a class. `Integer::sum` is a reference to a static method. `"Fizz"::equals` is a reference to `equals` on that one string. Week 1 used the same form for `TransactionDto::timestamp` and `this::processTransaction`.

Create `src/main/java/com/ledger/collections/ListOperations.java`. Each block starts from a fresh list. A later call must not see a list an earlier call already changed.

```java
package com.ledger.collections;

import java.util.Comparator;
import java.util.List;

public class ListOperations {
    public static void main(String[] args) {
        List<String> printed = new FizzBuzz().fizzBuzz(5);
        printed.forEach(System.out::println);

        List<String> kept = new FizzBuzz().fizzBuzz(15);
        kept.removeIf(s -> s.equals("Fizz"));
        System.out.println(kept);

        List<String> upper = new FizzBuzz().fizzBuzz(5);
        upper.replaceAll(String::toUpperCase);
        System.out.println(upper);

        List<String> byLength = new FizzBuzz().fizzBuzz(15);
        byLength.sort(Comparator.comparing(String::length));
        System.out.println(byLength);
    }
}
```

Run it.

```bash
mvn -q compile exec:java -Dexec.mainClass=com.ledger.collections.ListOperations
```

`forEach` prints five lines: `1`, `2`, `Fizz`, `4`, `Buzz`. `removeIf` prints `[1, 2, 4, Buzz, 7, 8, Buzz, 11, 13, 14, FizzBuzz]`. `replaceAll` prints `[1, 2, FIZZ, 4, BUZZ]`. `sort` prints `[1, 2, 4, 7, 8, 11, 13, 14, Fizz, Buzz, Fizz, Fizz, Buzz, Fizz, FizzBuzz]`.

`List.forEach` takes a `Consumer<String>`. `System.out::println` prints each word. `List.removeIf` takes a `Predicate<String>` and drops every `"Fizz"`. The other words stay in their original order. `List.replaceAll` takes a `UnaryOperator<String>` and writes the upper-case word back into the same index. `List.sort` takes a `Comparator<String>`. `Comparator.comparing(String::length)` builds that comparator from the length of each word. `List.sort` is stable. Two words of equal length stay in the order they already had.

`removeIf`, `replaceAll`, and `sort` change the list you pass in. `forEach` does not.

### Recap

A lambda needs a target type with one abstract method. The types above are `Predicate`, `Function`, `IntFunction`, `Consumer`, `Comparator`, `UnaryOperator`, and `BinaryOperator`. A method reference is a lambda whose body is a single call. In the program above, `List.forEach` takes the `Consumer`, `List.removeIf` takes the `Predicate`, `List.replaceAll` takes the `UnaryOperator`, and `List.sort` takes the `Comparator`.

## Streams

`answer.stream()` returns a `Stream<String>`. The stream is a pipeline over the list. The stream is not another list. It does not replace the queue in Number of Recent Calls.

`filter` and `map` are intermediate. They return another stream and do no work yet. `toList`, `collect`, `reduce`, and `forEach` are terminal. A terminal operation runs the pipeline and consumes that stream.

```java
Stream<String> once = List.of("Fizz", "Buzz").stream();
once.toList();
once.count();
```

The second terminal call throws `IllegalStateException`. The message says the stream has already been operated upon or closed. Call `.stream()` again if you need a second pass. The list is still there. You cannot use that stream again.

`List.of` builds a small fixed list for this demonstration. The Fizz Buzz method keeps using `new ArrayList<>()`, because that list has to grow inside the loop.

### Fizz Buzz as a stream

The loop shows why the result is a `List`. Order matters. `"Fizz"` may appear more than once. The method below returns that same list from a pipeline. `IntStream.rangeClosed(1, n)` supplies `1` through `n`. Python's `range(1, n + 1)` has the same inclusive end. `IntStream.range(1, n)` stops at `n - 1`.

```java
public List<String> fizzBuzz(int n) {
    return IntStream.rangeClosed(1, n)
            .mapToObj(i -> switch (i % 15) {
                case 0 -> "FizzBuzz";
                case 3, 6, 9, 12 -> "Fizz";
                case 5, 10 -> "Buzz";
                default -> Integer.toString(i);
            })
            .toList();
}
```

`mapToObj` takes an `IntFunction<String>`. The lambda receives the `int` and returns the word. The `switch` is an expression, so the lambda has no block and no `return`. `i % 15` is the remainder inside one block of 15. Remainder `0` means `i` is a multiple of both 3 and 5. Remainders `3`, `6`, `9`, and `12` are the other multiples of 3. Remainders `5` and `10` are the other multiples of 5. `toList()` is the terminal operation. It returns a `List<String>`. The caller still sees a `List`. Add `import java.util.stream.IntStream` to the file.

You can keep both methods on `FizzBuzz`. Name this one `fizzBuzzStream` if you want to print both from `main`. `fizzBuzz(15)` and `fizzBuzzStream(15)` print the same list.

### Recap

`IntStream.rangeClosed` supplies `1` through `n`. `mapToObj` turns each `int` into a `String`. `toList()` collects an ordered `List`, duplicates included. One stream, one terminal operation.

## Principles applied in this session

### The variable's type, and the class

The variable's type is the interface. The interface lists the methods a caller is allowed to use. The `new` expression chooses the class. A method that returns a collection returns the interface. The caller names the interface as well. Switching the class is a change on the `new` expression.

### Map is not a Collection

`List`, `Set`, and `Queue` extend `Collection`. `Map` does not extend `Collection`. A pipeline over a map starts at `values()` or `entrySet()`. Both of those are collections.

### What each interface does

A `List` keeps duplicates and order. You read each element by index.

A `Set` holds each value once. `add` returns `true` when the set changed. It returns `false` when the value was already there.

A `Queue` adds at the back. `peek` reads the front and leaves it. `poll` reads the front and removes it.

A `Map` stores each value under a key. `containsKey` tests presence. `get` reads the value. A missing key makes `get` return `null`. Unboxing that `null` throws `NullPointerException`.

### Keys

`HashSet` and `HashMap` compare with `equals` and `hashCode`. `String` and `Integer` already implement both. `HashSet` and `HashMap` compare a class you write by identity until it implements both. Until then, two objects with the same fields are two keys.

### Functional interfaces

A lambda is legal where the compiler already knows a target type with one abstract method.

`Collection.removeIf` and `Stream.filter` take a `Predicate`. `Stream.map` takes a `Function`. `IntStream.mapToObj` takes an `IntFunction`. `Iterable.forEach` and `Stream.forEach` take a `Consumer`. `List.sort` and `Stream.sorted` take a `Comparator`. `List.replaceAll` takes a `UnaryOperator`. `Stream.reduce` takes a `BinaryOperator`. `Map.merge` takes a `BiFunction` as its third argument. A `BinaryOperator` is that `BiFunction` when both arguments and the result share one type.

A method reference is a lambda whose body is a single call.

### Streams

A stream is one pass over a source. Intermediate operations such as `filter` and `map` return another stream. They do no work yet. A terminal operation such as `toList`, `collect`, `reduce`, or `forEach` runs the pipeline and consumes the stream. A second terminal call on that same stream throws `IllegalStateException`. A second pass starts again from the source.

The terminal operation collects into a `List`, a `Set`, or a `Map`. Keep a loop when the step removes from the front as it goes. Keep a loop when the next write depends on a check against what is already stored. A stream builds a result in one pass.

## Take-home exercises

Write the rest of `src/main/java/com/ledger/exercises/Exercises.java` with streams.

`Client.all()`, `Account.all()`, and `Bank.all()` build the three lists. The fields are private. You read them with the getters. `Main` passes the three lists to the `Exercises` constructor and prints every method.

From `week-2-collections-streams`:

```bash
mvn -q compile
java -cp target/classes com.ledger.exercises.Main
```

`listClientIds` is already written. That is the shape to copy.

```java
return clients.stream().map(Client::getId).toArray(Integer[]::new);
```

`clients` is a `List<Client>`. `stream()` starts the pipeline. `map(Client::getId)` keeps the id of each client. `toArray(Integer[]::new)` ends the pipeline and returns an `Integer[]`. Question 0 prints `[1, 2, 3, 4, 5, 6]`.

The other eight methods return an empty array, an empty map, or `-1`. The comment above each method is what it has to return. `filter`, `map`, `sorted`, and `collect` are the operations you need.

`newClientRanking` adds a client who is not in `clients`, and one account at the bank named `ESTADO` with balance 9000. It returns that client's place in the ranking from `sortClientsTotalBalances`. Copy the lists into a new `ArrayList` before you add. The lists from `all()` come from `List.of`, and `add` on those lists throws.
