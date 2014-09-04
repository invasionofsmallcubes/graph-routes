package com.invasionofsmallcubes.graph;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;

import java.util.LinkedList;
import java.util.List;

public class Dao {

    private final static Label label = DynamicLabel.label("Airport");

    private GraphDatabaseService graphDb;

    public Dao(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    public void addNode(String name) {
        try (Transaction tx = graphDb.beginTx()) {
            graphDb.createNode(label).setProperty("id", name);
            tx.success();
        }
    }

    public Node getNode(String name) {
        try (Transaction tx = graphDb.beginTx()) {
            return graphDb.findNodesByLabelAndProperty(label, "id", name).iterator().next();
        }
    }

    public void addChildRelationship(String child, String parent) {

        Node childNode = null;
        Node parentNode = null;

        try (Transaction tx = graphDb.beginTx()) {
            ResourceIterable<Node> id1 = graphDb.findNodesByLabelAndProperty(label, "id", child);
            if (id1.iterator().hasNext()) {
                childNode = id1.iterator().next();
            }
            ResourceIterable<Node> id2 = graphDb.findNodesByLabelAndProperty(label, "id", parent);
            if (id2.iterator().hasNext()) {
                parentNode = id2.iterator().next();
            }
            if (null != childNode && null != parentNode) {
                childNode.createRelationshipTo(parentNode, Relationships.CHILD);
                tx.success();
            }
        }


    }

    public List<String> getAncestry(String target) {

        Node startPosition = null;

        try (Transaction tx = graphDb.beginTx()) {
            startPosition = graphDb.findNodesByLabelAndProperty(label, "id", target).iterator().next();
        }

        Traverser traverse = graphDb.traversalDescription()
                .depthFirst()
                .relationships(Relationships.CHILD, Direction.OUTGOING)
                .evaluator(Evaluators.all())
                .traverse(startPosition);
        List<String> list = new LinkedList<>();
        String output = "";
        try (Transaction tx = graphDb.beginTx()) {
            for (Path path : traverse) {
                list.add(path.endNode().getProperty("id").toString());
            }
        }
        System.out.println(output);
        return list;
    }
}
