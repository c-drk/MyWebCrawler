<%-- 
    Document   : index
    Created on : 05/10/2017, 23:29:01
    Author     : drk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
     <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Ejercicio</title>
        <!-- Compiled and minified CSS -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <!--Import jQuery before materialize.js-->
        <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
        <!-- Compiled and minified JavaScript -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
    </head>
    <body>
        <div class="row">
            <h4>My first web crawler</h4>
        </div>
        <div class="row">
            
            <form id="myForm" name="myForm" class="col s12" method="post" enctype="multipart/form-data">
                
                <div class="row">
                    <div class="input-field col s6 m4">
                        <i class="material-icons prefix">account_circle</i>
                        <input readonly value="https://news.ycombinator.com/" id="url" name="url" type="text" class="validate">
                        <label for="url">Url</label>
                    </div>
                    <div class="input-field col s6 m4">
                        Tags
                        <div class="chip">
                            options
                        </div>
                        <div class="chip">
                            comments
                        </div>
                        <input type="hidden" value="options" name="filters"/>
                        <input type="hidden" value="option" name="filters"/>
                        <input type="hidden" value="comments" name="filters"/>
                        <input type="hidden" value="comment" name="filters"/>
                    </div>  
                </div>
                <div class="row">
                        Filtros:
                        <p>
                            <input name="metodos" type="radio" id="opt1" checked value="1"/>
                            <label for="opt1">Filter all previous entries with more than five words in the title ordered by amount of comments first.</label>
                        </p>
                        <p>
                            <input name="metodos" type="radio" id="opt2" value="2" />
                            <label for="opt2">Filter all previous entries with less than or equal to five words in the title ordered by points.</label>
                        </p>
                </div>
                <br>
                <button id="submitForm" type="button" class="btn btn-primary">Enviar
                </button>    
            </form>
          </div>
    </body>
    <script>
        $(document).ready(function() {       
            $('#submitForm').click(function() {
                alert("Procesando la Url...");     
                $.ajax({
                    type: "POST",
                    url: "MyWebCrawler/submitForm",
                    data: $("#myForm").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
                        alert(data); // show response from the php script.
                    }
                });
            });
        });
    </script>   
</html>
