/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

/**
 *
 * @author dandrus
 */
public class WicketApplication extends WebApplication
{
    public WicketApplication() {
        
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

}
