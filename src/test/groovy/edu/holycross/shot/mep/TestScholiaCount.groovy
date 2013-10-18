package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestScholiaCount extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    MepGraph graph = new MepGraph(serverUrl)
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    CtsUrn mainScholia = new CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt")

    void testTotal() {
        MepPage pg = new MepPage(twelverecto, graph)
        Integer expectedTotal = 67
        assert pg.getNumScholia() == expectedTotal
    }

    void testDoc() {
        MepPage pg = new MepPage(twelverecto, graph)
        Integer expectedCount = 16
        assert pg.getNumScholia(mainScholia) == expectedCount 
        assert pg.getNumScholia(mainScholia.toString()) == expectedCount
    }

}
