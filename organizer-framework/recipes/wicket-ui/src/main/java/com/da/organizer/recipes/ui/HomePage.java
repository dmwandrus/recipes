/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 *
 * @author dandrus
 */
/**
 * Very simple page providing entry points into various other examples.
 */
public class HomePage extends WebPage {

    private static final long serialVersionUID = 1L;

    public HomePage() {
        add(new Label("oneComponent", "Welcome to the most simple pax-wicket application based on blueprint."));
    }
}