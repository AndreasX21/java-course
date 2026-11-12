# Java basics

This guide is for people who already program in another language (Python, JavaScript, and so on) but have never touched Java. It explains the pieces you will run into in this repo: the compiler, the JVM, the JDK, and the two main build tools, Maven and Gradle. Where it helps, it compares them to things you probably already know from Python.

## The big idea: compile first, then run

In Python you usually run source code directly: `python app.py`. Java adds a step in between. You first **compile** your code, then you **run** the result.

`javac` is the Java compiler. It turns `.java` source files into `.class` files. A `.class` file contains **bytecode**, a compact set of instructions written for the Java platform rather than for any particular processor (Intel, Apple Silicon, and so on). In this repo, `com.ledger.app.App` compiles to `com/ledger/app/App.class`.

Python does something similar behind the scenes: it compiles your code to bytecode and caches it in `.pyc` files. The difference is that in Java the compile step is explicit, and you can see and ship the result.

Because bytecode is not tied to one kind of machine, the same `.class` files run on macOS, Windows, and Linux. Each machine just needs its own Java runtime to execute them. This is what "write once, run anywhere" means.

### Static typing

Java is **statically typed**: every variable, parameter, and return value has a declared type (`String`, `int`, `App`), and the compiler checks those types before your program ever runs. If you pass a `String` where an `int` is required, you get a compile error instead of a crash at runtime. Think of it as type hints in Python, except they are mandatory and always enforced.

### Classes, objects, and interfaces

All Java code lives inside classes. A **class** is a blueprint, and an **object** is one instance built from that blueprint.

- Fields (the data inside an object) are usually `private`. Other code gets at them through methods.
- A class can **extend** another class, inheriting its code and overriding some of its methods. If you call a method through the parent type, the subclass version is the one that runs.
- An **interface** is a list of methods that a class promises to implement. Unlike extending a class, implementing an interface does not inherit any code. It is a contract, not a code share.

## From source to a running program

```
App.java  --javac-->  App.class  --JVM-->  machine code for this CPU
```

Here is what happens, step by step:

1. You write `src/main/java/com/ledger/app/App.java`.
2. `javac` (usually started for you by Maven) type-checks the file and compiles it to bytecode.
3. The JVM loads `App.class`, verifies it, and runs its `main` method.
4. While the program runs, the JVM notices which methods are called most often and compiles those *again*, in memory, into native machine code for the current CPU. This is called **just-in-time (JIT) compilation**.

The distinction matters: the bytecode in `App.class` is portable, so you can copy it to another computer. The JIT output exists only inside the running process and disappears when it exits.

## The JVM

The **Java Virtual Machine (JVM)** is the program that runs bytecode. Python has the same idea: the `python` executable is what actually runs your code. The JVM we use here is **HotSpot**, which comes bundled with Temurin 25.

The JVM does several jobs:

- **Finds and links classes.** It locates `App.class` and the library classes that file uses (such as `System` and `String`).
- **Verifies bytecode.** It rejects class files that break the platform's type rules, including files that did not come from `javac`.
- **Runs your code.** It interprets bytecode at first, then JIT-compiles the hot methods for speed.
- **Manages memory.** Objects are allocated on a heap, and the **garbage collector** frees anything that is no longer referenced. As in Python, you never free memory yourself.
- **Handles threads.** Java threads run on operating-system threads.

The JVM itself is built for a specific operating system and CPU. Your bytecode is not.

## JVM vs. JRE vs. JDK

These three terms confuse almost everyone at first. They are nested, each one containing the previous one:

| | What it contains | What you use it for |
| --- | --- | --- |
| **JVM** | The engine that runs bytecode | Executing `.class` files |
| **JRE** | A JVM plus the standard library (`java.lang`, `java.util`, `java.io`, …) | Running a program that is already compiled |
| **JDK** | A JRE plus developer tools: `javac`, `jar`, `javadoc`, `jshell`, a debugger | Compiling *and* running |

