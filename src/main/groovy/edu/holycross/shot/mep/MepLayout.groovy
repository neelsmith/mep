package edu.holycross.shot.mep

import groovy.xml.MarkupBuilder

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

/** "Layout" class to analyze data in a MepPage object.
*/
class MepLayout {

    /** Current page to analyze. */
    MepPage pg

    /** Constructor initialized to be able to analyze
    * a given page.
    * @param mepPage The page to analyze.
    */
    MepLayout(MepPage mepPage) {
        this.pg = mepPage
    }

    /** Finds difference between two maps with identical 
    * keysets.  Although a generic functino, this is useful to 
    * compare Maniaci rankings and Churik rankings for a given 
    * set of scholia.
    * @param mapA First map.
    * @param mapB Second map.
    * @returns A list of difference records.  Each record consists of a triplet,
    * consisting of the shared key, the mapA value for the key and the
    * mapB value for the key.
    */
    ArrayList compareMaps(LinkedHashMap mapA, LinkedHashMap mapB) {
        def diffList = []

        def keysA = mapA.keySet().sort()
        def keysB = mapB.keySet().sort()
        assert keysA == keysB
        keysA.each { k ->
            if (mapA[k] != mapB[k]) {
                def record = [k, mapA[k], mapB[k]]
                diffList.add(record)
            }
        }
        return diffList
    }


    /** 
    * Evaluates a list of scholia against a given map of 
    * zone values and assigns each scholion to one of the
    * zone by comparing the top (initial y) value of the 
    * scholion's RoI with the bottom value of the zone.
    * @param scholia A list of CTS URNs for scholia.
    * @param zones A map of image coordinate values
    * keyed by MepPage.PageZone.
    * @returns A map of scholia URNs to MepPage.PageZone values.
    * If the scholion's RoI falls below the page bottom or
    * above the page top, the mapped value is null.
    */
    LinkedHashMap rankScholia(CtsUrn urn, LinkedHashMap zones) {
        return rankScholia(urn.toString(), zones)
    }

    /** 
    * Evaluates a list of scholia against a given map of 
    * zone values and assigns each scholion to one of the
    * zone by comparing the top (initial y) value of the 
    * scholion's RoI with the bottom value of the zone.
    * @param scholia A list of CTS URNs for scholia.
    * @param zones A map of image coordinate values
    * keyed by MepPage.PageZone.
    * @returns A map of scholia URNs to MepPage.PageZone values.
    * If the scholion's RoI falls below the page bottom or
    * above the page top, the mapped value is null.
    */
    LinkedHashMap rankScholia(ArrayList scholia, LinkedHashMap zones) {
        def rankings = [:]
        scholia.each { s ->
            def roi = pg.roiForScholion[s.toString()].split(/,/)
            def top = roi[1] as BigDecimal

            if (top < pg.pageTop) {
                rankings[s] = null
                System.err.println "rankScholia: ERROR on ${s}: ${top} < ${pg.pageTop} (page top)."
            } else if (top < zones[MepPage.PageZone.TOP]) {
                rankings[s] = MepPage.PageZone.TOP
            } else if (top < zones[MepPage.PageZone.MIDDLE]) {
                rankings[s] = MepPage.PageZone.MIDDLE
            }  else if (top < zones[MepPage.PageZone.BOTTOM]) {
                rankings[s] = MepPage.PageZone.BOTTOM
            } else {
                rankings[s] = null
                System.err.println "rankScholia: ERROR on ${s}: ${top} > ${zones[MepPage.PageZone.BOTTOM]} (page bottom)"
            }
        }
        return rankings
    }


    // ADD CONSTRAINTS BASED ON NUM LINES ON PAGE>
    // only apply if >=23 and <=27
    // 6 18 6
    // TEST FOR NUM LINES ON PAGE...?
    // CHECK DENSITY OF SCHOLIA ZONE..?

    LinkedHashMap computeChurikRankForIliad() {
        def rankings = [:]
        System.err.println "Page has " + pg.iliadLines.size() + " lines."
        Integer count = 0
        pg.iliadLines.each { ln ->
            count++;
            if (count < 7) {
                rankings[ln] = MepPage.PageZone.TOP
            } else if (count < 18) {
                rankings[ln] = MepPage.PageZone.MIDDLE
            } else {
                rankings[ln] = MepPage.PageZone.BOTTOM
            }
        }
        return rankings
    }
    

// methods for ranking Iliad differ between manaici and churik:
// maniaci is straight zone comparison.  Divide Iliad block by num lines,
// churcik is by line count:  6, 18, 6



    def computeManiaciRankForIliad() {
        def rankings = [:]
        // can treat spacing of lines within Iliad block as equal,
        // so divide ht of Iliad zone by num lines
        def vals = pg.pageRoI.split(/,/)
        BigDecimal iliadTop = vals[1].toBigDecimal()
        BigDecimal iliadHt = vals[3].toBigDecimal()
        Integer numLines = pg.iliadLines.size()
        def lineSize = iliadHt.div(numLines)
        
        Integer count =  0
        pg.iliadLines.each { ln ->
           def lineTop = (count * lineSize) + iliadTop
           count++;
           System.err.println "COMPARE ${lineTop} to zones ${pg.churikZones}: "
           
           if (lineTop < pg.pageTop) {
               rankings[ln] = null
               System.err.println "rankScholia: ERROR on ${ln}: ${top} < ${pg.pageTop} (page top)."
           } else if (lineTop < pg.churikZones[MepPage.PageZone.TOP]) {
               rankings[ln] = MepPage.PageZone.TOP
           } else if (lineTop < pg.churikZones[MepPage.PageZone.MIDDLE]) {
                rankings[ln] = MepPage.PageZone.MIDDLE
            }  else if (lineTop < pg.churikZones[MepPage.PageZone.BOTTOM]) {
                rankings[ln] = MepPage.PageZone.BOTTOM
            } else {s
                rankings[ln] = null
                System.err.println "rankScholia: ERROR on ${ln}: ${top} > ${pg.churikZones[MepPage.PageZone.BOTTOM]} (page bottom)"
            }
           System.err.println "\t${rankings[ln]}"
        }
        return rankings
    }






/*
    boolean scholMatchesIliad(String ctsUrnVal) {
        ChurikZone scholZone = rankPosition(scholionPosition(ctsUrnVal))
        ChurikZone iliadZone  = rankPosition(iliadPosition(commentary[ctsUrnVal]))
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
*/

    /** Computes the position of a given Iliad line
    * on the page as a quotient of its posiion and
    * the total number of lines. This quotient is
    * directly comparable with the values for scholia
    * position.
    * @param ctsUrnVal URN, as a String, of the line.
    * @returns Line position from top of page divided by total 
    * number of Iliadic lines.
    */

/*
    BigDecimal iliadPosition(String ctsUrnVal) {
        Integer pos
        this.iliadLines.eachWithIndex { ln, index ->
            if (ln == ctsUrnVal) {
                pos = index
            }
        }
        return (pos / iliadLines.size() )
    }
*/




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
/*
    ChurikZone rankPosition(BigDecimal pos) {
        if (pos > 0.75) {
            return ChurikZone.BOTTOM
        } else if (pos > 0.25) {
            return ChurikZone.EXTERIOR
        } else {
            return ChurikZone.TOP
        }
    }
*/



}
