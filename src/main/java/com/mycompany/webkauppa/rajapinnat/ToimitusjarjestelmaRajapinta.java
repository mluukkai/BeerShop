package com.mycompany.webkauppa.rajapinnat;

import com.mycompany.webkauppa.sovelluslogiikka.Ostos;
import java.util.*;

public class ToimitusjarjestelmaRajapinta {

    private static ToimitusjarjestelmaRajapinta instance;

    public static ToimitusjarjestelmaRajapinta getInstance() {
        if (instance == null) {
            instance = new ToimitusjarjestelmaRajapinta();
        }

        return instance;
    }
    private ArrayList<String> toimitukset;

    public ToimitusjarjestelmaRajapinta() {
        toimitukset = new ArrayList<>();
    }    
    
    public void kirjaatoimitus(String nimi, String osoite, List<Ostos> ostokset){
        toimitukset.add( new Date() + " "+ nimi + " "+ osoite + "\n"+ merkkijonona(ostokset) );
    }
    
    public ArrayList<String> toimitukset() {
        return toimitukset;
    }

    private String merkkijonona(List<Ostos> ostokset) {
        String mj = "";
        for (Ostos ostos : ostokset) {
            mj += " "+ostos.tuotteenNimi() + " x " +ostos.lukumaara() + "\n";
        }
        
        return mj;        
    }
}
