package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestIliadBlock extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    MepGraph graph = new MepGraph(serverUrl)
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    String expectedBlock = "0.0551,0.2305,0.5115,0.494"

    void testGraph() {
        assert graph.getIliadBlock(twelverecto.toString())  == expectedBlock
    }

    void testPage() {
        MepPage pg = new MepPage(twelverecto, graph)
        assert pg.getIliadRoI() == expectedBlock
    }

}
