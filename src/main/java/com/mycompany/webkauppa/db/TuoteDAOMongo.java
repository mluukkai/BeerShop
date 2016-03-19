package com.mycompany.webkauppa.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mycompany.webkauppa.domain.Tuote;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

public class TuoteDAOMongo implements TuoteDAO {
    private Datastore store;


    public TuoteDAOMongo() {
        Morphia morphia = new Morphia();
        MongoClientURI uri = new MongoClientURI("mongodb://heroku_d8l45x65:d338utkatj56tmeaf2ni7ml5ua@ds019839.mlab.com:19839/heroku_d8l45x65");
        MongoClient mc = new MongoClient(uri);
        store = morphia.createDatastore(mc, uri.getDatabase());
    }

    private Query<Tuote> tuotteet() {
        return store.createQuery(Tuote.class);
    }

    @Override
    public List<Tuote> findAll() {
        return tuotteet().asList();
    }

    @Override
    public Tuote find(ObjectId id) {
        return tuotteet().field("id").equal(id).get();
    }

    @Override
    public void save(Tuote tuote) {
        store.save(tuote);
    }

}
