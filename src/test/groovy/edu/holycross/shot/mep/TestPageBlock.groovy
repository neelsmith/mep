package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestPageBlock extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    MepGraph graph = new MepGraph(serverUrl)
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    String expectedRoI = "0.0417,0.0813,0.845,0.8813"
    void testGraph() {
        assert  graph.getPageBlock(twelverecto.toString()) == expectedRoI
    }


    void testPage() {
        MepPage pg = new MepPage(twelverecto, graph)
        assert pg.getPageRoI() == expectedRoI
    }

}
