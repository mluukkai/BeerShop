package com.mycompany.webkauppa.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mycompany.webkauppa.domain.Ostostapahtuma;
import java.util.List;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

public class OstostapahtumaDAOMongo {
    private Datastore store;
    
    public OstostapahtumaDAOMongo() {
        Morphia morphia = new Morphia();
        MongoClientURI uri = new MongoClientURI("mongodb://heroku_d8l45x65:d338utkatj56tmeaf2ni7ml5ua@ds019839.mlab.com:19839/heroku_d8l45x65");
        MongoClient mc = new MongoClient(uri);
        store = morphia.createDatastore(mc, uri.getDatabase());
    }
    
    private Query<Ostostapahtuma> tuotteet() {
        return store.createQuery(Ostostapahtuma.class);
    }
    
    public List<Ostostapahtuma> findAll() {
        return tuotteet().asList();
    }


    public void save(Ostostapahtuma ot) {
        store.save(ot);
    }    
}
