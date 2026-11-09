# my-app

Maven quickstart project (`com.ledger.app:my-app`) built with Java 25 (Eclipse Temurin) managed by jenv.

## Install jenv

```bash
brew install jenv
```

Add jenv to your shell (`~/.zshrc`):

```bash
echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.zshrc
echo 'eval "$(jenv init -)"' >> ~/.zshrc
source ~/.zshrc
```

Enable the plugins Maven needs so `JAVA_HOME` follows the selected JDK:

```bash
jenv enable-plugin export
jenv enable-plugin maven
```

Open a new terminal after enabling plugins.

## Install Java 25 (Temurin)

```bash
brew install --cask temurin@25
```

Register it with jenv and use it for this project (writes `.java-version`):

```bash
jenv add /Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home
cd my-app
jenv local 25
java -version
```

## Install Maven

```bash
brew install maven
mvn -version
```

## Create this project

From the parent directory of `my-app`:

```bash
mvn archetype:generate \
  -DgroupId=com.ledger.app \
  -DartifactId=my-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.5 \
  -DinteractiveMode=false
```

That generates:

```
my-app/
├── pom.xml
└── src/
    ├── main/java/com/ledger/app/App.java
    └── test/java/com/ledger/app/AppTest.java
```

Then pin Java 25 inside the new project:

```bash
cd my-app
jenv local 25
```

## Build and run

```bash
mvn test
mvn package
mvn exec:java -Dexec.mainClass=com.ledger.app.App
```
