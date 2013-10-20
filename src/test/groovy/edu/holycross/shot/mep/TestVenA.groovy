package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestVenA extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)

    // As of Oct., 2013, data on beta.hpcc.uh.edu
    Integer expectedPages = 188

    void testMS() {
        MepMS venA = new MepMS(graph)
        assert venA.pageSequence.size() == expectedPages
    }

}
