/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.exception;

/**
 *
 * @author dandrus
 */
public class IngredientParseException extends Exception
{
    public IngredientParseException(String reason)
    {
        super(reason);
    }
    public IngredientParseException(String reason, Exception cause)
    {
        super(reason, cause);
    }
}
