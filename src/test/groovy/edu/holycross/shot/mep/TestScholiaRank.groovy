package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestScholiaRank extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)

    Integer expectedMainCount = 16

    void testManiaciRank() {
        MepPage pg = new MepPage(twelverecto, graph)
        MepLayout ml = new MepLayout(pg)

        def mainScholiaRanks = ml.rankScholia(pg.getScholiaForDocument("urn:cts:greekLit:tlg5026.msA.hmt"), pg.maniaciZones)

        assert mainScholiaRanks.size() == expectedMainCount
        assert mainScholiaRanks["urn:cts:greekLit:tlg5026.msA.hmt:1.1"] == MepPage.PageZone.TOP
        
    }

}
