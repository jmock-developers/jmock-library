package org.jmock.lib;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;
import static java.lang.Math.max;

import org.jmock.api.MockObjectNamingScheme;

/**
 * A naming scheme in which the implicit name for a mock object is
 * the last word of the mocked type's name in lower case.
 * E.g. A mock object of type HelloWorld would be named "world".
 * If the type name is an acronym it is completely lowercased.  
 * For example, the type URL would be named "url".  Digits and other
 * non-letter characters are left on the end of the word.  For
 * example, the type NMEA0183 would be named "nmea0183".
 * 
 * @author npryce
 */
public class LastWordNamingScheme implements MockObjectNamingScheme {
    public static MockObjectNamingScheme INSTANCE = new LastWordNamingScheme();
    
    public String defaultNameFor(Class<?> typeToMock) {
        String simpleName = typeToMock.getSimpleName();
        int wordEnd = indexOfLastLetter(simpleName);
        
        int start;
        if (isUpperCase(simpleName.charAt(wordEnd))) {
            start = indexOfLastLowerCaseChar(simpleName, wordEnd)+1;
        } else {
            start = indexOfLastUpperCaseChar(simpleName, wordEnd);
        }
        
        return simpleName.substring(max(0,start)).toLowerCase();
    }
    
    private int indexOfLastLetter(String simpleName) {
        int i = simpleName.length()-1;
        while (i > 0 && !Character.isLetter(simpleName.charAt(i))) {
            i--;
        }
        return i;
    }
    
    private int indexOfLastUpperCaseChar(String simpleName, int wordEnd) {
        int i = wordEnd;
        while (i >= 0 && isLowerCase(simpleName.charAt(i))) {
            i--;
        }
        return i;
    }
    
    private int indexOfLastLowerCaseChar(String simpleName, int wordEnd) {
        int i = wordEnd;
        while (i >= 0 && isUpperCase(simpleName.charAt(i))) {
            i--;
        }
        return i;
    }
    
    private LastWordNamingScheme() {}
}
