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


    LinkedHashMap surveyTokenCount(String docUrn) {
        def lastIdx = pageSequence.size() - 1
        def tokens = [:]
        pageSequence.each { pg ->
            CiteUrn pgUrn = new CiteUrn(pg)
            MepPage mp = new MepPage(pgUrn, graph)
            tokens[pg] = mp.tokenCounts[docUrn]
            if (mp.tokenCounts[docUrn] < 1) {
                System.err.println "${pg} has NO TOKENS in document ${docUrn}."
            } else {
                System.err.println "${pg}: ${tokens[pg]} tokens."
            }
        }
        return tokens
    }


    LinkedHashMap surveyScholiaCount(String docUrn) {
        def lastIdx = pageSequence.size() - 1
        def scholiaCount = [:]
        pageSequence.each { pg ->
            CiteUrn pgUrn = new CiteUrn(pg)
            MepPage mp = new MepPage(pgUrn, graph)
            def scholia = mp.getScholiaForDocument(docUrn)
            if (scholia != null) {
                scholiaCount[pg] = scholia.size()
                System.err.println "${pg}: ${scholiaCount[pg]} scholia"
            } else {
                System.err.println "${pg} has NO SCHOLIA in document ${docUrn}."
            }
        }
        return scholiaCount
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
