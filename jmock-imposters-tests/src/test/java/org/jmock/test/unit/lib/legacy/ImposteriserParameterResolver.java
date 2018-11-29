package org.jmock.test.unit.lib.legacy;

import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.lib.JavaReflectionImposteriser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.test.acceptance.AbstractImposteriserParameterResolver;

/**
 * Provide known instances of ClassImposteriser for tests
 * 
 * @author oliverbye
 *
 */
public class ImposteriserParameterResolver extends AbstractImposteriserParameterResolver {

    public ImposteriserParameterResolver() {
        super(ByteBuddyClassImposteriser.INSTANCE,
                ClassImposteriser.INSTANCE,
                JavaReflectionImposteriser.INSTANCE);
    }

}
