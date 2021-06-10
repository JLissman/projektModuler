package server;

import db.EntityHandler;
import db.entity.Pokemon;

public class HTMLTemplateBuilder {

    public static String deleteString(Pokemon pkmn){
            String success = "<header><center>Successfully deleted pokemon:"+pkmn.getName()+" with ID "+pkmn.getId()+"</center></header><br><br><br>" +
            "<center><button type=\"button\" onclick=\"goBack()\">Back</a><center>" +
            "<script> function goBack() {window.history.back();}</script>";
        return success;
    }



    public static String addString(Pokemon pkmn){
        String success = "<header><center>Successfully Added pokemon:"+pkmn.getName()+" with ID "+pkmn.getId()+"</center></header><br><br><br>" +
                "<center><button type=\"button\" onclick=\"goBack()\">Back</a><center>" +
                "<script> function goBack() {window.history.back();}</script>";

        return success;
    }


    public static String buildPKMNtemplate(int id){
        EntityHandler entityHandler = new EntityHandler(Pokemon.class);
        Pokemon pkmn = (Pokemon) entityHandler.findById(id);
        Pokemon nextPkmn = null;
        Pokemon prevPkmn = null;
        if(id < 150 && id > 1){
            prevPkmn = (Pokemon) entityHandler.findById(id-1);
            nextPkmn = (Pokemon) entityHandler.findById(id+1);
        }

        String finalString = "<!DOCTYPE html>"+
                "<html lang=\"en\">"+
                "<head>\n"+
                "<meta charset=\"UTF-8\">"+
                "<title>POKEMON BITCHES</title>"+
                "</head>"+
                "<center><body>"+
                "<div id=\"pkmnid\">Pokedex index:"+pkmn.getId()+"</div>"+
                "<div id =\"pkmnname\">Pokemon name:"+pkmn.getName()+"</div>"+
                "<div id =\"pkmnevo\">Pokemon evolution:"+pkmn.getEvolution()+" </div>"+
                "<img src=\"https://img.pokemondb.net/artwork/large/"+pkmn.getName().toLowerCase()+".jpg\" style='height: 150px; width: 150px'></img>"+
                "</body>";
        if(prevPkmn != null){
            finalString = finalString +"<form method=\"POST\" action=\"/pokemon.html?index="+prevPkmn.getId()+"\">" +
                    "<button type=\"submit\" VALUE=\"Refresh\">Previous Pokemon </button></form>";
        }

        finalString = finalString + "<form method=\"POST\" action=\"/index.html\">" +
                "<button type=\"submit\">New Search</button></form>";
        if(nextPkmn != null){
            finalString = finalString +"<form method=\"POST\" action=\"/pokemon.html?index="+nextPkmn.getId()+"\">" +
                    "<button type=\"submit\">Next pokemon</button></form>";
        }
        finalString = finalString +  "</center>"+
                "</html>";
        return finalString;


    }
}
