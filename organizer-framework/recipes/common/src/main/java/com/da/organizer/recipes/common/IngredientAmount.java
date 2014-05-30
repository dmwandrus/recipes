/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import com.da.organizer.recipes.common.exception.IngredientParseException;
import com.da.organizer.recipes.common.unit.IngredientUnit;
import com.da.organizer.recipes.common.unit.UnitSize;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The amount of an ingredient used in a recipe can be an exacting
 * amount or more of a suggestion, this class intends to hold as much 
 * of that tenuous information as possible.
 * 
 * Examples of ingredient strings, with the amount surrounded by [] 
 * [2 T] crunchy peanut butter, divided
 * [1 large] onion, chopped coarsely
 * [1 28-oz can] tomatoes
 * [2 8-ounce packages] cream cheese, softened
 * 
 * There is the main unit, in these cases, Tablespoon, whole, can and packages.
 * In cooking, sometimes a package may have a size, such as a 28-ounce can of 
 * tomatoes, and you need 2 of those units. In this case the unit is can, the 
 * amount is 2, the package size is 28 and the package unit ounces. 
 * @author dandrus
 */
@Embeddable
public class IngredientAmount {
    
    private PersistableFraction amount;
    
    @Column(name = "unit")
    private IngredientUnit unit;
    
    @Column(name = "unit_size")
    private UnitSize unitSize;
    
    @Column(name = "package_size")
    private Double packageSize;
    
    @Column(name = "package_unit")
    private IngredientUnit packageUnit;
    
    
    public void setUnitSize(UnitSize us)
    {
        this.unitSize = us;
    }
    public UnitSize getUnitSize()
    {
        return unitSize;
    }
    public PersistableFraction getAmount()
    {
        return amount;
    }
    public void setAmount(PersistableFraction fraction)
    {
        this.amount = fraction;
    }
    public void setAmountFromString(String amount) throws IngredientParseException
    {
        this.amount = PersistableFraction.fromString(amount);
    }
    
    public IngredientUnit getUnit() {
        return unit;
    }

    public void setUnit(IngredientUnit unit) {
        this.unit = unit;
    }

    public Double getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(double packageSize) {
        this.packageSize = packageSize;
    }

    public IngredientUnit getPackageUnit() {
        return packageUnit;
    }

    public void setPackageUnit(IngredientUnit packageUnit) {
        this.packageUnit = packageUnit;
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(amount != null)
        {
            sb.append(amount.toString());
            sb.append(" ");
        }
        if(unit != null)
        {
            sb.append(unit);
            sb.append(" ");
        }
        if(unitSize != null)
        {
            sb.append(unitSize);
            sb.append(" ");
        }
        if(packageSize != null && packageUnit != null)
        {
            sb.append(packageSize);
            sb.append("-");
            sb.append(packageUnit);
        }
        return sb.toString();
    }
    
    public String prettyPrint()
    {
        StringBuilder sb = new StringBuilder();
        if(amount != null)
        {
            sb.append("\namount: '");
            sb.append(amount.toString());
            sb.append("' ");
        }
        if(unit != null)
        {
            sb.append("\nunit: '");
            sb.append(unit);
            sb.append("' ");
        }
        if(unitSize != null)
        {
            sb.append("\nunit size: '");
            sb.append(unitSize);
            sb.append("' ");
        }
        if(packageSize != null && packageUnit != null)
        {
            sb.append("\npackage size: '");
            sb.append(packageSize);
            sb.append("' \npackage unit: '");
            sb.append(packageUnit);
            sb.append("' ");
        }
        return sb.toString();

    }

    
}
