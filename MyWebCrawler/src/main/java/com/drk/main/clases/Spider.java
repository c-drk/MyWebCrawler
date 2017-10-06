/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.drk.main.clases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 *
 * @author drk
 */
public class Spider {
    // Se definen las cabeceras para la petición http
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private Document htmlDocument; //almacena la información enviada por el servidor
    private final HtmlDocument htmlobject;
    private Element table;
    private String tableData;
    private final List<String>titles= new ArrayList<>();
    private final List<String>subtext= new ArrayList<>();
    
    public Spider(HtmlDocument htmlobject) {
        this.htmlobject = htmlobject;
    }
    
    public boolean getConnection()
    {
        String url=this.htmlobject.getUrl();
        System.out.println("Url obtenida " + url);
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            System.out.println("Status del servidor " + connection.response().statusCode());
            if(!(connection.response().statusCode() == 200)) 
            // Se comprueba la respuesta del servidor si el status es 200 ok
            //caso contrario se devuelve false
            {
                System.out.println("Error!! accediendo a la página " + url);
                return false;
            }
            if(!connection.response().contentType().contains("text/html"))
                // Se comprueba que la respuesta sea código html legible
            {
                System.out.println("Error!! La respuesta no coincide con HTML");
                return false;
            }
            return true;
        }
        catch(IOException ioe)
        {
            return false;
        }
    }
    
   
}
