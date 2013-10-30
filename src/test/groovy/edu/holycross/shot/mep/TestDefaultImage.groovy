package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestDefaultImage extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)


    void testImage() {
        MepPage pg = new MepPage(twelverecto, graph)
        String expected = "urn:cite:hmt:vaimg.VA012RN-0013"
        assert pg.getImg() == expected
    }

}
