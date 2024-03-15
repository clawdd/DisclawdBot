package org.clawd.parser.exceptions;

/**
 * Custom exception that can be thrown by the Item parser
 */
public class FailedItemsParseException extends Exception{
    public FailedItemsParseException(String errorMsg) {
        super(errorMsg);
    }
}
