/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.apache.log4j.Logger;

/**
 *
 * @author Diane
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Recipe.findAll",
                query="SELECT r FROM Recipe r"),
    @NamedQuery(name="Recipe.findByName",
                query="SELECT r FROM Recipe r WHERE r.name = :name"),
}) 
@Table(name="RECIPE")
public class Recipe implements Persistable {

    final static Logger logger = Logger.getLogger(Recipe.class);
    
    
    @Id
    @GeneratedValue
    @Column(name = "recipe_id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    @Temporal(TemporalType.TIMESTAMP) 
    @Column(name = "submit_date")
    private Date submittalDate;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description", length=4000)
    private String description;
    
    @Column(name = "num_servings")
    private int numberOfServings;
    
    @Column(name = "submitter_name")
    private String submitterName;
    
    @Column(name = "prep_time_minutes")
    private Integer prepTime;
    
    @Column(name = "total_time_minutes")
    private Integer totalTime;
    
    @Column(name = "needs_review")
    private Boolean needsReview;
    
    @Column(name = "origination")
    private String origination; // magazine, family, etc
    
     @OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    private List<RecipeInstruction> instructions = new ArrayList<>();
    
     @OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    private List<RecipeIngredient> ingredients = new ArrayList<>();
    
    public Recipe() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getSubmittalDate() {
        return submittalDate;
    }

    public void setSubmittalDate(Date submittalDate) {
        this.submittalDate = submittalDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(RecipeIngredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public List<RecipeInstruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<RecipeInstruction> instructions) {
        this.instructions = instructions;
    }

    public void addInstruction(RecipeInstruction instruction) {
        this.instructions.add(instruction);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalTime() {
        return this.totalTime;
    }
    public void setTotalTime(Integer totalTime)
    {
        this.totalTime = totalTime;
    }
    public Integer getPrepTime() {
        return this.prepTime;
    }
    public void setPrepTime(Integer prepTime)
    {
        this.prepTime = prepTime;
    }
    
    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public String getOrigination() {
        return origination;
    }

    public void setOrigination(String origination) {
        this.origination = origination;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("\nRecipe: ");
        b.append(createVersionString());
        b.append("\n").append(name).append(" submitted by ");
//        if (submitter != null) {
//            b.append(submitter.getUsername());
//        } else {
            b.append("Anonymous");
//        }
        b.append("\nOrigination: ").append(origination);
        b.append("\nDescription: ").append(description);
        b.append("\nServes: ").append(numberOfServings);
        return b.toString();
    }

    public String prettyPrint()
    {
        StringBuilder b = new StringBuilder();
        b.append("\nRecipe: ");
        b.append(createVersionString());
        b.append("\n").append(name).append(" submitted by ");
//        if (submitter != null) {
//            b.append(submitter.getUsername());
//        } else {
            b.append("Anonymous");
//        }
        b.append("\nOrigination: ").append(origination);
        b.append("\nDescription: ").append(description);
        b.append("\nServes: ").append(numberOfServings);
        for(RecipeIngredient ingredient:ingredients)
        {
            b.append("\n * ").append(ingredient.prettyPrint());
        }
        int i = 1;
        for(RecipeInstruction instruction:instructions)
        {
            b.append("\n").append(i).append(") ").append(instruction);
            i++;
        }
        return b.toString();
    }
    
    public String toShortString() {
        StringBuilder b = new StringBuilder();
        b.append("Name: ").append(name);
        b.append("; ID: ").append(id);
        return b.toString();
    }

    public String createVersionString() {
        StringBuilder b = new StringBuilder();
        b.append("\nID: ").append(id);
        b.append("; Version: ").append(version);
//        b.append("; Submitted: ").append(submittalDate);
        return b.toString();
    }
    
    public String toXml()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public static Recipe fromXml()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.name);
        return hash;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public Boolean getNeedsReview() {
        return needsReview;
    }

    public void setNeedsReview(Boolean needsReview) {
        this.needsReview = needsReview;
    }


}
