package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestPageScholia extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")

    def expectedDocs =  ["urn:cts:greekLit:tlg5026.msA.hmt", "urn:cts:greekLit:tlg5026.msAext.hmt", "urn:cts:greekLit:tlg5026.msAil.hmt", "urn:cts:greekLit:tlg5026.msAim.hmt", "urn:cts:greekLit:tlg5026.msAint.hmt", "urn:cts:greekLit:tlg5026.msAlater.hmt"]

    def expectedMain = ["urn:cts:greekLit:tlg5026.msA.hmt:1.1", "urn:cts:greekLit:tlg5026.msA.hmt:1.2", "urn:cts:greekLit:tlg5026.msA.hmt:1.3", "urn:cts:greekLit:tlg5026.msA.hmt:1.4", "urn:cts:greekLit:tlg5026.msA.hmt:1.5", "urn:cts:greekLit:tlg5026.msA.hmt:1.6", "urn:cts:greekLit:tlg5026.msA.hmt:1.7", "urn:cts:greekLit:tlg5026.msA.hmt:1.8", "urn:cts:greekLit:tlg5026.msA.hmt:1.9", "urn:cts:greekLit:tlg5026.msA.hmt:1.10", "urn:cts:greekLit:tlg5026.msA.hmt:1.11", "urn:cts:greekLit:tlg5026.msA.hmt:1.12", "urn:cts:greekLit:tlg5026.msA.hmt:1.13", "urn:cts:greekLit:tlg5026.msA.hmt:1.14", "urn:cts:greekLit:tlg5026.msA.hmt:1.15", "urn:cts:greekLit:tlg5026.msA.hmt:1.16"]


    void testMepGraph() {
        def graph = new MepGraph(serverUrl)
        def scholiaMap = graph.getScholia(twelverecto)
        // keySet() not guaranteed to retain order, so test for
        // set membership
        def docList = scholiaMap.keySet() as Set
        assert docList == expectedDocs as Set

        // each document list should be ordered, so test for
        // list equivalence
        def venAmain = scholiaMap["urn:cts:greekLit:tlg5026.msA.hmt"]
        assert venAmain == expectedMain
    }


    void testMepPage() {
        def graph = new MepGraph(serverUrl)
        MepPage pg = new MepPage(twelverecto, graph)   

        CtsUrn mainScholia = new CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt")

        // add test on mapped scholia...
        def scholiaMap = pg.getScholiaMap()
        // keySet() not guaranteed to retain order, so test for
        // set membership
        def docList = scholiaMap.keySet() as Set
        assert docList == expectedDocs as Set

        // each document list should be ordered, so test for
        // list equivalence
        def venAmain = scholiaMap[mainScholia.toString()]
        assert venAmain == expectedMain
        
        assert pg.scholiaForDocument(mainScholia) == expectedMain
    }
}
