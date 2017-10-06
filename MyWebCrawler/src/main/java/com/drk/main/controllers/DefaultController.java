/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.drk.main.controllers;

import com.drk.main.clases.HtmlDocument;
import com.drk.main.clases.Spider;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author drk
 */
@Controller
public class DefaultController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap map) {       
       return "index";
    }   
    
    @RequestMapping(value = "/submitForm", method = RequestMethod.POST)
    public List<String> updateDisplay(@RequestParam(value="url", required=true) String url,@RequestParam(value="metodos", required=true) int metodos,@RequestParam(value="filters", required=true) String[]filters) {
        //String url=form.getUrl();
        //int metodos=form.getMetodos();
        //List<String> filters=form.getFilters();
        List<String> listfilters=Arrays.asList(filters); 
        HtmlDocument website=new HtmlDocument(url, listfilters);
        Spider spider=new Spider(website);
            spider.getConnection();
        spider.getHtmlTableData();
        //return spider.result(metodos);     
        return listfilters;
    }
    
    @RequestMapping(value = "/test")
    public @ResponseBody String test() {
        
        return "HOLA";     
    }
    
}
