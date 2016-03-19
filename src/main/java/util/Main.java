package util;


import com.mycompany.webkauppa.db.TuoteDAOMongo;
import com.mycompany.webkauppa.domain.Tuote;


public class Main {
    public static void main(String[] args) {
        TuoteDAOMongo dao = new TuoteDAOMongo();
        Tuote t = new Tuote("Karhu", 2);
        t.setSaldo(25);
        dao.save(t);
        for (Tuote tuote : dao.findAll()) {
            System.out.println(tuote);
        }
    }
}
