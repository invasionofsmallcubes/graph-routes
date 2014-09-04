package com.invasionofsmallcubes.graph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class GraphTest {

    protected GraphDatabaseService graphDb;

    @Before
    public void prepareTestDatabase()
    {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
    }

    @Test
    public void iCanCreateAGraph() {

        Node n = null;

        try( Transaction tx = graphDb.beginTx()) {
            n = graphDb.createNode();
            n.setProperty("id","MXP");
            tx.success();
        }

        assertThat( n.getId() , is( greaterThan( -1L ) ) );
    }

    @After
    public void destroyTestDatabase()
    {
        graphDb.shutdown();
    }

}
