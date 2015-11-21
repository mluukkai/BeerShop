package com.mycompany.webkauppa.rajapinnat;

import java.util.ArrayList;
import java.util.Date;

public class PankkiRajapinta {

    private static PankkiRajapinta instance;

    public static PankkiRajapinta getInstance() {
        if (instance == null) {
            instance = new PankkiRajapinta();
        }

        return instance;
    }

    public PankkiRajapinta() {
        maksut = new ArrayList<>();
    }
    
    private ArrayList<String> maksut;

    public boolean maksa(String nimi, String luottokortti, int hinta) {
        if (luottokortti.length() < 2) {
            return false;
        }

        maksut.add( new Date() + " " + nimi + " " + luottokortti + " " + hinta );

        return true;
    }
    
    public ArrayList<String> maksut() {
        return maksut;
    }
}
