package edu.holycross.shot.mep

import groovy.xml.MarkupBuilder

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

/** The Venetus A manuscript.
*/
class MepMS {
    
    ArrayList pageSequence
    

    // As of Oct., 2013, data on beta.hpcc.uh.edu:
    Integer expectedPages = 188

    MepMS(MepGraph mg) {
        this.pageSequence = mg.getEditedPages()
        assert pageSequence.size() == expectedPages
    }

}
