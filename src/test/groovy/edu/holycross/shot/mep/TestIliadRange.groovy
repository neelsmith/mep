package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestIliadRange extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)
    String expected = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.25"

    void testRange() {
        MepPage pg = new MepPage(twelverecto, graph)
        assert pg.getIliadRange() == expected
    }

}
