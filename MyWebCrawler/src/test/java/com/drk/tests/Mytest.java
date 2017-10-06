package com.drk.tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.drk.main.clases.HtmlDocument;
import com.drk.main.clases.Spider;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author drk
 */
public class Mytest {
    
    public Mytest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void testBoolean () {
        List<String> filters = new ArrayList<>();
        HtmlDocument website=new HtmlDocument("https://news.ycombinator.com/", filters);
        assertTrue(new Spider(website).getConnection());
        website=new HtmlDocument("https://news.ycombinator.com1/", filters);
        assertFalse(new Spider(website).getConnection());                      
    }
    
     @Test
    public void testEquality() {
        List<String> filters = new ArrayList<>();
        filters.add("points");
        filters.add("point");
        filters.add("comments");
        filters.add("comment");
        HtmlDocument website=new HtmlDocument("https://news.ycombinator.com/", filters);
        Spider spider=new Spider(website);
                spider.getConnection();
        //En caso de no haber conexión el método devuelve null
        //assertEquals(null,spider.getHtmlTableData());
        assertNotEquals(null,spider.getHtmlTableData());
        assertNotEquals(null,spider.result(2));
        
    }
}
