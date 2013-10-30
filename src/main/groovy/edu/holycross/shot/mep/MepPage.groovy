package edu.holycross.shot.mep

import groovy.xml.MarkupBuilder

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

/** Class representing a single physical page of the Venetus A manuscript.
* A MepPage object draws on a MepGraph to collect all the information about the
* physical page it represents necessary to analyze the layout of the page in
* either of the Maniaci or Churik models.  Analyzing the layout is the
* task of a MepLayout object.
*/
class MepPage {

    /** Divisions of page in either Maniaci or Churik system. */
    enum PageZone {
        TOP, MIDDLE, BOTTOM
    }

    /** Graph object to query.*/
    MepGraph mepg

    /** URN of the page to analyze. */
    CiteUrn urn


    /** Strinv value of URN of default image */
    String img

    /** An ordered list of Iliad lines appearing on this page. */
    def iliadLines = []
    
    /** Ordered lists of scholia for a folio
    * in a map keyed by document URN. */
    def scholiaMap = [:]

    /** Numbers of token occuring in a group of scholia, keyed by document URN.*/
    def tokenCounts = [:]

    /** Map of scholion URNs to URNs of Iliad passages.*/
    def commentary = [:]

    /** Map of RoI values for each scholion.
    */
    def roiForScholion = [:]

    /** Four-element string indicating
    * area of image representing the folio page.
    */
    String pageRoI

    /** Four-element string indicating
    * area of image representing the Iliad block.
    */
    String iliadRoI

    /** Number of scholia on page */
    Integer numScholia

    /** Number of tokens in scholia texts on page.*/
    Integer numTokens

    // Not needed (yet?) for mep but very fun to have!
    // def tokensByScholion = [:]

    /** Scale-independent value giving top of page
    * on default image.*/
    BigDecimal pageTop

    /** Scale-independent value giving height of page
    * on default image. */
    BigDecimal pageBottom

    /** Map, keyed by PageZone values, of lower bounds
    * of Maniaci's three equal page zones.
    */
    def maniaciZones = [:]


    /** Map, keyed by PageZone values, of lower bounds
    * of three page zones defined by position of Iliad block
    * in Churik's hypothesis.
    */
    def churikZones = [:]

    /** Constructor initializes all data for this folio page.
    * @param folioPage URN of page to analyze.
    * @param mepGraph Graph object providing data.
    */
    MepPage(CiteUrn folioPage, MepGraph mepGraph ) {
        this.urn = folioPage
        this.mepg = mepGraph
        this.iliadLines = mepg.getIliad(this.urn)
        this.scholiaMap = mepg.getScholia(this.urn)
        this.tokenCounts = mepg.getTokenCounts(this.urn)
        this.commentary = mepg.getScholiaIliadMap(this.urn)
        this.numScholia = this.commentary.size()
        this.numTokens = this.countTotalTokens()
        this.pageRoI = mepg.getPageBlock(this.urn)
        this.iliadRoI = mepg.getIliadBlock(this.urn)
        // add default image
        this.img = mepg.getDefaultImage(this.urn)

        getRoIsForScholia()
        // set pageTop and pageBottom values, and
        // caclulate vertical zones:
        calculatePageDimm()
        calculateManiaciZones()
        calculateChurikZones()
    }


    /** For each scholion on this page, extracts RoI portion of
    * mapped image, and stores that in the roiForScholion map.
    */
    void getRoIsForScholia() {
        def roiMap =  mepg.getScholiaRoIs(this.urn)
        roiMap.keySet().each { k ->
            CiteUrn imgUrn = new CiteUrn(roiMap[k])
            roiForScholion[k] = imgUrn.getExtendedRef()
        }
    }

    /** Computes lower bounds of three equal zones
    * on the physical page, and assigns these values
    * to the maniaciZones map, keyed by PageZone value.
    */
    void calculateManiaciZones() {
        if ((pageBottom != null) && (pageTop != null)) {
            def height = pageBottom - pageTop
            def thirds = height.div(3)
            maniaciZones[PageZone.TOP] = pageTop + thirds
            maniaciZones[PageZone.MIDDLE] = pageTop + 2*thirds
            maniaciZones[PageZone.BOTTOM] = pageBottom
        }
    }


    // range is 23-27 lines, apart from very short 
    // selections on book ending pages.
    //
    /** Computes lower bounds of three zones on physical
    * page defined by location of Iliad block, and assigns
    * these values to the churikZones map, keyed by PageZone value.
    */
    void calculateChurikZones() {
        if (this.iliadRoI != null) {
            def vals = this.iliadRoI.split(",")
            BigDecimal iliadTop = vals[1].toBigDecimal()
            BigDecimal iliadHt = vals[3].toBigDecimal()
            churikZones[PageZone.TOP] = iliadTop
            churikZones[PageZone.MIDDLE] = iliadTop + iliadHt
            churikZones[PageZone.BOTTOM] = pageBottom
        }
    }

    /** Finds number of tokens in all scholia for this page.
    * @returns Number of tokens in all scholia for this page.
    */
    Integer countTotalTokens() {
        Integer count = 0
        this.tokenCounts.keySet().each { k ->
            count = count + tokenCounts[k]
        }
        return count
    }

    /** Finds number of tokens in all scholia belonging
    * to a given scholia document.
    * @param urnString String value of document's CTS URN.
    * @returns Number of tokens in all scholia for this page.
    */
    Integer countTokensForDocument(String urnString) {
        return tokenCounts[urnString]
    }

    /** Finds number of tokens in all scholia belonging
    * to a given scholia document.
    * @param urn The document's CTS URN.
    * @returns Number of tokens in all scholia for this page.
    */
    Integer countTokensForDocument(CtsUrn urn) {
        return tokenCounts[urn.toString()]
    }
    
    /**
    * Gets ordered list of scholia from a specified
    * document appearing on this page.
    * @param urn CTS URN of the document.
    * @returns An ordered list
    */
    ArrayList getScholiaForDocument(CtsUrn urn) {
        return scholiaMap[urn.toString()]
    }
    
    /**
    * Gets ordered list of scholia from a specified
    * document appearing on this page.
    * @param urnString String value of the document's CTS URN.
    * @returns An ordered list
    */
    ArrayList getScholiaForDocument(String urnString) {
        return scholiaMap[urnString]
    }

    /** Initializes pageTop and pageBottom members.
    */
    void calculatePageDimm() {
        if (this.pageRoI  != null) {
            def vals = this.pageRoI.split(",")
            this.pageTop = vals[1].toBigDecimal()
            BigDecimal ht = vals[3].toBigDecimal()
            this.pageBottom = ht + this.pageTop
        }
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


}
