package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GenericInvocationAcceptanceTests
{
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    private static interface Function<F, T> {
        T apply(F input);
    }

    private static interface StringFunction extends Function<String, String> {
        @Override public String apply(String input);
    }

    private static abstract class AbstractStringFunction implements Function<String, String> {
        @Override public abstract String apply(String input);
    }

    private static String apply(Function<String, String> f, String arg) {
        return f.apply(arg);
    }

    @SuppressWarnings("unchecked")
    private static Object objectApply(Function f, Object arg) {
        return f.apply(arg);
    }

    @Test
    public void plain_old_invocation()
    {
        @SuppressWarnings("unchecked")
        final Function<String, String> f = context.mock(Function.class);

        // NB - don't try to remove this duplication - the resulting erasure invalidates the tests
        context.checking(new Expectations() {{
            allowing(f).apply("alice");
                will (returnValue("bob"));
        }});
        assertEquals("bob", f.apply("alice"));
        assertEquals("bob", apply(f, "alice"));
        assertEquals("bob", objectApply(f, "alice"));
    }

    @Test
    public void invocation_of_overriden_class_method()
    {
        context.setImposteriser(ClassImposteriser.INSTANCE);
        final AbstractStringFunction f = context.mock(AbstractStringFunction.class);

        // NB - don't try to remove this duplication - the resulting erasure invalidates the tests
        context.checking(new Expectations() {{
            allowing(f).apply("alice");
                will (returnValue("bob"));
        }});
        assertEquals("bob", f.apply("alice"));
        assertEquals("bob", apply(f, "alice"));
        assertEquals("bob", objectApply(f, "alice"));
    }

    @Test
    public void invocation_of_overriden_interface_method_with_capture_of_generic_interface()
    {
        final Function<String, String> f = context.mock(StringFunction.class);

        // NB - don't try to remove this duplication - the resulting erasure invalidates the tests
        context.checking(new Expectations() {{
            allowing(f).apply("alice");
                will (returnValue("bob"));
        }});
        assertEquals("bob", f.apply("alice"));
        assertEquals("bob", apply(f, "alice"));
        assertEquals("bob", objectApply(f, "alice"));
    }

    @Test
    public void invocation_of_overriden_interface_method_with_capture_of_concrete_interface()
    {
        final StringFunction f = context.mock(StringFunction.class);

        // NB - don't try to remove this duplication - the resulting erasure invalidates the tests
        context.checking(new Expectations() {{
            allowing(f).apply("alice");
                will (returnValue("bob"));
        }});
        assertEquals("bob", f.apply("alice"));
        assertEquals("bob", apply(f, "alice"));
        assertEquals("bob", objectApply(f, "alice"));
    }

    @Test
    @Ignore("Fails, and cure might be worse than the disease?")
    public void invocation_of_overriden_interface_method_with_capture_via_function()
    {
        final StringFunction f = context.mock(StringFunction.class);

        expectApply(f, "alice", "bob");

        assertEquals("bob", f.apply("alice"));
        assertEquals("bob", apply(f, "alice"));
        assertEquals("bob", objectApply(f, "alice"));
    }

    private void expectApply(final Function<String, String> f, final String arg, final String result) {
        context.checking(new Expectations() {{
            allowing(f).apply(arg);
                will (returnValue(result));
        }});
    }

}