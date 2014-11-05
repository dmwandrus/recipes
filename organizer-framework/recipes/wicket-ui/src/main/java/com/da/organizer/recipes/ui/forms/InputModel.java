/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.ui.forms;

import org.apache.wicket.util.io.IClusterable;

/**
 *
 * @author diane
 */
public class InputModel implements IClusterable {
    /** some plain text. */
        public String text = "";

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "text = '" + text + "'";
        }
}
