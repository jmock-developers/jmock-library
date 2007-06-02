/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.website;

public interface Person
{
    String name();

    int age();

    Person spouse();

    Person[] children();

    void birth();

    void school();

    void work();

    void death();
}
