/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.common;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author diane
 */
@Entity
@Table(name = "TAG")
public class Tag implements Persistable{
    @Id
    @GeneratedValue
    @Column(name = "TAG_ID")
    private Long id;
    
    @Version
    @Column(name = "VERSION")
    private Integer version;
    
    @Column(name = "TAG_TEXT", unique = true)
    private String tagText;
    
    @OneToMany(cascade = CascadeType.PERSIST, fetch= FetchType.EAGER)
    private List<Tag> tags;
    public Tag()
    {
        
    }
    public Tag(String tag)
    {
        this.tagText = tag;
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
    
    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    
    
}