A few notes:

- `System.out.println` works because the JRE ships the `System` class. Your `.class` file only stores the call, not the code behind it. This is similar to how Python's built-in functions come with the interpreter.
- A plain JRE has no compiler, so you cannot build anything with it.
- This project is built with a JDK, specifically Temurin 25.
- Since Java 11, the normal download is a full JDK. When you ship an app to users, you often bundle a slimmed-down runtime made with `jlink`, so they do not have to install Java themselves.

## Maven

Java projects need a way to compile code, run tests, package the result, and download libraries. **Maven** does all of that. In Python terms, it covers some of what `pip`, `pytest`, and `python -m build` do, combined into one tool.

Maven is configured by a file called `pom.xml` at the root of the project. It records:

- The project's **coordinates**, its unique name: `com.ledger.app:my-app:1.0-SNAPSHOT` (group, artifact, version).
- Which Java version to compile for.
- The **dependencies** (libraries) the project uses.
- Any **plugins** that add extra behavior.

### The lifecycle and common commands

Maven runs a fixed sequence of steps called the **lifecycle**. These commands mean the same thing in nearly every Maven project:

| Command | What it does |
| --- | --- |
| `mvn compile` | Compiles the code in `src/main/java` |
| `mvn test` | Compiles and runs the tests in `src/test/java` |
| `mvn package` | Builds a jar file (a zip of compiled classes) under `target/` |
| `mvn install` | Copies that jar to `~/.m2` so other projects on your machine can use it |

The folders above (`src/main/java`, `src/test/java`) are Maven's defaults, and the quickstart template created them for this repo. Because every Maven project follows the same layout, you can open an unfamiliar one and know where things are.

### Dependencies

A **dependency** is a library, identified by a group, an artifact, and a version. Maven downloads it from **Maven Central** (Java's equivalent of PyPI), and it also downloads whatever libraries *that* library needs. These are called transitive dependencies.

The JUnit **BOM** (bill of materials) in `pom.xml` pins all the JUnit artifacts to one matching set of versions, so you do not have to keep them in sync by hand.

### Plugins

Maven's actual work is done by plugins attached to the lifecycle. `maven-compiler-plugin`, `maven-surefire-plugin` (test runner), and `maven-jar-plugin` (packaging) each handle one phase. If you want the build to do something new, you add a plugin.

A parent POM can also build several modules in the right order. And since the same POM and the same Maven version run the same steps on any machine, CI servers usually just call `mvn test` or `mvn package`.

### The trade-off

Maven's strength is that its lifecycle is fixed and predictable. Its weakness is the same thing: if your build needs its own custom steps, the POM gets long and awkward. That is the situation Gradle is designed for.

## Gradle

**Gradle** is the other major Java build tool. Instead of a fixed XML file, a Gradle build is a **script**: `build.gradle` (written in Groovy) or `build.gradle.kts` (written in Kotlin). Android projects use Gradle. If this repo switched to Gradle, the new build script would replace `pom.xml`.

Gradle's Java plugin already provides the basic tasks `compileJava`, `test`, and `jar`. Anything beyond that is just code in the script: generating a source file, copying files, calling another tool.

A few things Gradle does well:

- **Dependencies.** It resolves libraries (including transitive ones) from Maven Central and other repositories, and it can use jars that were published with Maven.
- **Incremental builds.** It remembers each task's inputs and outputs and skips any task whose inputs have not changed.
- **Build cache.** It can reuse outputs from earlier runs and, if you set it up, from other machines.
- **Parallel builds.** Separate modules can build at the same time.
- **Java toolchains.** A build can request Java 25 specifically, and Gradle finds that JDK on its own, regardless of which `java` is first on your `PATH`.

### The trade-off

Small Gradle builds stay short and readable. But a pile of custom tasks becomes a real program that someone has to maintain. If you want every repo to look the same and be boring to read, Maven is the safer choice.
