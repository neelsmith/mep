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

// {urn:cts:greekLit:tlg5026.msA.hmt=[urn:cts:greekLit:tlg5026.msA.hmt:1.1, urn:cts:greekLit:tlg5026.msA.hmt:1.2, urn:cts:greekLit:tlg5026.msA.hmt:1.3, urn:cts:greekLit:tlg5026.msA.hmt:1.4, urn:cts:greekLit:tlg5026.msA.hmt:1.5, urn:cts:greekLit:tlg5026.msA.hmt:1.6, urn:cts:greekLit:tlg5026.msA.hmt:1.7, urn:cts:greekLit:tlg5026.msA.hmt:1.8, urn:cts:greekLit:tlg5026.msA.hmt:1.9, urn:cts:greekLit:tlg5026.msA.hmt:1.10, urn:cts:greekLit:tlg5026.msA.hmt:1.11, urn:cts:greekLit:tlg5026.msA.hmt:1.12, urn:cts:greekLit:tlg5026.msA.hmt:1.13, urn:cts:greekLit:tlg5026.msA.hmt:1.14, urn:cts:greekLit:tlg5026.msA.hmt:1.15, urn:cts:greekLit:tlg5026.msA.hmt:1.16], urn:cts:greekLit:tlg5026.msAext.hmt=[urn:cts:greekLit:tlg5026.msAext.hmt:1.25, urn:cts:greekLit:tlg5026.msAext.hmt:1.26], urn:cts:greekLit:tlg5026.msAil.hmt=[urn:cts:greekLit:tlg5026.msAil.hmt:1.322, urn:cts:greekLit:tlg5026.msAil.hmt:1.323, urn:cts:greekLit:tlg5026.msAil.hmt:1.324, urn:cts:greekLit:tlg5026.msAil.hmt:1.325, urn:cts:greekLit:tlg5026.msAil.hmt:1.326, urn:cts:greekLit:tlg5026.msAil.hmt:1.327, urn:cts:greekLit:tlg5026.msAil.hmt:1.328, urn:cts:greekLit:tlg5026.msAil.hmt:1.329, urn:cts:greekLit:tlg5026.msAil.hmt:1.330, urn:cts:greekLit:tlg5026.msAil.hmt:1.331], urn:cts:greekLit:tlg5026.msAim.hmt=[urn:cts:greekLit:tlg5026.msAim.hmt:1.17, urn:cts:greekLit:tlg5026.msAim.hmt:1.18, urn:cts:greekLit:tlg5026.msAim.hmt:1.19, urn:cts:greekLit:tlg5026.msAim.hmt:1.20, urn:cts:greekLit:tlg5026.msAim.hmt:1.21, urn:cts:greekLit:tlg5026.msAim.hmt:1.22, urn:cts:greekLit:tlg5026.msAim.hmt:1.23, urn:cts:greekLit:tlg5026.msAim.hmt:1.24], urn:cts:greekLit:tlg5026.msAint.hmt=[urn:cts:greekLit:tlg5026.msAint.hmt:1.27, urn:cts:greekLit:tlg5026.msAint.hmt:1.28, urn:cts:greekLit:tlg5026.msAint.hmt:1.29, urn:cts:greekLit:tlg5026.msAint.hmt:1.30, urn:cts:greekLit:tlg5026.msAint.hmt:1.31, urn:cts:greekLit:tlg5026.msAint.hmt:1.32], urn:cts:greekLit:tlg5026.msAlater.hmt=[urn:cts:greekLit:tlg5026.msAlater.hmt:1.332, urn:cts:greekLit:tlg5026.msAlater.hmt:1.333, urn:cts:greekLit:tlg5026.msAlater.hmt:1.334, urn:cts:greekLit:tlg5026.msAlater.hmt:1.335, urn:cts:greekLit:tlg5026.msAlater.hmt:1.336, urn:cts:greekLit:tlg5026.msAlater.hmt:1.337, urn:cts:greekLit:tlg5026.msAlater.hmt:1.338, urn:cts:greekLit:tlg5026.msAlater.hmt:1.339, urn:cts:greekLit:tlg5026.msAlater.hmt:1.340, urn:cts:greekLit:tlg5026.msAlater.hmt:1.341, urn:cts:greekLit:tlg5026.msAlater.hmt:1.342, urn:cts:greekLit:tlg5026.msAlater.hmt:1.343, urn:cts:greekLit:tlg5026.msAlater.hmt:1.344, urn:cts:greekLit:tlg5026.msAlater.hmt:1.345, urn:cts:greekLit:tlg5026.msAlater.hmt:1.346, urn:cts:greekLit:tlg5026.msAlater.hmt:1.347, urn:cts:greekLit:tlg5026.msAlater.hmt:1.348, urn:cts:greekLit:tlg5026.msAlater.hmt:1.349, urn:cts:greekLit:tlg5026.msAlater.hmt:1.350, urn:cts:greekLit:tlg5026.msAlater.hmt:1.351, urn:cts:greekLit:tlg5026.msAlater.hmt:1.352, urn:cts:greekLit:tlg5026.msAlater.hmt:1.353, urn:cts:greekLit:tlg5026.msAlater.hmt:1.354, urn:cts:greekLit:tlg5026.msAlater.hmt:1.355, urn:cts:greekLit:tlg5026.msAlater.hmt:1.356]}

    def expectedDocs =  ["urn:cts:greekLit:tlg5026.msA.hmt", "urn:cts:greekLit:tlg5026.msAext.hmt", "urn:cts:greekLit:tlg5026.msAil.hmt", "urn:cts:greekLit:tlg5026.msAim.hmt", "urn:cts:greekLit:tlg5026.msAint.hmt", "urn:cts:greekLit:tlg5026.msAlater.hmt"]

    void testMepPage() {
        def graph = new MepGraph(serverUrl)
        def scholiaMap = graph.getScholia(twelverecto)
        def docList = scholiaMap.keySet() as Set
        assert docList == expectedDocs as Set
    }

//         MepPage pg = new MepPage(twelverecto, graph)   
}
