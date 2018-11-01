package org.jmock.test.unit.lib.legacy;

import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.test.acceptance.AbstractImposteriserParameterResolver;

public class CodeGeneratingImposteriserParameterResolver extends AbstractImposteriserParameterResolver {

    public CodeGeneratingImposteriserParameterResolver() {
        super(ByteBuddyClassImposteriser.INSTANCE,
                ClassImposteriser.INSTANCE);
    }
}
