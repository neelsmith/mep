package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestPageIliad extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"

    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")

    def expectedList = ["urn:cts:greekLit:tlg0012.tlg001.msA:1.1", "urn:cts:greekLit:tlg0012.tlg001.msA:1.2", "urn:cts:greekLit:tlg0012.tlg001.msA:1.3", "urn:cts:greekLit:tlg0012.tlg001.msA:1.4", "urn:cts:greekLit:tlg0012.tlg001.msA:1.5", "urn:cts:greekLit:tlg0012.tlg001.msA:1.6", "urn:cts:greekLit:tlg0012.tlg001.msA:1.7", "urn:cts:greekLit:tlg0012.tlg001.msA:1.8", "urn:cts:greekLit:tlg0012.tlg001.msA:1.9", "urn:cts:greekLit:tlg0012.tlg001.msA:1.10", "urn:cts:greekLit:tlg0012.tlg001.msA:1.11", "urn:cts:greekLit:tlg0012.tlg001.msA:1.12", "urn:cts:greekLit:tlg0012.tlg001.msA:1.13", "urn:cts:greekLit:tlg0012.tlg001.msA:1.14", "urn:cts:greekLit:tlg0012.tlg001.msA:1.15", "urn:cts:greekLit:tlg0012.tlg001.msA:1.16", "urn:cts:greekLit:tlg0012.tlg001.msA:1.17", "urn:cts:greekLit:tlg0012.tlg001.msA:1.18", "urn:cts:greekLit:tlg0012.tlg001.msA:1.19", "urn:cts:greekLit:tlg0012.tlg001.msA:1.20", "urn:cts:greekLit:tlg0012.tlg001.msA:1.21", "urn:cts:greekLit:tlg0012.tlg001.msA:1.22", "urn:cts:greekLit:tlg0012.tlg001.msA:1.23", "urn:cts:greekLit:tlg0012.tlg001.msA:1.24", "urn:cts:greekLit:tlg0012.tlg001.msA:1.25"]


    void testMepGraph() {
        def mepg = new MepGraph(serverUrl)
        assert mepg.getIliad(twelverecto) == expectedList
    }


    void testMepPage() {
        def graph = new MepGraph(serverUrl)
        MepPage pg = new MepPage(twelverecto, graph)
        pg.getIliadLines() == expectedList
    }
   
}
