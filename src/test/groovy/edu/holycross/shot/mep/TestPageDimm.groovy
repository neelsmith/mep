package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestPageDimm extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)
    def expectedHeight =  0.8000
    def expectedTop = 0.0813

    void testGraph() {
        MepPage pg = new MepPage(twelverecto, graph)
        System.err.println "Top " + pg.pageTop + ", height " + pg.pageHeight
        assert pg.pageTop == expectedTop
        assert pg.pageHeight == expectedHeight
    }

}
