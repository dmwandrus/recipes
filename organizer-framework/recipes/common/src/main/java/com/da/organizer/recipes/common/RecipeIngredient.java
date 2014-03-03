/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.exception.UnitNotFoundException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.log4j.Logger;

/**
 *
 * @author Diane
 */
@Entity
@Table(name = "RECIPE_INGREDIENT")
public class RecipeIngredient implements Serializable
{
    @Id
     @GeneratedValue
    @Column(name = "recipe_ingredient_id")
    private Long id;
    
    final static Logger logger = Logger.getLogger(RecipeIngredient.class);

    @Column(name = "amount")
    private double amount;
    
    @Column(name = "unit")
    private IngredientUnit unit;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "prep")
    private String prePreparation;
    
    // placeholder for ingredient information, if it exists
    
    @ManyToOne
    private Ingredient ingredient;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public IngredientUnit getUnit() {
        return unit;
    }

    public void setUnit(IngredientUnit unit) {
        this.unit = unit;
    }

    public String getIngredientName()
    {
        if(ingredient != null)
        {
            return ingredient.getName();
        }
        else{
            return null;
        }
    }
    
    public void setIngredientName(String name)
    {
        if(ingredient == null)
        {
            ingredient = new Ingredient();
        }
        ingredient.setName(name);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPrePreparation()
    {
        return prePreparation;
    }

    public void setPrePreparation(String prePreparation)
    {
        this.prePreparation = prePreparation;
    }

    
    /**
     * This will expect something along the lines of 
     * 1/4 tsp vanilla
     * 1 egg
     * 5 T parsley, chopped
     * 1 egg white
     * 1 egg, divided
     * pinch of salt
     * 1/2 package of ranch seasoning
     * 
     * so, this has to be rather intelligent...
     * 1)  It needs to know how to parse through for units - 
     *     which may be abbreviated in various ways
     * 2)  It need to parse through for amounts, even if it is in words
     * 3)  and it should be able to find existing ingredient names
     * 
     * Look for things that can help with spell-checking...
     *
     * - parse through the string until you find the last number -
     * everything before that is the amount
     *
     * - then get the next word, till the next space - that's the unit (unless
     *   its the last word before a comma, then its the ingredient)
     * - need to add some smarts to my enum - abbreviations to these units and
     *   whatnot.
     *
     * - if there's a comma, then everything after that is the ingredient
     *   prep. else, everything after the unit is the ingredient name.
     * 
     * 
     * @param ingredient
     * @return
     */
    public static RecipeIngredient parseIngredientString(String ingredientString)
    {
        logger.debug("Parsing '"+ingredientString+"'");
        Pattern pattern = Pattern.compile("^[0-9/]*");
        Matcher m = pattern.matcher(ingredientString);
        m.find();
        String amountString = m.group();
        logger.debug("amount: "+amountString);

        String prePrep = null;
        int endOfAmount = m.end() + 1;  // get past the first space
        int indexOfComma = ingredientString.indexOf(",");
        String ingredientAndUnit = ingredientString.substring(endOfAmount);

        if(indexOfComma != -1)
        {
            prePrep = ingredientString.substring(indexOfComma+1);
            prePrep = prePrep.trim();
            ingredientAndUnit = ingredientString.substring(endOfAmount, indexOfComma);
        }

        logger.debug("preprep: '"+prePrep+"'");

        // assuming that from "1 cup fresh parsley, divided"
        // I should have "divided" as preprep, 1 as amount
        // and "cup fresh parsley" as ingredient and unit
        // "1 egg" should be "1" in amount & "egg" here as ingredient & unit

        IngredientUnit unit = IngredientUnit.whole;


        
        String name = ingredientAndUnit;
        name = name.trim();
        logger.debug("Ingredient & unit sub string: '"+ingredientAndUnit+"'");
        int indexOf = ingredientAndUnit.indexOf(" ");
        if(indexOf != -1)
        {
            String unitString = ingredientAndUnit.substring(0, indexOf);
            unitString = unitString.trim();
            logger.debug("Unit String: '"+unitString+"'");
            
            try {
                unit = IngredientUnit.getUnit(unitString);
                name = ingredientAndUnit.substring(indexOf);
                name = name.trim();
            } catch (UnitNotFoundException ex) {
                logger.error("Unable to determine unit, using 'whole'", ex);
            }
            logger.debug("ingredient name string: '"+name+"'");
        }


        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setAmount(convertStringToAmount(amountString));
        ingredient.setIngredientName(name);
        if(prePrep != null)
        {
            ingredient.setPrePreparation(prePrep);
        }
        ingredient.setUnit(unit);
        return ingredient;
    }

    protected static double convertStringToAmount(String amount)
    {
        int indexOf = amount.indexOf("/");
        if(indexOf != -1)
        {
            String[] split = amount.split("/");
            // assuming split is size 2
            if(split.length == 2 )
            {
                Double top = Double.valueOf(split[0]);
                Double bottom = Double.valueOf(split[1]);
                Double actualAmount = top/bottom;
                logger.info("Top: "+top+ " / Bottom: "+bottom+" = "+actualAmount);
                return actualAmount;
            }else
            {
                logger.error("Something is strange.  Original String: "+amount+".  Split: "+split+". Returning default value of 1.");
                return 1.0;
            }
        }else{
            return Double.valueOf(amount);
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.amount).append(" ");
        sb.append(this.unit).append(" ");
        if(ingredient != null)
        {
            sb.append(this.ingredient.getName());
            sb.append(" [");
            sb.append(this.ingredient.getId());
            sb.append("]");
        }
        sb.append(", ");
        sb.append(this.prePreparation);
        return sb.toString();
    }

    
}
