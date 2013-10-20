package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestCompare extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)
    def expectedDiffList = [["urn:cts:greekLit:tlg5026.msA.hmt:1.6", MepPage.PageZone.TOP, MepPage.PageZone.MIDDLE], ["urn:cts:greekLit:tlg5026.msA.hmt:1.7", MepPage.PageZone.TOP, MepPage.PageZone.MIDDLE]]

    void testRankCompare() {
        MepPage pg = new MepPage(twelverecto, graph)
        MepLayout ml = new MepLayout(pg)
        
        def mRanks = ml.rankScholia(pg.getScholiaForDocument("urn:cts:greekLit:tlg5026.msA.hmt"), pg.maniaciZones)
        def cRanks = ml.rankScholia(pg.getScholiaForDocument("urn:cts:greekLit:tlg5026.msA.hmt"), pg.churikZones)

        assert mRanks.keySet() == cRanks.keySet()
        def diffList = ml.compareMaps(mRanks, cRanks)
        assert diffList.size() == expectedDiffList.size()
        assert diffList == expectedDiffList
    }


}
