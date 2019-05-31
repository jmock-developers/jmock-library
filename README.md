# JMock Library
[![Build Status](https://travis-ci.org/jmock-developers/jmock-library.svg?branch=jmock2)](https://travis-ci.org/jmock-developers/jmock-library)
[![Maven Central](https://img.shields.io/maven-central/v/org.jmock/jmock.svg?label=Maven%20Central)](https://mvnrepository.com/artifact/org.jmock)

# Maven
```xml
  <dependency>
    <groupId>org.jmock</groupId>
    <artifactId>jmock-junit5</artifactId>
    <version>2.12.0</version>
    <scope>test</scope>
  </dependency>
```
# Gradle
```
testCompile(
    "junit:junit5:5.3.1",
    "org.jmock:jmock-junit5:2.12.0"
)
```
# Recent Changes
## 2.10.0
### JUnit 5 Support
* Swap @Rule JUnit4Mockery for @RegisterExtension JMock5Mockery
* Assign to a non-private JMock5Mockery or JUnit5 won't use it

```java

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class JUnit5TestThatDoesSatisfyExpectations {
    @RegisterExtension
    JUnit5Mockery context = new JUnit5Mockery();
    private Runnable runnable = context.mock(Runnable.class);
    
    @Test
    public void doesSatisfyExpectations() {
        context.checking(new Expectations() {{
            oneOf (runnable).run();
        }});
        
        runnable.run();
    }
}
```
### JUnit 4 moved to provided scope in org.jmock:jmock
* This allows dependents to use other versions of junit or other test frameworks (e.g. junit 5)

### Java7 Support will be dropped next release

## 2.9.0
* Dropped JDK 6 compliance.
* Exposed the InvocationDispatcher so that ThreadingPolicies 

## Upgrading to 2.8.X
Are you seeing NPEs?

We have had to make a breaking change to `with()`. Tests using `with(any(matcher))` for method signatures that require native types will throw `NullPointerException`.

You should change

    oneOf(mock).methodWithIntParams(with(any(Integer.class)));

to the following

    oneOf(mock).methodWithIntParams(with.intIs(anything());
This is due to a compiler change in Java 1.7. The 2.6.0 release was compiled with Java 1.6 so it did not suffer this problem.


# Advantages of jMock 2 over jMock 1
* Uses real method calls, not strings, so you can refactor more easily and
  autocomplete in the IDE.
* Customisation by delegation, not by inheritance.
* Many more plugin-points for customisation.
* Independent of any testing framework: compatability with the testing
  framework is a plugin-point.
* Can mock concrete classes *without* calling their constructors (if
  you really want to).
* Uses Hamcrest matchers, so can use a large and ever-growing library
  of matchers in expectations.
* Expectations match in first-in, first-out order, so tests are easier to understand.

# Package Structure

[jMock]() 2 is organised into published and internal packages.  We guarantee backwards compatability of types in published packages within the same major version of jMock.  There are no guarantees about backward compatability for types in internal packages.

Types defined in published packages may themselves define public methods that accept or return types from internal packages or inherit methods from types in internal packages.  Such methods have no compatability guarantees and should not be considered as part of the published interface.


## Published packages

### org.jmock

DSL-style API

### org.jmock.api

### org.jmock.lib

Convenient classes that implement the APIs in the core, are used  by the DSL-style API, and can be used in user-defined APIs 

### org.jmock.integration

Classes integrating jMock with different testing APIs, such  as JUnit 3.x, JUnit 4.x and TestNG. 


## Packages of example code

### org.jmock.lib.nonstd

Lib classes that rely on clever hacks or otherwise cannot be  guaranteed to always work in all JVMs.  There are no compatability guarantees with these classes.  Use at your own risk.


## Internal packages

### org.jmock.internal

Internal implementation details 

### org.jmock.test

Tests for jMock itself


## Plug-in Points

### Matcher

Controls the matching of invocations to expectations

### Action

Performs an action in response to an invocation

### Imposteriser

Wraps mock objects in an adapter of the correct type

### Expectation

Matches an invocation and fakes its behaviour

### ExpectationErrorTranslator

Translates expectation errors into error type used by a specific 
testing framework.

### MockObjectNamingScheme

Creates names for mock objects based on the mocked type.

# Contributing

If you'd like to contribute, then do the following:

1.  clone this repository (`git clone …`)
2.  install Maven (`brew install mvn` on Mac OS, for example)
3.  `$ mvn package` in order to generate a signed JAR. This will run all the tests. (`$ mvn test` appears not to suffice.)

