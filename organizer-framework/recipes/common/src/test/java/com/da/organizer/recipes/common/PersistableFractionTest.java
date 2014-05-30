/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.exception.IngredientParseException;
import java.util.logging.Logger;
import org.apache.commons.math3.fraction.Fraction;
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
public class PersistableFractionTest {
    private static final Logger LOG = Logger.getLogger(PersistableFractionTest.class.getName());
    
    public PersistableFractionTest() {
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
     * Test of "1/2"
     */
    @Test
    public void test_1_over_2() throws IngredientParseException {
        PersistableFraction pfraction = PersistableFraction.fromString("1/2");
        LOG.info("Fraction: "+pfraction.toString());
        Integer expectedNumerator = 1;
        Integer expectedDenominator = 2;
        Integer expectedWhole = 0;
        assertEquals(pfraction.getWhole(), expectedWhole);
        assertEquals(pfraction.getNumerator(), expectedNumerator);
        assertEquals(pfraction.getDenominator(), expectedDenominator);
        assertEquals(pfraction.toString(), "1/2");
    }
    
    @Test
    public void test_2() throws IngredientParseException {
        PersistableFraction pfraction = PersistableFraction.fromString("2");
        LOG.info("Fraction: "+pfraction.toString());
        Integer expectedNumerator = 0;
        Integer expectedWhole = 2;
        assertEquals(pfraction.getWhole(), expectedWhole);
        assertEquals(pfraction.getNumerator(), expectedNumerator);
        assertTrue(pfraction.getDenominator() > 0); // since a den. can't be zero or less. 
        assertEquals(pfraction.toString(), "2");
    }
    
    @Test
    public void test_2_and_1_over_2() throws IngredientParseException {
        PersistableFraction pfraction = PersistableFraction.fromString("2 1/2");
        LOG.info("Fraction: "+pfraction.toString());
        Integer expectedNumerator = 1;
        Integer expectedDenominator = 2;
        Integer expectedWhole = 2;
        assertEquals(pfraction.getWhole(), expectedWhole);
        assertEquals(pfraction.getNumerator(), expectedNumerator);
        assertEquals(pfraction.getDenominator(), expectedDenominator);
        assertEquals(pfraction.toString(), "2 1/2");
    }

    @Test
    public void testing()
    {
        String stringWithPeriod = "0.5";
        LOG.info("Contains period? "+stringWithPeriod.contains("."));
        LOG.info("Contains period? "+stringWithPeriod.contains("\\."));
        LOG.info("Contains period? "+stringWithPeriod.contains("[.]"));
    }
    
    @Test
    public void test_0_dot_5() throws IngredientParseException {
        PersistableFraction pfraction = PersistableFraction.fromString("0.5");
        LOG.info("Fraction: "+pfraction.toString());
        Integer expectedNumerator = 1;
        Integer expectedDenominator = 2;
        Integer expectedWhole = 0;
        assertEquals(pfraction.getWhole(), expectedWhole);
        assertEquals(pfraction.getNumerator(), expectedNumerator);
        assertEquals(pfraction.getDenominator(), expectedDenominator);
        assertEquals(pfraction.toString(), "1/2");
    }
    
    @Test
    public void test_2_dot_5() throws IngredientParseException {
        PersistableFraction pfraction = PersistableFraction.fromString("2.5");
        LOG.info("Fraction: "+pfraction.toString());
        Integer expectedNumerator = 1;
        Integer expectedDenominator = 2;
        Integer expectedWhole = 2;
        assertEquals(pfraction.getWhole(), expectedWhole);
        assertEquals(pfraction.getNumerator(), expectedNumerator);
        assertEquals(pfraction.getDenominator(), expectedDenominator);
        assertEquals(pfraction.toString(), "2 1/2");
    }
}