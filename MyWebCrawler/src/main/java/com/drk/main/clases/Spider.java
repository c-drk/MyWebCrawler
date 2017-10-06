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
    
    public String getHtmlTableData()
    {        
        System.out.println("Analizando la estructura del documento ");
        try{
            String originalText = this.htmlDocument.body().text();        
            Element body = this.htmlDocument.select("body").get(0); //seleccionamos el contenido de body
            table = body.select("table.itemList").get(0); //seleccionamos la primera tabla con la clase itemList
            Elements rows = table.select("tr");
            tableData="";
            for (int i = 0; i < rows.size(); i++) { 
                Element row = rows.get(i);
                //filtra los td con clase title y los span con clase rank (que serian los números de entrada)
                Elements colsNumbers = row.select("td.title").select("span.rank"); 
                //filtra los td con clase title y los span con clase storylink (que serian los títulos de la entrada)
                Elements colsTitle= row.select("td.title").select("a.storylink");
                if(!((colsNumbers.text().isEmpty()) || (colsTitle.text().isEmpty())))
                {
                    //System.out.println(colsNumbers.text()+colsTitle.text());
                    tableData+=colsNumbers.text()+colsTitle.text();
                    this.titles.add(colsNumbers.text()+colsTitle.text());
                }
                //filtra los td con clase subtext
                Elements colsSubText = row.select("td.subtext");   
                if(!colsSubText.text().isEmpty()) {            
                    //System.out.println(colsSubText.text());
                    tableData+=searchForWord(colsSubText.text());
                    this.subtext.add(searchForWord(colsSubText.text()));
                }
            }
            //System.out.println(tableData);
            return tableData;
        }
        catch(Exception e){
            return null;
        }
    }
    
    private String searchForWord(String txt)
    {
        
        //System.out.println("Buscando los parámetros .."+txt);
        List<String> filters=this.htmlobject.getFilters();
        int firstIndex=txt.indexOf(filters.get(0));
        String points="";
        if ( firstIndex == -1){
            firstIndex=txt.indexOf(filters.get(1));
            if(firstIndex == -1){
                points="0 ";
            }
            else{
                points=txt.substring(0,firstIndex); 
            }
        }   
        else{
                points=txt.substring(0,firstIndex); 
            }
        String comments="";
        int lastIndex=txt.indexOf(filters.get(2));
        if ( lastIndex == -1){
            lastIndex=txt.indexOf(filters.get(3));
            if(lastIndex == -1){
                comments="0 ";
            }
            else{
                comments=txt.substring(txt.indexOf("e | ")+4,lastIndex-1);
            }
        }
        else{
                comments=txt.substring(txt.indexOf("e | ")+4,lastIndex-1);
            }
        //System.out.println(String.format("points %scomments %s", points, comments));
        return String.format("points %scomments %s", points, comments);
    }
    
    
}
