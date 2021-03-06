package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestChurikZones extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)

    def expectedTop = 0.2305
    def expectedMiddle = 0.7245
    def expectedBottom = 0.9626

    void testZones() {
        MepPage pg = new MepPage(twelverecto, graph)
        def zones = pg.churikZones
        assert zones[MepPage.PageZone.TOP] == expectedTop
        assert zones[MepPage.PageZone.MIDDLE] == expectedMiddle
        assert zones[MepPage.PageZone.BOTTOM] == expectedBottom
        assert zones[MepPage.PageZone.BOTTOM] == pg.pageBottom
    }

}
