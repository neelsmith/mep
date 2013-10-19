package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestTokenCounts extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")

    def expectedDocs = ["urn:cts:greekLit:tlg5026.msAint.hmt", "urn:cts:greekLit:tlg5026.msAim.hmt", "urn:cts:greekLit:tlg5026.msAext.hmt", "urn:cts:greekLit:tlg5026.msAil.hmt", "urn:cts:greekLit:tlg5026.msA.hmt"]

    void testMepGraph() {
        def graph = new MepGraph(serverUrl)
        def tokenMap = graph.getTokenCounts(twelverecto)
        // keySet() not guaranteed to retain order, so test for
        // set membership
        def docList = tokenMap.keySet() as Set
        assert docList == expectedDocs as Set
    }


    void testMepPage() {
        def graph = new MepGraph(serverUrl)
        MepPage pg = new MepPage(twelverecto, graph)   

        CtsUrn mainScholia = new CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt")

        // add test on mapped scholia...
        def tokenMap = pg.getTokenCounts()
        // keySet() not guaranteed to retain order, so test for
        // set membership
        def docList = tokenMap.keySet() as Set
        assert docList == expectedDocs as Set
        
        Integer expectedMain = 918
        assert tokenMap["urn:cts:greekLit:tlg5026.msA.hmt"] == expectedMain
        assert pg.tokenCountForDocument(mainScholia) == expectedMain
        assert pg.tokenCountForDocument(mainScholia.toString()) == expectedMain
    }
}
