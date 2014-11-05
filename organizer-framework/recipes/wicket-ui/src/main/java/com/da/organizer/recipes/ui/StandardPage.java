/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui;

import com.da.organizer.recipes.ui.partials.FooterPanel;
import com.da.organizer.recipes.ui.partials.HeaderPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 *
 * @author dandrus
 */
public class StandardPage extends WebPage{
    
    private HeaderPanel header;
    private FooterPanel footer;
    
        
    public StandardPage()
    {
        
       add(header = new HeaderPanel("headerPanel"));
       add(footer = new FooterPanel("footerPanel"));
    }
    
}
