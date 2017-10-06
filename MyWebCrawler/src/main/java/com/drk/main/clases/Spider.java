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
                    this.titles.add(colsNumbers.text()+colsTitle.text()); // se añade el título a una Lista (titles)
                }
                //filtra los td con clase subtext
                Elements colsSubText = row.select("td.subtext");   
                if(!colsSubText.text().isEmpty()) {            
                    //System.out.println(colsSubText.text());
                    tableData+=searchForWord(colsSubText.text());
                    this.subtext.add(searchForWord(colsSubText.text())); // se añade el subtexto a una Lista (subtext)
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
        
        System.out.println("Buscando los parámetros .."+txt);
        List<String> filters=this.htmlobject.getFilters(); //se toman los tags a buscar (puntos y comentarios)
        int firstIndex=txt.indexOf(filters.get(0)); //se busca una coincidencia en el texto secundario que es el que contiene 
        //datos de puntaje y comentarios, en este caso la posicón 0 refiere al tag points
        String points="";
        if ( firstIndex == -1){
            firstIndex=txt.indexOf(filters.get(1)); //si no hubo coincidencia se intenta con el tag "point"
            if(firstIndex == -1){
                points="0 "; //si no hay coincidencia con ningún tag de puntos se asigna 0
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
        //se busca una coincidencia en el texto secundario que es el que contiene 
        //datos de puntaje y comentarios, en este caso la posición 2 refiere al tag comments
        if ( lastIndex == -1){
            lastIndex=txt.indexOf(filters.get(3)); //si no hubo coincidencia se intenta con el tag "comment"
            if(lastIndex == -1){
                comments="0 "; //si no hay coincidencia con ningún tag de comentarios se asigna 0
            }
            else{
                comments=txt.substring(txt.indexOf("e | ")+4,lastIndex-1);
            }
        }
        else{
                comments=txt.substring(txt.indexOf("e | ")+4,lastIndex-1);
            }
        System.out.println(String.format("points %scomments %s", points, comments));
        return String.format("points %scomments %s", points, comments);
    }
    
    public List<String> result(int option){
        List<String>titlesresulSet = new ArrayList<>(); //lista que almacena los títulos de las entradas
        List<String>finalResult = new ArrayList<>();//lista que almacena el resultado final
        List<Integer>points = new ArrayList<>();//lista que almacena los puntos de las entradas
        List<Integer>comments = new ArrayList<>();//lista que almacena los comentarios de las entradas
        int auxPoints;  
        int auxComments;
        String auxTitle;
        StringTokenizer st;
        //Se toma la opción enviada por el formulario donde se organiza el contenido de maneras distintas
        switch(option){
            case 1:  
                for (int i = 0; i < this.titles.size(); i++) { 
                    st = new StringTokenizer(this.titles.get(i)); //se toma el título y el método couintTokens nos devuelve el número de palabras
                    //System.out.println(st.countTokens());
                    if(st.countTokens()>5) // se toman la entradas con título mayores a 5 palabras
                    {
                        titlesresulSet.add(this.titles.get(i));
                        String aux1=this.subtext.get(i).split(" ")[1];               
                        points.add(Integer.parseInt(aux1.substring(0,aux1.length())));  
                        aux1=this.subtext.get(i).split(" ")[3];
                        comments.add(Integer.parseInt(aux1.substring(0,aux1.length())));
                    }
                }
                for (int i = 0; i < comments.size()-1; i++) { 
                    for (int j = i+1; j < comments.size(); j++) { 
                        if(comments.get(i)<comments.get(j)) //se ordenan las listas dependiendo del número de comentarios
                        {
                            auxPoints=points.get(i);
                            points.set(i,points.get(j));
                            points.set(j,auxPoints);
                            auxComments=comments.get(i);
                            comments.set(i,comments.get(j));
                            comments.set(j,auxComments);
                            auxTitle=titlesresulSet.get(i);
                            titlesresulSet.set(i,titlesresulSet.get(j));
                            titlesresulSet.set(j,auxTitle);
                        }
                    }                    
                }               
                break;
            case 2:
                for (int i = 0; i < this.titles.size(); i++) { 
                    st = new StringTokenizer(this.titles.get(i)); //se toma el título y el método couintTokens nos devuelve el número de palabras
                    if(st.countTokens()<=5) // se toman las entradas con título con menos de 6 palabras
                    {
                        titlesresulSet.add(this.titles.get(i));
                        String aux1=this.subtext.get(i).split(" ")[1];
                        points.add(Integer.parseInt(aux1.substring(0,aux1.length()))); 
                        aux1=this.subtext.get(i).split(" ")[3];
                        comments.add(Integer.parseInt(aux1.substring(0,aux1.length())));
                    }
                }
                for (int i = 0; i < points.size()-1; i++) { 
                    for (int j = i+1; j < points.size(); j++) { 
                        if(points.get(i)<points.get(j)) //se ordenan las listas dependiendo del número de puntos
                        {
                            auxPoints=points.get(i);
                            points.set(i,points.get(j));
                            points.set(j,auxPoints);
                            auxComments=comments.get(i);
                            comments.set(i,comments.get(j));
                            comments.set(j,auxComments);
                            auxTitle=titlesresulSet.get(i);
                            titlesresulSet.set(i,titlesresulSet.get(j));
                            titlesresulSet.set(j,auxTitle);
                        }
                    }
                    
                }
                
                break;
        }
        //se arma una lista final concatenando las listas que contiene el título, puntos y comentarios
        for (int i = 0; i < titlesresulSet.size(); i++) { 
            System.out.println(titlesresulSet.get(i)+ " points: "+points.get(i)+" comments:"+comments.get(i));
            finalResult.add( titlesresulSet.get(i)+ " <font color='red'><b>points: </b></font>"+points.get(i)+" <font color='green'><b>comments: </b></font>"+comments.get(i) );
        }
        return finalResult;
    }
    
}
