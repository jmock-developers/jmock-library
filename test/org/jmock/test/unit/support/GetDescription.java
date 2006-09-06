package org.jmock.test.unit.support;

import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

public class GetDescription {
	public static String of(SelfDescribing a) {
		StringDescription description = new StringDescription();
		a.describeTo(description);
		return description.toString();
	}
}
