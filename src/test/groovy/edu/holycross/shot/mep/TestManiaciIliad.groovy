package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestManiaciIliad extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)
    MepPage pg = new MepPage(twelverecto, graph)

    Integer expectedSize = 25

    void testRanks() {
        MepLayout layout = new MepLayout(pg)
        def rankings = layout.computeManiaciRankForIliad()
        assert rankings.size() == expectedSize
        assert rankings["urn:cts:greekLit:tlg0012.tlg001.msA:1.1"] == MepPage.PageZone.TOP
        
    }

}
