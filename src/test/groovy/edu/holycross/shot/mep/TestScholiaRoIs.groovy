package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestScholiaRoIs extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)

    Integer expectedSize = 67
    String expectedImageUrn = "urn:cite:hmt:vaimg.VA012RN-0013@0.14967545,0.55647936,0.65903016,0.23365826"
String expectedRoIValue = "0.14967545,0.55647936,0.65903016,0.23365826"

    void testGraph() {
        def roiMap =  graph.getScholiaRoIs(twelverecto.toString()) 
        assert roiMap.size() == expectedSize
        assert roiMap["urn:cts:greekLit:tlg5026.msA.hmt:1.11"] == expectedImageUrn
    }

    void testPage() {
        MepPage pg = new MepPage(twelverecto, graph)
        def roiMap = pg.getRoiForScholion()
        assert roiMap.size() == expectedSize
        assert roiMap["urn:cts:greekLit:tlg5026.msA.hmt:1.11"] == expectedRoIValue
    }
}
