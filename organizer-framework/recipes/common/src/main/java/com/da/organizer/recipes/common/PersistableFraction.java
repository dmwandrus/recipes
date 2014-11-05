/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.exception.IngredientParseException;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;

/**
 *
 * @author dandrus
 */
@Embeddable
public class PersistableFraction implements Serializable {

    @Column
    private Integer whole;
    @Column
    private Integer numerator;
    @Column
    private Integer denominator;

    protected PersistableFraction()
    {
        // unused
    }
    
    public PersistableFraction(Integer whole, Integer numerator, Integer denominator) {
        this.whole = whole;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Integer getNumerator() {
        return numerator;
    }

    public Integer getDenominator() {
        return denominator;
    }

    public Integer getWhole() {
        return whole;
    }

    @Override
    public String toString() {
        boolean hasWhole = false;
        StringBuilder b = new StringBuilder();
        if (whole != null && whole > 0) {
            b.append(whole);
            hasWhole = true;
        }

        if (numerator != null && numerator > 0) {
            if (hasWhole) {
                b.append(" ");
            }
            b.append(numerator);
            b.append("/");
            b.append(denominator);
        }

        return b.toString();
    }

    /**
     * Parse amount from string. Should be able to handle all of the following:
     * 1/2, 1/4, 1/8, 3/4, etc 1, 2, 3 2 1/2 5.2, 3.6, 1.75
     *
     * @param amountString
     * @return
     */
    public static PersistableFraction fromString(String amountString) throws IngredientParseException {
        if (amountString.isEmpty()) {
            throw new IngredientParseException("Amount String is empty. Unable to parse.");
        }
        // get whole number from string, if it exists...which it will
        // if there is a space.
        String[] split = amountString.split(" ");
        if (split.length == 1) {
            // Fraction Format doesn't handle double values....so, need to first
            // check if it is a double value, if it isn't carry on, otherwise 
            // create the double from the amount string and send that directly
            // to the Fraction constructor.
            Fraction fraction;
            if (amountString.contains(".")) {
                // this is a double value.  who uses this in a recipe?  no idea.
                try {
                    double doubleAmount = Double.parseDouble(amountString);
                    fraction = new Fraction(doubleAmount);
                } catch (NumberFormatException nfe) {
                    throw new IngredientParseException("Unable to parse amount from decimal '" + amountString + "'", nfe);
                }

            } else {

                try {
                    FractionFormat format = new FractionFormat();
                    fraction = format.parse(amountString);
                } catch (MathParseException mpe) {
                    throw new IngredientParseException("Unable to parse amount into fraction: '" + amountString + "'", mpe);
                }
            }
            // now, since I'm splitting the whole from the fraction, I need to check here
            // and see that my numerator isn't greater than my denominator
            //This returns the whole number part of the fraction.

            int whole = fraction.intValue();
            Fraction fractionLessWhole = fraction.subtract(whole);
            int numerator = fractionLessWhole.getNumerator();
            int denominator = fractionLessWhole.getDenominator();
            if(numerator == 0)
            {
                // then just clear out the fraction entirely. 
                // its just a whole number
                denominator = 0;
            }
            PersistableFraction pFraction = new PersistableFraction(whole, numerator, denominator);
            return pFraction;
        } else if (split.length == 2) {
            try {
                FractionFormat format = new FractionFormat();
                Fraction fraction = format.parse(split[1]);
                Integer whole = Integer.parseInt(split[0]);
                PersistableFraction pFraction = new PersistableFraction(whole, fraction.getNumerator(), fraction.getDenominator());
                return pFraction;
            } catch (MathParseException mpe) {
                throw new IngredientParseException("Unable to parse fraction amount '" + split[1] + "'", mpe);
            } catch (NumberFormatException nfe) {
                throw new IngredientParseException("Unable to parse integer amount '" + split[0] + "'", nfe);
            }
            
            
        } else {
            // throw exception.  something. handle this better.
            return null;
        }


    }

    private PersistableFraction getFraction(String amountString) {
        FractionFormat format = new FractionFormat();
        Fraction fraction = format.parse(amountString);
        // now, since I'm splitting the whole from the fraction, I need to check here
        // and see that my numerator isn't greater than my denominator
        //This returns the whole number part of the fraction.
        int wholeValue = fraction.intValue();
        Fraction fractionLessWhole = fraction.subtract(wholeValue);
        PersistableFraction pFraction = new PersistableFraction(wholeValue, fractionLessWhole.getNumerator(), fractionLessWhole.getDenominator());
        return pFraction;
    }
}
