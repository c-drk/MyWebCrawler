/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.drk.main.clases;

import java.util.List;

/**
 *
 * @author drk
 */
public class HtmlDocument {
    private String url;
    private List<String> filters;

    public HtmlDocument(String url, List<String> filters ) {
        this.url = url; 
        this.filters = filters; 
    }
    
    public String getUrl(){
        return this.url;
    }
    
    public List<String> getFilters(){
        return this.filters;
    }
}
