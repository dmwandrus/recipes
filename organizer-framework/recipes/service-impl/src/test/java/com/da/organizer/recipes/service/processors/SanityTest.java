/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.service.processors;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author diane
 */
public class SanityTest
{

    String s1 = "Recipe: \n"
            + "Origination: Bon Appetit - July 2014\n"
            + "Description: We think fat spears of asparagus have the best texture. White ones take a little longer to cook, so boil them separately.\n"
            + "Serves: 0\n"
            + "  4 ounce   bacon \n"
            + "  2 bunch   asparagus \n"
            + "     Kosher salt \n"
            + "  1/2   shallot \n"
            + "  3 tablespoon   Sherry vinegar \n"
            + "  1 tablespoon   pure maple syrup \n"
            + "  1 teaspoon   whole grain mustard \n"
            + "  2 tablespoon   olive oil \n"
            + "  1 tablespoon   vegetable oil \n"
            + "     ground black pepper \n"
            + "  4   hard-boiled eggs \n"
            + "  2 tablespoon   tender herbs \n"
            + "1) Cook bacon in a large skillet over medium heat, turning once, until browned and crisp, 5-8 minutes. Transfer to paper towels to drain; let cool, then crumble.\n"
            + "2) Meanwhile, cook asparagus in a large pot of boiling salted water until crisp-tender, 3-5 minutes, depending on thickness. (If using both green and white asparagus, cook white asparagus first to keep them from turning green.) Drain and transfer to a large bowl of ice water to cool. Drain and pat dry.\n"
            + "3) Whisk shallot, vinegar, maple  syrup, and mustard in a medium bowl. Gradually whisk in olive oil until emulsified, then whisk in vegetable oil; season with salt and pepper. \n"
            + "4) Serve asparagus drizzled with vinaigrette and topped with eggs, herbs, and bacon.\n"
            + "5) DO AHEAD: Vinaigrette can be made 2 days ahead; cover and chill. Asparagus can be cooked 1 day ahead. Cover and chill.";

    String s2 = "\n"
            + "Recipe: \n"
            + "Origination: Bon Appetit - July 2014\n"
            + "Description: We think fat spears of asparagus have the best texture. White ones take a little longer to cook, so boil them separately\n"
            + "Serves: 8\n"
            + "  4 ounce   bacon \n"
            + "  2 bunch   green and/or white asparagus \n"
            + "     Kosher salt \n"
            + "  1/2   shallot \n"
            + "  3 tablespoon   Sherry vinegar \n"
            + "  1 tablespoon   pure maple syrup \n"
            + "  1 teaspoon   whole grain mustard \n"
            + "  2 tablespoon   olive oil \n"
            + "  1 tablespoon   vegetable oil \n"
            + "     ground black pepper \n"
            + "  4   hard-boiled eggs \n"
            + "  2 tablespoon   tender herbs \n"
            + "1) Cook bacon in a large skillet over medium heat, turning once, until browned and crisp, 5-8 minutes. Transfer to paper towels to drain; let cool, then crumble.\n"
            + "2) Meanwhile, cook asparagus in a large pot of boiling salted water until crisp-tender, 3-5 minutes, depending on thickness. (If using both green and white asparagus, cook white asparagus first to keep them from turning green.) Drain and transfer to a large bowl of ice water to cool. Drain and pat dry. \n"
            + "3) Whisk shallot, vinegar, maple syrup, and mustard in a medium bowl. Gradually whisk in olive oil until emulsified, then whisk in vegetable oil; season with salt and pepper.\n"
            + "4) Serve asparagus drizzled with vinaigrette and topped with eggs, herbs, and bacon.\n"
            + "5) Do Ahead: Vinaigrette can be made 2 days ahead; cover and chill. Asparagus can be cooked 1 day ahead. Cover and chill.";

    @Test
    public void iaminsane()
    {
//        assertEquals(s1, s2);
    }
}
