package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestCommentary extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    MepGraph graph = new MepGraph(serverUrl)
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")

    void testGraph() {
        def commentary =  graph.getScholiaIliadMap(twelverecto.toString())

        Integer expectedSize = 67
        assert commentary.size() == expectedSize

        String expectedLine = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1"
        assert commentary["urn:cts:greekLit:tlg5026.msA.hmt:1.2"] == expectedLine
    }

    void testPage() {
        MepPage pg = new MepPage(twelverecto, graph)
        def commentary = pg.getCommentary()

        Integer expectedSize = 67
        assert commentary.size() == expectedSize

        String expectedLine = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1"
        assert commentary["urn:cts:greekLit:tlg5026.msA.hmt:1.2"] == expectedLine
    }

}
