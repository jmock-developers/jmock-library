package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Steve Freeman 2012 http://www.jmock.org
 */
public class InvocationDescriptionAcceptanceTests {
  private static final String UNEXPECTED_ARGUMENT = "unexpected argument";
  private final JUnit4Mockery aMockContext = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
      }};

    private final SubBean aSubBean = aMockContext.mock(SubBean.class);
    private final Collaborator aCollab = aMockContext.mock(Collaborator.class);

    // https://github.com/jmock-developers/jmock-library/issues/20
    @Test
    public void doesNotModifyInvocationsWhileReportingFailure() {
      final Bean lBean = new Bean(aSubBean);

      aMockContext.checking(new Expectations() {{
        allowing(aSubBean).count(); will(returnValue(123));
        oneOf(aCollab).message("xyzzy", lBean);
      }});

      try {
        new Subject(aCollab).doIt(lBean);
      } catch (AssertionError expected) {
        assertThat(expected.getMessage(), containsString("collaborator.message(\"unexpected argument\", <Bean{123}>)"));
        return;
      }
      fail("should have thrown exception");
    }

    static class Bean  {
      private final SubBean aSubBean;

      public Bean(SubBean pSubBean) {
        aSubBean = pSubBean;
      }

      @Override
      public String toString() { // called via describeTo() when reporting error
        return String.format("Bean{%d}", aSubBean.count());
      }
    }

    interface SubBean {
      int count();
    }

    interface Collaborator {
      void message(String pX, Bean pBean);
    }

    static class Subject {
      private final Collaborator aCollab;

      public Subject(Collaborator pCollab) {
        aCollab = pCollab;
      }

      @SuppressWarnings("UnusedDeclaration")
      void doIt(Bean pBean) {
        aCollab.message(UNEXPECTED_ARGUMENT, pBean);
      }
    }


}
