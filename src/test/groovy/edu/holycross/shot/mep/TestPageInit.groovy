package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestPageInit extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)



    void testGraph() {
        MepPage pg = new MepPage(twelverecto, graph)
        assert pg.urn != null
        assert pg.iliadLines.size() > 0
        assert pg.scholiaMap.size() > 0
        assert pg.tokenCounts.size() > 0
        assert pg.commentary.size() > 0
        assert pg.roiForScholion.size() == pg.commentary.size()
        assert pg.pageRoI != null
        assert pg.numScholia == pg.roiForScholion.size()
        assert pg.numTokens > pg.numScholia
    }

    /*
    Integer numTokens

    BigDecimal pageTop
    BigDecimal pageHeight


    */

}
