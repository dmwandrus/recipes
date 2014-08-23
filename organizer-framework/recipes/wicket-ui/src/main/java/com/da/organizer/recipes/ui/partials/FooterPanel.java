/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.partials;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author dandrus
 */
public class FooterPanel extends Panel 
{
    public FooterPanel(String id)
    {
        super(id);
        add(new Label("author", "Diane Andrus (c) 2014"));
        
    }
    
    
}
