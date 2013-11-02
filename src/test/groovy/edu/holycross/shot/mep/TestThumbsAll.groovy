package edu.holycross.shot.mep 

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestThumbsAll extends GroovyTestCase {
    // use default fuseki settings to test
    def serverUrl = "http://localhost:3030/ds/"
    CiteUrn twelverecto = new CiteUrn("urn:cite:hmt:msA.12r")
    MepGraph graph = new MepGraph(serverUrl)


    void testImage() {
        def imap = graph.getEditedThumbs()
        String expected = "urn:cite:hmt:vaimg.VA012RN-0013"
        System.err.println "Size: " + imap.size()
        System.err.println "First: " + imap[0]
//        assert imap[twelverecto.toString()] == expected
    }

}
