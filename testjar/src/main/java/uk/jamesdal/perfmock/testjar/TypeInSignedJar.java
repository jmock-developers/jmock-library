package uk.jamesdal.perfmock.testjar;

/**
 * Can't use the default package across shared and unshared jars
 */
public interface TypeInSignedJar {
    void doSomething();
}
