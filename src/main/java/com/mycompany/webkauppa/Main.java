package com.mycompany.webkauppa;

import com.mycompany.webkauppa.rajapinnat.PankkiRajapinta;
import com.mycompany.webkauppa.rajapinnat.ToimitusjarjestelmaRajapinta;
import com.mycompany.webkauppa.sovelluslogiikka.Ostoskori;
import com.mycompany.webkauppa.sovelluslogiikka.Tuote;
import com.mycompany.webkauppa.sovelluslogiikka.Varasto;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

// mvn exec:java -Dexec.mainClass=com.mycompany.webkauppa.Main

public class Main {
    public static void main(String[] args) {       
        get("/home", (req, res) ->  {           
            return new ModelAndView(new HashMap<>(), "index");
            
        }, new ThymeleafTemplateEngine());

        get("/yhteystiedot", (req, res) ->  { 
            return new ModelAndView(new HashMap<>(), "yhteystiedot");            
        
        }, new ThymeleafTemplateEngine());        

        get("/tyhjenna", (req, res) ->  { 
            req.session().removeAttribute("ostoskori");             
            res.redirect("/tuotteet");
            return "";        
        });     

        get("/kassa", (req, res) ->  { 
            Map<String, Object> params = new HashMap<>();
            
            Ostoskori kori = (Ostoskori)req.session(true).attribute("ostoskori");
            if (kori==null) {
                kori = new Ostoskori();
            }
            params.put("kori", kori);            
            return new ModelAndView(params, "kassa");    
        
        }, new ThymeleafTemplateEngine());       
        
        post("/poista", (req, res) ->  { 
            int tuoteId = Integer.parseInt(req.queryParams("id"));
            Ostoskori kori = (Ostoskori)req.session(true).attribute("ostoskori");
            Tuote poistettavaTuote = Varasto.getInstance().etsiTuote(tuoteId);
            kori.poista(poistettavaTuote);
            
            res.redirect("/kassa");
            return ""; 
        });         
        
        post("/kassa", (req, res) ->  { 
            Ostoskori kori = (Ostoskori)req.session(true).attribute("ostoskori");
            
            String nimi = req.queryParams("nimi");
            String osoite = req.queryParams("osoite");
            String kortti = req.queryParams("luottokorttinumero");
                        
            if ( PankkiRajapinta.getInstance().maksa(nimi, kortti, kori.hinta()) ) {
                ToimitusjarjestelmaRajapinta.getInstance().kirjaatoimitus(nimi, osoite, kori.ostokset());
                req.session().removeAttribute("ostoskori");
                req.session(true).attribute("osoite", osoite);
                req.session(true).attribute("hinta", ""+kori.hinta());
                res.redirect("/onnistunut_maksu");                
            } else {
                res.redirect("/epaonnistunut_maksu");
            }
            
            return ""; 
        }); 
        
        get("/onnistunut_maksu", (req, res) ->  { 
            Map<String, Object> params = new HashMap<>();
            
            String osoite = (String)req.session().attribute("osoite");
            req.session().removeAttribute("osoite");
            
            String hinta = (String)req.session().attribute("hinta");
            req.session().removeAttribute("hinta");

            params.put("osoite", osoite);    
            params.put("hinta", hinta);   
            return new ModelAndView(params, "ostokset");    
        
        }, new ThymeleafTemplateEngine()); 
        
        get("/epaonnistunut_maksu", (req, res) ->  {           
            return new ModelAndView(new HashMap<>(), "virhe");    
        
        }, new ThymeleafTemplateEngine()); 
        
        get("/tuotteet", (req, res) ->  {
    
            if ( req.session(true).attribute("ostoskori")==null ) {
                req.session(true).attribute("ostoskori", new Ostoskori());
            }
            
            Map<String, Object> params = new HashMap<>();
            params.put("tuotteet", Varasto.getInstance().tuotteidenLista());
            
            Ostoskori kori = (Ostoskori)req.session(true).attribute("ostoskori");
            params.put("kori", kori);
                        
            return new ModelAndView(params, "tuotteet");    
        
        }, new ThymeleafTemplateEngine());      

        post("/tuotteet", (req, res) ->  { 
            int tuoteId = Integer.parseInt(req.queryParams("id"));

            Ostoskori kori = (Ostoskori)req.session(true).attribute("ostoskori");    
            Varasto varasto = Varasto.getInstance();
            Tuote tuote = varasto.otaVarastosta(tuoteId);
        
            if (tuote!=null) {
                kori.lisaaTuote(tuote);
            }
            
            res.redirect("/tuotteet");
            return "";
        });         
        
        get("/hallinta", (req, res) ->  {
            String data = "";
            data += PankkiRajapinta.getInstance().maksut() + "\n";
            data += ToimitusjarjestelmaRajapinta.getInstance().toimitukset();
            return data;    
        });        
        
        get( "*", (req, res) ->  { 
            
            return new ModelAndView(new HashMap<>(), "index");
            
        }, new ThymeleafTemplateEngine());    
    }
    
    
}