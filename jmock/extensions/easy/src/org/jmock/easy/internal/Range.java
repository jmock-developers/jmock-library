/*
 * Copyright (c) 2001-2004 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.jmock.easy.internal;

public class Range {
    private int minimum;
    private int maximum;

    public Range(int count) {
        this(count, count);
    }

    public Range(int minimum, int maximum) {
        if (!(minimum <= maximum)) {
            throw new IllegalArgumentException("minimum must be <= maximum");
        }

        if (!(minimum >= 0)) {
            throw new IllegalArgumentException("minimum must be >= 0");
        }

        if (!(maximum >= 1)) {
            throw new IllegalArgumentException("maximum must be >= 1");
        }
        this.minimum = minimum;
        this.maximum = maximum;
    }

    boolean hasFixedCount() {
        return minimum == maximum;
    }

    int getMaximum() {
        return maximum;
    }

    int getMinimum() {
        return minimum;
    }

    public String toString() {
        if (hasFixedCount()) {
            return "" + minimum;
        } else if (hasOpenCount()) {
            return "at least " + minimum;
        } else {
            return "between " + minimum + " and " + maximum;
        }
    }

    public String expectedAndActual(int count) {
        return "expected: " + this.toString() + ", actual: " + count;
    }

    boolean contains(int count) {
        return minimum <= count && count <= maximum;
    }

    boolean hasOpenCount() {
        return maximum == Integer.MAX_VALUE;
    }
}
