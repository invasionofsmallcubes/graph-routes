package com.invasionofsmallcubes.graph;

import org.junit.Test;
import org.neo4j.graphdb.Node;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DaoTest extends GraphTest {

    @Test
    public void iCanAddANode() {
        Dao dao = new Dao(graphDb);
        dao.addNode("MXP");
        Node mxp = dao.getNode("MXP");
        assertThat(mxp, is(not(nullValue())));
    }

    @Test
    public void iCanAddARelationShip() {
        Dao dao = new Dao(graphDb);
        dao.addNode("MXP");
        dao.addNode("MIL");
        dao.addChildRelationship("MXP", "MIL");
    }

    @Test
    public void iCanRecoverACascade() {
        Dao dao = new Dao(graphDb);

        dao.addNode("MXP");
        dao.addNode("BGY");
        dao.addNode("MIL");
        dao.addNode("ITA");
        dao.addNode("EUR");
        dao.addNode("WORLD");

        dao.addChildRelationship("MXP", "MIL");
        dao.addChildRelationship("BGY", "MIL");
        dao.addChildRelationship("MIL", "ITA");
        dao.addChildRelationship("ITA", "EUR");
        dao.addChildRelationship("EUR", "WORLD");

        List<String> airports = dao.getAncestry("BGY");

        assertThat(airports.get(0),is(equalTo("BGY")));
        assertThat(airports.get(1),is(equalTo("MIL")));
        assertThat(airports.get(2),is(equalTo("ITA")));
        assertThat(airports.get(3),is(equalTo("EUR")));
        assertThat(airports.get(4),is(equalTo("WORLD")));


    }

}