/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;


/**
 * <p>The ReturnValue class allows a value to be setup which will then be returned upon a specific
 * method call. If </code>value.getValue()</code> is called before <code>value.setValue(value)</code>
 * the ReturnValue will raise an error warning that this value has not been set. If the required
 * return value is <code>null</code> the return value can be set like this
 * <code>value.setValue(null)</code> in this case calling <code>value.getValue()</code>
 * will return null.<p>
 * <p/>
 * <p>The advantage of this is provide better information to the user of a mock when
 * interacting with third party code which may expect certain values to have been set.</p>
 * <p/>
 * e.g.
 * <pre>
 * private final ReturnValue value = new ReturnValue("value");
 * <p/>
 * public void setupValue(Integer value){
 *    value.setValue(value);
 * }
 * <p/>
 * public Integer getValue(){
 *     return (Integer)value.getValue();
 * }
 * </pre>
 * 
 * @version $Revision$
 */
public class ReturnValue {
    private final String name;
    private Object value;

    /**
     * @param name the name used to identify the ReturnValue when an error is raised
     */
    public ReturnValue(String name) {
        this.name = name;
    }

    /**
     * @return the value set using setValue
     * @throws junit.framework.AssertionFailedError
     *          throw if setValue has not been called
     */
    public Object getValue() {
        AssertMo.assertNotNull("The return value \"" + name + "\" has not been set.", value);

        if (value instanceof Null) {
            return null;
        }

        return value;
    }

    /**
     * @param value value to be returned by getValue. null can be use to force getValue to return null.
     */
    public void setValue(Object value) {
        if (value == null) {
            this.value = Null.NULL;
        } else {
            this.value = value;
        }
    }

    /**
     * @param value value to be returned by getBooleanValue. Calling getValue after this method will return
     *              a Boolean wrapper around the value.
     */
    public void setValue(boolean value) {
        setValue(new Boolean(value));
    }

    /**
     * @return the current value converted to a boolean
     */
    public boolean getBooleanValue() {
        return ((Boolean) getValue()).booleanValue();
    }

    /**
     * @return the current value converted to an int
     */
    public int getIntValue() {
        return ((Number) getValue()).intValue();
    }

    /**
     * @param value value to be returned by getIntValue. Calling getValue after this method will return
     *              a Integer wrapper around the value.
     */
    public void setValue(int value) {
        setValue(new Integer(value));
    }

    /**
     * @param value value to be returned by getLongValue. Calling getValue after this method will return
     *              a Long wrapper around the value.
     */
    public void setValue(long value) {
        setValue(new Long(value));
    }

    /**
     * @return the current value converted to an long
     */
    public long getLongValue() {
        return ((Number) getValue()).longValue();
    }


}