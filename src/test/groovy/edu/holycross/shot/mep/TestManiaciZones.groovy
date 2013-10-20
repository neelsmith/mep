package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestManiaciZones extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)

    def expectedTop = 0.3750666667 
    def expectedMiddle = 0.6688333334
    def expectedBottom  = 0.9626

/*    def expectedTop = 0.3479666667
    def expectedMiddle = 0.6146333334
    def expectedBottom = 0.9626 */
    void testZones() {
        MepPage pg = new MepPage(twelverecto, graph)
        def zones = pg.maniaciZones
        assert zones[MepPage.PageZone.TOP] == expectedTop
        assert zones[MepPage.PageZone.MIDDLE] == expectedMiddle
        assert zones[MepPage.PageZone.BOTTOM] == expectedBottom
        assert zones[MepPage.PageZone.BOTTOM] == pg.pageBottom

    }

}
