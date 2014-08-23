/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import java.io.Serializable;
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
 * Holds information that describes how an ingredient is used 
 * in a recipe. 
 * Examples of strings: 
 * 2 T crunchy peanut butter, divided
 * 1 large onion, chopped coarsely
 * 1 28-oz can tomatoes
 * 2 8-ounce packages cream cheese, softened
 * 
 * Note: Not every ingredient will actually be in the ingredient database.
 * The parser expects that the recipe ingredient, in string form, will always be
 * in the format of:
 * [amount (opt)] [unit size (opt)] [unit (opt)] [name (required][, extra info (opt)]
 * amount - Persistable Fraction
 * unit size - this is meant to convey adjective such as small, large and other 
 * rather subjective terms
 * unit - exact measurable amounts, according to measuring standards
 * amount, UnitSize & unit are contained within  the IngredientAmount Object
 * name - a string describing the ingredient - anything from 'onion' to 'organic red striped onion'
 * extra info - this will always be all information past the comma in the string. 
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
    
    @Column(name = "prep")
    private String prePreparation;
    
    // placeholder for ingredient information, if it exists
//    @ManyToOne
//    private Ingredient ingredient;
    
    @Column(name = "ingredient_name")
    private String ingredientName;
    
    @OneToOne
    private IngredientAmount recipeAmount = new IngredientAmount();
    
    public String getRecipeAmountAsString() {
        return recipeAmount.toString();
    }
    
    public void setRecipeAmount(IngredientAmount ia)
    {
        this.recipeAmount = ia;
    }
    
    public IngredientAmount getRecipeAmount()
    {
        return this.recipeAmount;
    }
    
    public String getIngredientName()
    {
        return ingredientName;
    }
    
    public void setIngredientName(String name)
    {
        this.ingredientName = name;
    }

    public String getPrePreparation()
    {
        return prePreparation;
    }

    public void setPrePreparation(String prePreparation)
    {
        this.prePreparation = prePreparation;
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
        sb.append("[");
        sb.append(this.recipeAmount).append("] [");
        sb.append(", [");
        sb.append(this.prePreparation);
        sb.append("]");
        return sb.toString();
    }
    
    public String prettyPrint()
    {
        StringBuilder sb = new StringBuilder();
        if(this.recipeAmount != null )
        {
            sb.append(this.recipeAmount.debugPrint()).append(" ");
        }
        if(this.prePreparation != null)
        {
            sb.append("\npre prep: '");
            sb.append(this.prePreparation);
            sb.append("' ");
        }
        return sb.toString();
    }
    
}
