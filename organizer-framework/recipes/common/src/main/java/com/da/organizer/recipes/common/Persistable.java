/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import java.io.Serializable;

/**
 *
 * @author Diane
 */
    public interface Persistable extends Serializable
{
    public Long getId();
    public Integer getVersion();
    public void setId(Long id);
    public void setVersion(Integer version);
}
