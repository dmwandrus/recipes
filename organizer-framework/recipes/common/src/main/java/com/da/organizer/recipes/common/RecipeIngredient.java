/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.descriptors.Descriptor;
import com.da.organizer.recipes.common.descriptors.Form;
import com.da.organizer.recipes.common.descriptors.Optionality;
import com.da.organizer.recipes.common.descriptors.PrepDescriptor;
import com.da.organizer.recipes.common.descriptors.Preparation;
import com.da.organizer.recipes.common.descriptors.Temperature;
import com.da.organizer.recipes.common.descriptors.UnitSize;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.log4j.Logger;

/**
 *
 * Holds information that describes how an ingredient is used in a recipe.
 * Examples of strings: 2 T crunchy peanut butter, divided 1 large onion,
 * chopped coarsely 1 28-oz can tomatoes 2 8-ounce packages cream cheese,
 * softened
 *
 * Note: Not every ingredient will actually be in the ingredient database. The
 * parser expects that the recipe ingredient, in string form, will always be in
 * the format of: [amount (opt)] [unit size (opt)] [unit (opt)] [name
 * (required][, extra info (opt)] amount - Persistable Fraction unit size - this
 * is meant to convey adjective such as small, large and other rather subjective
 * terms unit - exact measurable amounts, according to measuring standards
 * amount, UnitSize & unit are contained within the IngredientAmount Object name
 * - a string describing the ingredient - anything from 'onion' to 'organic red
 * striped onion' extra info - this will always be all information past the
 * comma in the string.
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

    // placeholder for ingredient information, if it exists
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "originalString")
    private String originalString;

    @Column(name = "parensContents")
    private String parensContents;

    @Transient
    private Set<Descriptor> descriptors = new HashSet<>();

    @ElementCollection(targetClass = Form.class)
    @Enumerated(EnumType.STRING) // Possibly optional (I'm not sure) but defaults to ORDINAL.
    @CollectionTable(name = "recipe_ingredient_form")
    @Column(name = "form") // Column name in recipe_ingredient_form
    private Set<Form> formDescriptors = new HashSet<>();

    @ElementCollection(targetClass = Optionality.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_ingredient_opt")
    @Column(name = "opt") // Column name in recipe_ingredient_form
    private Set<Optionality> optDescriptor = new HashSet<>();

    @ElementCollection(targetClass = PrepDescriptor.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_ingredient_pred_desc")
    @Column(name = "prep_desc")
    private Set<PrepDescriptor> prepDescriptor = new HashSet<>();

    @ElementCollection(targetClass = Preparation.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_ingredient_prep")
    @Column(name = "prep")
    private Set<Preparation> preparations = new HashSet<>();

    @ElementCollection(targetClass = Temperature.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_ingredient_temp")
    @Column(name = "temp")
    private Set<Temperature> tempDescriptor = new HashSet<>();

    @ElementCollection(targetClass = UnitSize.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_ingredient_unit_size")
    @Column(name = "unit_size")
    private Set<UnitSize> unitSizeDescriptor = new HashSet<>();

    @OneToOne
    private IngredientAmount recipeAmount = new IngredientAmount();

    public String getRecipeAmountAsString()
    {
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

    public Ingredient getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient)
    {
        this.ingredient = ingredient;
    }

    public String getOriginalString()
    {
        return this.originalString;
    }

    public void setOriginalString(String originalString)
    {
        this.originalString = originalString;
    }

    public String getParensContents()
    {
        return parensContents;
    }

    public void setParensContents(String parensContents)
    {
        this.parensContents = parensContents;
    }

    public Set<Descriptor> getDescriptors()
    {
        descriptors.clear();
        descriptors.addAll(formDescriptors);
        descriptors.addAll(optDescriptor);
        descriptors.addAll(prepDescriptor);
        descriptors.addAll(preparations);
        descriptors.addAll(tempDescriptor);
        descriptors.addAll(unitSizeDescriptor);
        return descriptors;
    }

    public void setDescriptors(Set<Descriptor> descriptors)
    {
        this.descriptors = descriptors;
    }

    protected static double convertStringToAmount(String amount)
    {
        int indexOf = amount.indexOf("/");
        if (indexOf != -1)
        {
            String[] split = amount.split("/");
            // assuming split is size 2
            if (split.length == 2)
            {
                Double top = Double.valueOf(split[0]);
                Double bottom = Double.valueOf(split[1]);
                Double actualAmount = top / bottom;
                logger.info("Top: " + top + " / Bottom: " + bottom + " = " + actualAmount);
                return actualAmount;
            } else
            {
                logger.error("Something is strange.  Original String: " + amount + ".  Split: " + split + ". Returning default value of 1.");
                return 1.0;
            }
        } else
        {
            return Double.valueOf(amount);
        }

    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Set<Form> getFormDescriptors()
    {
        return formDescriptors;
    }

    public void setFormDescriptors(Set<Form> formDescriptors)
    {
        this.formDescriptors = formDescriptors;
    }

    public Set<Optionality> getOptDescriptor()
    {
        return optDescriptor;
    }

    public void setOptDescriptor(Set<Optionality> optDescriptor)
    {
        this.optDescriptor = optDescriptor;
    }

    public Set<PrepDescriptor> getPrepDescriptor()
    {
        return prepDescriptor;
    }

    public void setPrepDescriptor(Set<PrepDescriptor> prepDescriptor)
    {
        this.prepDescriptor = prepDescriptor;
    }

    public Set<Preparation> getPreparations()
    {
        return preparations;
    }

    public void setPreparations(Set<Preparation> preparations)
    {
        this.preparations = preparations;
    }

    public Set<Temperature> getTempDescriptor()
    {
        return tempDescriptor;
    }

    public void setTempDescriptor(Set<Temperature> tempDescriptor)
    {
        this.tempDescriptor = tempDescriptor;
    }

    public Set<UnitSize> getUnitSizeDescriptor()
    {
        return unitSizeDescriptor;
    }

    public void setUnitSizeDescriptor(Set<UnitSize> unitSizeDescriptor)
    {
        this.unitSizeDescriptor = unitSizeDescriptor;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.recipeAmount);
        sb.append(" ");
        sb.append(this.ingredientName);

        if (this.descriptors != null)
        {
            for (Descriptor d : descriptors)
            {
                sb.append(", ");
                sb.append(d.getFullText());

            }
        }
        return sb.toString();
    }

    public String prettyPrint()
    {
        StringBuilder sb = new StringBuilder();
        if (this.recipeAmount != null)
        {
            sb.append(this.recipeAmount.prettyPrint()).append(" ");
        }
        sb.append(" ");
        sb.append(ingredientName);
        sb.append(" ");

        if (!descriptors.isEmpty())
        {
            sb.append("\n\tDescriptors: ");
            for (Descriptor d : descriptors)
            {
                sb.append(d.getFullText());
                sb.append(", ");
            }
        }

        return sb.toString();
    }

}
