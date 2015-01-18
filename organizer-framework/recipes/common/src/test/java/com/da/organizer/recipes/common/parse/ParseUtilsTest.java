/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common.parse;

import com.da.organizer.recipes.common.descriptors.Descriptor;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author diane
 */
public class ParseUtilsTest
{
    
    public ParseUtilsTest()
    {
    }
    private String searchingIn = "8 1-inch-thick halibut fillets, skinned, chilled";
    
    @Test
    public void testFindMatchAtEnd()
    {
        String searchingFor = "chilled";
        boolean expResult = true;
        boolean result = ParseUtils.containsStringIgnoreCase(searchingIn, searchingFor);
        assertEquals(expResult, result);

    }

    @Test
    public void testDontFindMatchWhenHyphenPresent()
    {
        String searchingFor = "thick";
        boolean expResult = false;
        boolean result = ParseUtils.containsStringIgnoreCase(searchingIn, searchingFor);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDontFindExtraWhenHyphenPresent()
    {
        String evoo = "extra-virgin olive oil";
        String searchingFor = "extra";
        boolean expResult = false;
        boolean result = ParseUtils.containsStringIgnoreCase(evoo, searchingFor);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testHyphenstuff()
    {
        String evoo = "this-is-super-duper";
        String searchingFor = "super";
        boolean expResult = false;
        boolean result = ParseUtils.containsStringIgnoreCase(evoo, searchingFor);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testFindMatchWithComma()
    {
        String searchingFor = "skinned";
        boolean expResult = true;
        boolean result = ParseUtils.containsStringIgnoreCase(searchingIn, searchingFor);
        assertEquals(expResult, result);
    }
}
