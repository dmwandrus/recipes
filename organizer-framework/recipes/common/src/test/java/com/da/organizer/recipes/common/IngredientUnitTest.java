/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.unit.IngredientUnit;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dandrus
 */
public class IngredientUnitTest {
    
    public IngredientUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

      /**
     * Test of getUnit method, of class IngredientUnit.
     */
    @Test
    public void testGetUnit_fromAltText() throws Exception {
        System.out.println("getUnit");
        String text = "ounces";
        IngredientUnit expResult = IngredientUnit.ounce;
        IngredientUnit result = IngredientUnit.getUnit(text);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetUnit_withPeriod() throws Exception {
        System.out.println("getUnit");
        String text = "c.";
        IngredientUnit expResult = IngredientUnit.cup;
        IngredientUnit result = IngredientUnit.getUnit(text);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetUnit_standard() throws Exception {
        System.out.println("getUnit");
        String text = "ounce";
        IngredientUnit expResult = IngredientUnit.ounce;
        IngredientUnit result = IngredientUnit.getUnit(text);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetUnit_teaspoon() throws Exception {
        System.out.println("getUnit");
        String text = "t";
        IngredientUnit expResult = IngredientUnit.teaspoon;
        IngredientUnit result = IngredientUnit.getUnit(text);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetUnit_tablespoon() throws Exception {
        System.out.println("getUnit");
        String text = "T";
        IngredientUnit expResult = IngredientUnit.tablespoon;
        IngredientUnit result = IngredientUnit.getUnit(text);
        assertEquals(expResult, result);
    }
}