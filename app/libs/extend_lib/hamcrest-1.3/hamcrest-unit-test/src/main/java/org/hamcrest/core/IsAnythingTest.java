/*  Copyright (c) 2000-2010 hamcrest.org
 */
package org.hamcrest.core;

import org.hamcrest.AbstractMatcherTest;
import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsAnything.anything;

public class IsAnythingTest extends AbstractMatcherTest {

    @Override
    protected Matcher<?> createMatcher() {
        return anything();
    }

    public void testAlwaysEvaluatesToTrue() {
        assertThat(null, anything());
        assertThat(new Object(), anything());
        assertThat("hi", anything());
    }

    public void testHasUsefulDefaultDescription() {
        assertDescription("ANYTHING", anything());
    }

    public void testCanOverrideDescription() {
        String description = "description";
        assertDescription(description, anything(description));
    }

}
