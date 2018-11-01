package org.jmock.test.acceptance;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jmock.api.Imposteriser;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

/**
 * Provide known instances of Code Generating ClassImposteriser for tests
 * Because reflection can't be used for class imposterisation
 * 
 * This is a Java 7 implementation, Java 8 is a lot simpler!
 * 
 * @author oliverbye
 *
 */
public abstract class AbstractImposteriserParameterResolver implements ArgumentsProvider {

    private final Imposteriser[] imposters;

    public AbstractImposteriserParameterResolver(Imposteriser... imposters) {
        this.imposters = imposters;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Arrays.stream(imposters).map(new FunctionImplementation());
    }

    // Java1.7 needs
    private static final class FunctionImplementation implements Function<Imposteriser, Arguments> {

        @Override
        public Arguments apply(Imposteriser i) {
            return new ArgumentSupplier(i);
        }
    }

    private static final class ArgumentSupplier implements Arguments {
        private Imposteriser i;

        public ArgumentSupplier(Imposteriser i) {
            this.i = i;
        }

        @Override
        public Object[] get() {
            return new Object[] { i };
        }
    }

}
