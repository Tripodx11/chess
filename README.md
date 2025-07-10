# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```


Url To Phase 3 Sequence Diagram:https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpZnTngUSWDABLBoAmCtu+hYp1YVqhFAwHNhMACLAwwAIIgQKAM7rMXBcABGwdShhc9ddGCgBPdbiJoA5jAAMAOgAcmB1AgBXbADEACwAzO4ATACcIDAByA4AFmB6CL7GAEooDkjqlgpIEGiYiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9L5GUAA6aADeAEQDlGjAALYo43XjMOMANCu4mgDu0FyLy2srKLPASAj7KwC+mKK1MJXsnDyUDRNTUDPzF4fjm+o7UD2SxW63Gx1O52B42uHG4vFgD1u4gaUCyOTAlAAFJlsrlKJkAI5pXIAShuNXE9yq8kUKjUmgaDhQYAAqoNMe9PigyTTlKoNOoqSY9HUAGJIbgwNmUXkwPRWGCcuYSPSo4AAaylgxgWyQYASisGXJgwAQaq4CpQAA90VpeXSBfdERS1A1pVBeeSxGonVVbi8YEozShgBaOhr0ABRK1qbAEQpeu5lB4lCwNILOEIjCbzTTAJmLFaRqA+eqG6bKk3B0MKxTq9DQzDoLiYe38zS+6relANNC+BAIROUh5t+nqOogNUY90cwa8nm6B0dh6mOpKLhcLUy3RDn0jxft8eTkMYpS+fWY4DnhLz1sHsdC1frzdn-WepF7h6w55lnHo-EaP2WDfvCnb+mWbxGsqywNAcoJXvqHQQPWaAwSsVyJpQnaphgDThM4zjZpMUFfDAsEgisCEJEhKFoQc1zNvQXg+P4ATQJwTKxKKcCRrIcBKDAAAyEDZEUOGsM61ABi07TdH0RiaAUaDZkq8zrH8AJcNc4FCiBAajOWHzKuphj-LsMJPKBklUMiMAICJEqYsJomEsSYBkh+xj7rSh6MsyM6qdyd4+Q+K4iuKkrurK8qGVydCqiGmrusaXAQBoaAAOSsNa6LBXyoXWbZfYDruXnWQGLIzNe0BIAAXigXDRrG8ZFDAbVdkmKbIGmMAZgAjERubqPmCywcWpYNL4VX6jV9V7Ax3B0KOjrJn6Lo9luHo7pgpqsBpuwmGl6iZawJxgCACSYG1nlCp5DQXSgICavtgIwJiUW6K4L2bgAvD9MDfWSbWgAQVAKMYnlXR14gADww8md2iqaRhQ1wIbgEgYMYtDahQzdq0dRV00JLNDVNSgcZKXjNRYQT4nps4A1jOMQ0jYW4zjdAk3E6T83mAgRgA6ZmkwGdF3U92t3rfdCSPc9wsHe9c6fd9MB-UL2y7EDJoY1jEPrRLNlqHDCPSx0UBpKj6Og+DOMSNd61CstDIwMe4Ovgkl7XrebXO4KYUNM+gbezuDuSwTells5EqZOoQFQ5HYE0xBKyBezFHjFRNENuRyzXGHnVVPTMD4YRYypyRo3oT8WfITn1cwAtLaMUx3h+IE3goOgsTxEknfd85fhYOJUtSWWjSyJGgmRh0kY9L0CnqEpIy1yhmEIlUkevCa17Z2gjeYInhWunZIlD05Z-nq5GjuaVTv3gKflgB7XuIXXaALiFK1VKuEWblR0UFSr3QHQXaO834oUOulLKMAcq5Chr7B+y5j4bWKoOdqdsk5jzdDzKAdUyYxgpi1BBhMERdVKGABmTMcwCjZmNEsXNFS4PwXzFAAtjDAP3nAsAWgMF+ywUbDaHtPQYPxtSJB44TBsOZCgF+nDtaIK-suH+Io5DSOxsI0O7V8ZfksgGQeV9AIICwBgo+5UIL520cnbC3VcIlwItmJuTZFr0DbqxVEm4AjYAlJqQS6IYAAHFlRaBHuYhojQAkz3ng4ZUK9d7vzoDpXRcJ9LgOou-A+R81rdgaMgXIQTcxOXRAUjQ19SR328vlR+MAmTP2vK-dJKFP5VOUcKMUEp-4h0UHKIB8S147WMWkveUDjowO4SQ-hBM7owDQSQnR2Tag4KorzcmlMEyiOsXTWxlDeqM0GrQgs9CJpMOWXguajY2GC04bAm0uReHtUmSgwOXTgBzMdpUpcki8lgBKeoTEzTPmPlUZkSwqAaBVgQIE5UnpHkLNsn43IMMSm8gqG88OySfwNART84Jsd46mL0WQhZqTxgxNzIWRoEwyUoAAJKyELH1cIIQgigi2AkPUKBkrQSWIcEY4wUigHVFyr4PLQTUoAHLKh5ZcGAXRLEF1puQnqpdswrGpeoClVLlR0oZUyllKw2UcuFaNcYvLUICqesa-YPwJVStNTKuVzjm4uLoG4wIXAADskRnAoGcLESMIQ4A8QAGzwCnMYEpMBijbNHospobROjRNicTPe2ZbXzGuOvXShLt6cPWOm4wFkUkbztvdcNJTMRuwxCUspt8xFyAkU-ORfT0AAsPEC9pkpNHdJipw0BgzrmpWgdlW5JiHkSIEUVfs6CrHouJWWSqpyWGrOIRsseNiKFUP2XmQ5RYGFlimku85DF2FDIydw+5iiWn+yecHN8WiC7DnEUoyRVaUAVoLW20KKjA4bhqdCncsLS3-vmCI2dgjOxbzDSed9yo8XGJIWY+d29SXavpQ0RlzKD5rsLvAbZeEHHl1Q-MHVGG9UH0Yi3V1LFAhWDYfZLYPckCJDAHRgcEBGMACkIASihfMWIFr1TRoobG6SzQWRyV6NSuJED0DZmwAgYAdGoBwAgPZKA+a0PaU2ZvHNMADJ5oBop5Tqn1OaZI7ITJhLY22QAFY8bQBW7jEoa0oCJDfDy7zn3XqbfU+ReVAUBz-mkwBZ7+lgMHUdE6Nzcp8InVM6WMzp1oogwl7BUpmFzRXVTHDiqi74d2dQlmByq4c33dzI9DULmnuuReiZ8Xb3dteThp9DaX1Pw-WhhRbXr0dpgMF5FugekgdpbIEhYDqUjOi+MuLL7J0n1mRglrn5kMwAAEKhlrdl9ZS2FVErw5u3ZZcaE7tK5zMsBhNyonc7kRsu22qXOMJNur92euBdvdSz0JCMFgIU0prCIA1PQCrOaWsEZWqvfA8OMJgZqxhnB9tiHkPSEbp6hmLMzNWa7rK8c00oOYB1gbPKpbj3XZA9gGgCArBCdI8h-Wv2k1sA6GrcqTE1K6XrH8Mzhqg3FDdde5Mn9Uome20+w+yHQHpli8UN9+78zHjFoaM5xzcGjFjuR0h0hrxievaSUquxKqxg6-ai3DBlHFpUeYu3AI3glNMZY7bhUiAQywGANgBThB8iFCjaE1bE8p4zznn0UwWaMXwm3kWn8NmT4gF4HgXkBgjCYmd-H3QieUC1s8+HbznyGgp62oodP-yAvtoDmo3g2NTSQveOZlA6wnhpJMDueXUH8-weAtZtLcbRjafXVsw7hue9OswEAA