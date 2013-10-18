package edu.holycross.shot.mep

import groovy.xml.MarkupBuilder

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn


class MepPage {


    enum ChurikZone {
        TOP, EXTERIOR, BOTTOM
    }

    /** Graph object to query.*/
    MepGraph mepg

    /** URN of the page to analyze */
    CiteUrn urn
    
    /** Unordered list of URNs of scholia
    * from all documents on this folio.
    */
    def scholionList = []


    /** Ordered lists of scholia for a folio
    * in a map keyed by document URN. */
    def scholiaMap = [:]

    /** CITE Image URN with ROI indicating
    * area of image representing the the folio page.
    */
    def pageRoI

    /** Scale-independent value giving top of page
    * on default image.*/
    BigDecimal pageTop

    /** Scale-independent value giving height of page
    * on default image. */
    BigDecimal pageHeight

    /** Map of RoI values for each scholion.
    * The key set for this map should be an
    * identical set to scholionList. 
    */
    def roiForScholion = [:]


    /** Map of Iliad line commented on
    *  each scholion.
    * The key set for this map should be an
    * identical set to scholionList. 
    */
    def commentsOn  = [:]


    /** An ordered list of */
    def iliadLines = []

    /** Number of scholia on page */
    Integer numScholia

    /** Number of tokens in scholia texts on page.*/
    Integer numTokens

    // Not needed for MeP but very fun to have!
    // def tokensByScholion = [:]


    /** Constructor initializes all data for this folio page.
    * @param folioPage URN of page to analyze.
    * @param mepGraph Graph object providing data.
    */
    MepPage(CiteUrn folioPage, MepGraph mepGraph ) {
        this.urn = folioPage
        this.mepg = mepGraph
        this.iliadLines = mepg.getIliad(folioPage)
        this.scholiaMap = mepg.getScholia(folioPage)

        // Ditch this kludge:
        initializeData()
    }


    ChurikZone rankPosition(BigDecimal pos) {
        if (pos > 0.75) {
            return ChurikZone.BOTTOM
        } else if (pos > 0.25) {
            return ChurikZone.EXTERIOR
        } else {
            return ChurikZone.TOP
        }
    }

    
    // get scholia for a single group
    ArrayList scholiaForSiglum(String siglum) {
        def scholGroup = []
        scholionList.each { s ->
            CtsUrn urn = new CtsUrn(s)
            if (urn.getWork() == siglum) {
                scholGroup.add(s)
            }
        }
        return scholGroup
    }

    boolean scholMatchesIliad(String ctsUrnVal) {
        ChurikZone scholZone = rankPosition(scholionPosition(ctsUrnVal))
        ChurikZone iliadZone  = rankPosition(iliadPosition(commentsOn[ctsUrnVal]))
        return scholZone == iliadZone
    }


    BigDecimal scholionPosition(String ctsUrnVal) {
        CiteUrn urn = new CiteUrn( roiForScholion[ctsUrnVal])
        def vals = urn.getExtendedRef().split(",")
        BigDecimal top = vals[1].toBigDecimal() - pageTop
        // get page top, and page height.
        // this position will be quotient of height from page top
        // divided by page height.
        return top / pageHeight
    }

    /** Computes the position of a given Iliad line
    * on the page as a quotient of its posiion and
    * the total number of lines. This quotient is
    * directly comparable with the values for scholia
    * position.
    * @param ctsUrnVal URN, as a String, of the line.
    * @returns Line position from top of page divided by total 
    * number of Iliadic lines.
    */
    BigDecimal iliadPosition(String ctsUrnVal) {
        Integer pos
        this.iliadLines.eachWithIndex { ln, index ->
            if (ln == ctsUrnVal) {
                pos = index
            }
        }
        return (pos / iliadLines.size() )
    }

    /** Retrieves "magic page query" from MepGraph, and
    * puts its flattened results into data structures useful
    * for MeP analysis. This is the method that turns intimate
    * knowledge of the convoluted graph into usable data.
    */
    void initializeData() {
        def pageBindings = mepg.pageJson("${urn.toString()}")
        this.numTokens = pageBindings.size()
        pageBindings.each { b ->
            this.pageRoI = b.pageroi.value
            def schol = b.schol.value
            if (!scholionList.contains(schol)) {
                scholionList.add(schol)
                roiForScholion[schol] = b.scholroi.value
                commentsOn[schol] = b.il.value
            }
        }
        this.numScholia = countScholia()

        CiteUrn pgRoiUrn = new CiteUrn(pageRoI)
        def vals = pgRoiUrn.getExtendedRef().split(",")
        pageTop = vals[1].toBigDecimal()
        BigDecimal ht = vals[3].toBigDecimal()
        pageHeight = ht - pageTop
        
    }

    Integer countScholia() {
        Integer count = 0
        this.scholiaMap.keySet().each { k ->
            count = count + scholiaMap[k].size()
        }
        return count
    }


    /** Formats an xml report about this page. */
/*
    String pageReport() {
        def writer = new StringWriter()
        def rept = new MarkupBuilder(writer)
        def roiMap = mepg.getScholiaRois(this.urn)
        rept.page() {
            request {
                page("${this.urn}")
            }
            reply {
                folio("${this.urn}")
                img("${mepg.getDefaultImg(this.urn)}")
                def scoreMap = mepg.getScholiaScore(this.urn)
                scoreMap.keySet().each { s ->
                    scholion(urn : "${s}")  {
                        if (scoreMap[s] == true) {
                            score ("1")
                        } else {
                            score ("0")
                        }
                        roi("${roiMap[s]}")
                    }
                }
                

            }
        }
        return writer.toString()
    }
*/
}
