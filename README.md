# Social Network

A simple Java implementation of a social network system with account management.

## Project Structure

```
src/
  ├── Account.java                    # User account class
  ├── SocialNetwork.java              # Main social network implementation
  ├── ISocialNetwork.java             # Interface for social network
  └── NoUserLoggedInException.java    # Custom exception

test/
  ├── AccountTest.java                # Account tests
  └── SocialNetworkTest.java          # Social network tests

lib/                                   # External libraries
extras/
  └── LabReport.txt                   # Lab report documentation
```

## Building

```bash
javac -d bin src/*.java
```

## Running Tests

```bash
javac -cp bin -d bin test/*.java
java -cp bin AccountTest
java -cp bin SocialNetworkTest
```

## Features

- User account management
- Social network operations
- Exception handling for invalid states
