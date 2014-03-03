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
import javax.persistence.Table;

/**
 *
 * @author Diane
 */
@Entity
@Table(name = "RECIPE_INSTRUCTION")
public class RecipeInstruction implements Serializable
{

    @Id
     @GeneratedValue
    @Column(name = "recipe_instruction_id")
    private Long id;
    
    @Column(name = "instruction_text")
    private String instructionText;
    

    public RecipeInstruction()
    {

    }

    public RecipeInstruction(String text)
    {
        instructionText = text;
    }

    public String getInstructionText() {
        return instructionText;
    }

    public void setInstructionText(String instructionText) {
        this.instructionText = instructionText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String toString()
    {
        return instructionText;
    }
}
