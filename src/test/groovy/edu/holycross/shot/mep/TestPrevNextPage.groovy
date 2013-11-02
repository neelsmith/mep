package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestPrevNextPage extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)


    void testPN() {
        MepPage pg = new MepPage(twelverecto, graph)

        assert pg.getPrevPage() == "urn:cite:hmt:msA.11v"
        assert pg.getNextPage() == "urn:cite:hmt:msA.12v"

    }

}
