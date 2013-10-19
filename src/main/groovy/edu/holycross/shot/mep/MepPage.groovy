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

    /** Counts of token keyed by document URN.*/
    def tokenCounts = [:]

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
        this.tokenCounts = mepg.getTokenCounts(folioPage)

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


    Integer tokenCountForDocument(String urnString) {
        return tokenCounts[urnString]
    }

    Integer tokenCountForDocument(CtsUrn urn) {
        return tokenCounts[urn.toString()]
    }

    
    /**
    * Gets ordered list of scholia from a specified
    * document appearing on this page.
    * @param urn CTS URN of the document.
    * @returns An ordered list
    */
    ArrayList scholiaForDocument(CtsUrn urn) {
        return scholiaMap[urn.toString()]
    }
    
    /**
    * Gets ordered list of scholia from a specified
    * document appearing on this page.
    * @param urnString String value of the document's CTS URN.
    * @returns An ordered list
    */
    ArrayList scholiaForDocument(String urnString) {
        return scholiaMap[urnString]
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


    /** Finds number of scholia in all documents on this page.
    * @returns Number of scholia.
    */
    Integer countScholia() {
        Integer count = 0
        this.scholiaMap.keySet().each { k ->
            count = count + scholiaMap[k].size()
        }
        return count
    }


    /** Finds number of scholia in all documents on this page.
    * @returns Number of scholia.
    */
    Integer getNumScholia() {
        return countScholia()
    }



    /** Finds number of scholia in a given document on this page.
    * @param docUrn CtsUrn of the document.
    * @returns Number of scholia.
    */
    Integer getNumScholia(CtsUrn docUrn) {
        return this.scholiaMap[docUrn.toString()].size()
    }

    /** Finds number of scholia in a given document on this page.
    * @param docUrnString String value of CtsUrn of the document.
    * @returns Number of scholia.
    */
    Integer getNumScholia(String docUrnString) {
        return this.scholiaMap[docUrnString].size()
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
