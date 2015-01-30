package org.jmock.test.unit.internal;

import org.hamcrest.Matcher;
import org.jmock.internal.ReturnDefaultValueAction;
import org.junit.Test;

import javax.xml.ws.handler.LogicalMessageContext;
import java.beans.beancontext.BeanContext;
import java.beans.beancontext.BeanContextServices;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.jmock.test.unit.internal.ReturnDefaultValueActionTests.invocationReturning;

/**
 * @author Steve Freeman 2013 http://www.jmock.org
 *         https://github.com/jmock-developers/jmock-library/issues/9
 */
public class ReturnDefaultCollectionTests {
    abstract static class AbstractCollection implements Collection {}
    private static final Matcher<Object> IS_PROXY_CLASS = hasProperty("canonicalName", containsString("Proxy"));
    private final ReturnDefaultValueAction action = new ReturnDefaultValueAction();

    @SuppressWarnings("unchecked")
    @Test public void
    returnsANewInstanceForEachCall() throws Throwable {
        final ArrayList<Object> firstInstance = returnedArrayList();
        firstInstance.add(new Object());

        assertThat(returnedArrayList(), is(empty()));
    }

    @Test public void
    returnsNewInstanceOfIterableClasses() throws Throwable {
        returnsInstanceForType(ArrayList.class, ArrayList.class);
        returnsInstanceForType(PriorityQueue.class, PriorityQueue.class);
    }

    @Test public void
    returnsNewInstanceOfMapClasses() throws Throwable {
        returnsInstanceForType(HashMap.class, HashMap.class);
        returnsInstanceForType(Properties.class, Properties.class);
    }

    @Test public void
    returnsNewInstanceConformingToCollectionInterface() throws Throwable {
        returnsInstanceForType(List.class, LinkedList.class);
        returnsInstanceForType(Set.class, TreeSet.class);
        returnsInstanceForType(NavigableSet.class, TreeSet.class);
        returnsInstanceForType(SortedSet.class, TreeSet.class);
        returnsInstanceForType(Queue.class, LinkedList.class);
        returnsInstanceForType(Deque.class, LinkedList.class);
    }

    // https://github.com/jmock-developers/jmock-library/issues/46
    @Test public void
    imposterisesBeanContextTypesToAvoidMissingTypesInAndroid() throws Throwable {
        assertThat(action.invoke(invocationReturning(BeanContext.class)).getClass(), IS_PROXY_CLASS);
        assertThat(action.invoke(invocationReturning(BeanContextServices.class)).getClass(), IS_PROXY_CLASS);
    }

    @Test public void
    returnsNewInstanceConformingToMapType() throws Throwable {
        returnsInstanceForType(Map.class, TreeMap.class);
        returnsInstanceForType(SortedMap.class, TreeMap.class);
        returnsInstanceForType(NavigableMap.class, TreeMap.class);
    }

    @Test public void
    imposterisesUnsupportedMapTypes() throws Throwable {
        assertThat(action.invoke(invocationReturning(LogicalMessageContext.class)).getClass(), IS_PROXY_CLASS);
    }
    
    @Test public void
    returnsNullForAbstractCollections() throws Throwable {
        assertThat(action.invoke(invocationReturning(AbstractCollection.class)), is(nullValue()));
    }

    private void returnsInstanceForType(Class<?> declaredType, Class<?> expectedType) throws Throwable {
        assertThat(
                action.invoke(invocationReturning(declaredType)),
                instanceOf(expectedType));
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Object> returnedArrayList() throws Throwable {
        return (ArrayList<Object>) action.invoke(invocationReturning(ArrayList.class));
    }

}
