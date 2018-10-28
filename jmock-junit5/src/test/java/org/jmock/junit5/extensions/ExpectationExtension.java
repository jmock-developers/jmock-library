package org.jmock.junit5.extensions;

import java.lang.annotation.Annotation;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import com.google.auto.service.AutoService;

@AutoService(Extension.class)
public class ExpectationExtension implements TestExecutionExceptionHandler, BeforeEachCallback, AfterEachCallback {

    private Throwable thrown = null;
    private Timer timer = null;

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (isAnnotated(context, ExpectationThrows.class)) {
            if (!readExpectedFromAnnotations(context).isAssignableFrom(throwable.getClass())) {
                throw throwable;
            }
            thrown = throwable;
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (isAnnotated(context, ExpectationTimeout.class)) {
            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Assertions.fail("Timed out");
                }
            }, readTimoutFromAnnotations(context));
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (isAnnotated(context, ExpectationTimeout.class)) {
            timer.cancel();
            timer = null;
        }

        if (isAnnotated(context, ExpectationThrows.class)) {
            Class<? extends Throwable> expected = readExpectedFromAnnotations(context);
            if (thrown == null || !expected.isAssignableFrom(thrown.getClass())) {
                Assertions.fail("Was expecting the throwable:" + expected.getName());
            }
        }
    }

    private boolean isAnnotated(ExtensionContext context, Class<? extends Annotation> annotation) {
        return context.getRequiredTestMethod().isAnnotationPresent(annotation);
    }

    private Class<? extends Throwable> readExpectedFromAnnotations(ExtensionContext context) {
        ExpectationThrows annotation = context.getRequiredTestMethod().getAnnotation(ExpectationThrows.class);
        return annotation.expected();
    }

    private long readTimoutFromAnnotations(ExtensionContext context) {
        ExpectationTimeout annotation = context.getRequiredTestMethod().getAnnotation(ExpectationTimeout.class);
        return annotation.timeout();
    }
}