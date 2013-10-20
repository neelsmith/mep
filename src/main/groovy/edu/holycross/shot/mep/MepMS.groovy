package edu.holycross.shot.mep

import groovy.xml.MarkupBuilder

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

/** Class representing the Venetus A manuscript.
*/
class MepMS {
    
    /** Ordered list of page URNs for all pages with
    * scholia editions. */
    ArrayList pageSequence


    /** Graph object to query.*/    
    MepGraph graph

    /** Constructor initializing pageSequence list. */
    MepMS(MepGraph mg) {
        this.graph = mg
        this.pageSequence = graph.getEditedPages()
    }

    /** Maps number of lines of the Iliad on each edited
    * page.
    * @returns A map with with the number of Iliad lines
    * on each page keyed by page URN.
    */
    LinkedHashMap surveyIliadLines() {
        def lastIdx = pageSequence.size() - 1
        System.err.println "Survey iliad lines from " + pageSequence[0] + " to " + pageSequence[lastIdx]
        def lineCounts = [:]
        pageSequence.each { pg ->
            CiteUrn pgUrn = new CiteUrn(pg)
            MepPage mp = new MepPage(pgUrn, graph)
            lineCounts[pg] = mp.iliadLines.size()
            def maxIdx =  mp.iliadLines.size() - 1
            if (maxIdx > 0) {
            } else {
                System.err.println "${pg} has NO ILIAD LINES indexed."
            }
        }
        return lineCounts
    }

}
