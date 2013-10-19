package edu.holycross.shot.mep

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import groovyx.net.http.*
import groovyx.net.http.HttpResponseException
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*



/**  
*/
class MepGraph {

    /** SPARQL query endpoint for HMT graph triples.   */
    String tripletServerUrl

    /** QueryGenerator object formulating SPARQL query strings. */
    QueryGenerator qg

    /** XML namespace for SPARQL vocabulary, as groovy Namespace object. */
    static groovy.xml.Namespace sparql = new groovy.xml.Namespace("http://www.w3.org/2005/sparql-results#")

/*
    enum ChurikZone {
        TOP, EXTERIOR, BOTTOM
    }
*/

    /** Constructor initializing required value for SPARQL endpoint.   */
    MepGraph(String serverUrl) {
        this.tripletServerUrl = serverUrl
        this.qg = new QueryGenerator()
    }






    // THIS SHOULD BE KILLED?
    /** Submits "magic query" that discovers information
    * down to the token level.
    * @param pageUrnStr Page to query for.
    * @returns An ArrayList of JSON bindings.
    */
    ArrayList pageJson(String pageUrnStr) {
        String folioReply = getSparqlReply("application/json", qg.magicQueryForFolio(pageUrnStr))

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(folioReply)
        return parsedReply.results.bindings
    }



    String getIliadBlock(String pageUrnStr) {
        String blockReply = getSparqlReply("application/json", qg.iliadBlock(pageUrnStr))
        CiteUrn img

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(blockReply)
        parsedReply.results.bindings.each { b ->
            img = new CiteUrn(b.imgroi.value)
        }
        return img.getExtendedRef()
    }




    /** Gets region of interest of physical page on its default image.
    * @param pageUrnStr String value of the page's URN.
    * @returns A String with four comma-delimited values in CITE RoI format.
    */
    String getPageBlock(String pageUrnStr) {
        String blockReply = getSparqlReply("application/json", qg.pageBlock(pageUrnStr))
        CiteUrn img

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(blockReply)
        parsedReply.results.bindings.each { b ->
            img = new CiteUrn(b.imgroi.value)
        }
        return img.getExtendedRef()
    }

    LinkedHashMap getTokenCounts(CiteUrn urn) {
        return getTokenCounts(urn.toString())
    }

    LinkedHashMap getTokenCounts(String pageUrnStr) {
        def tokens = [:]
        String q = this.qg.tokenCounts(pageUrnStr)
        String scholiaReply = getSparqlReply("application/json",q)

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(scholiaReply)

        parsedReply.results.bindings.each { b ->
            Integer count = b.num.value.toInteger()
            tokens[b.doc.value] = count
        }
        return tokens
    }


    /** Gets a series of ordered lists of scholia.
    * The lists are collected in a map keyed by CTS URN for the
    * document.
    * @param urn URN of the page.
    * @returns An ordered list of Iliad URNs.
    */
    LinkedHashMap getScholia(CiteUrn urn) {
        return getScholia(urn.toString())
    }


    /** Gets a series of ordered lists of scholia.
    * The lists are collected in a map keyed by CTS URN for the
    * document.
    * @param pageUrn URN value, as a String, for the page.
    * @returns An ordered list of Iliad URNs.
    */
    LinkedHashMap getScholia(String pageUrn) {
        def scholia = [:]
        String q = this.qg.orderedScholia(pageUrn)
        String scholiaReply = getSparqlReply("application/json",q)

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(scholiaReply)

        String currDoc = ""
        parsedReply.results.bindings.each { b ->
            if (currDoc != b.doc.value) {
                currDoc = b.doc.value
            }
            if (scholia[currDoc]) {
                def docList = scholia[currDoc]
                docList.add(b.schol.value)
                scholia[currDoc] = docList
            } else {
                def docList = ["${b.schol.value}"]
                scholia[currDoc] = docList
            }

        }
        return scholia
    }

    /** Gets an ordered list of Iliad URNs for a given page.
    * @param pageUrn URN of the page.
    * @returns An ordered list of Iliad URNs.
    */
    ArrayList getIliad(CiteUrn pageUrn) {
        return getIliad(pageUrn.toString())
    }



    /** Gets an ordered list of Iliad URNs for a given page.
    * @param pageUrn URN value of the page.
    * @returns An ordered list of Iliad URNs.
    */
    ArrayList getIliad(String pageUrn) {
        def lines = []
        String q = this.qg.orderedIliadLines(pageUrn)
        String iliadReply = getSparqlReply("application/json",q)

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(iliadReply)

        parsedReply.results.bindings.each { b ->
            if (b.il?.value) {
                lines.add(b.il.value)
            }
        }
        return lines
    }


    def getRangeForBook(CtsUrn iliadBookUrn) {
    }



    /** Submits a SPARQL query to the configured endpoint
    * and returns the text of the reply.
    * @param acceptType  Value to use for headers.Accept in 
    * http request.  If the value of acceptType is 'applicatoin/json'
    * fuseki's additional 'output' parameter is added to the 
    * http request string so that the string returned for the
    * the request will be in JSON format.  This separates the 
    * concerns of forming SPARQL queries from the decision about
    * how to parse the reply in a given format.
    * @param query Text of SPARQL query to submit.
    * @returns Text content of reply. 
    */
    String getSparqlReply(String acceptType, String query) {
        String replyString
        def encodedQuery = URLEncoder.encode(query)
        def q = "${tripletServerUrl}query?query=${encodedQuery}"
        if (acceptType == "application/json") {
            q +="&output=json"
        }
        def http = new HTTPBuilder(q)
        http.request( Method.GET, ContentType.TEXT ) { req ->
            headers.Accept = acceptType
            response.success = { resp, reader ->
                replyString = reader.text
            }
        }
        return replyString
    }


    /** Finds the default Image for a folio.
    * @param urn CITE URN of the folio to find image for.
    * @returns The CITE URN of the default image for this folio,
    * as a String.
    */

    // NOT NEEDED
/*
    String getDefaultImg(CiteUrn urn) {
        String imgUrnStr = null
        String imgReply = getSparqlReply("application/json", qg.defaultImageQuery(urn))

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(imgReply)

        parsedReply.results.bindings.each { b ->
            if (b.img) {
                imgUrnStr = b.img.value
            }
        }
        return imgUrnStr
    }

*/


    /** Gets an ordered list of folio page URNs for a range 
    * of folios identified by CITE URN. 
    * @param firstPage URN value of the first folio page in the range.
    * @param lastPage URN value of the last folio page in the range.
    * @returns An ordered list of URN values for folio pages, beginning
    * with firstPage and ending with lastPage.
    */
    ArrayList getFoliosForRange(String firstPage, String lastPage) {
        try {
            CiteUrn firstUrn = new CiteUrn(firstPage)
            CiteUrn lastUrn = new CiteUrn(lastPage)
            return getFoliosForRange(firstUrn,lastUrn)
        } catch (Exception e) {
            throw e
        }
    }

    /** Gets an ordered list of folio page URNs for a range 
    * of folios identified by CITE URN. 
    * @param firstPage URN of the first folio page in the range.
    * @param lastPage URN of the last folio page in the range.
    * @returns An ordered list of URN values for folio pages, beginning
    * with firstPage and ending with lastPage.
    */
    ArrayList getFoliosForRange(CiteUrn firstPage, CiteUrn lastPage) {
        def folios = []
        String msUrnStr = "urn:cite:${firstPage.getNs()}:${firstPage.getCollection()}"
        String foliosReply = getSparqlReply("application/json", qg.getFolioRange(firstPage, lastPage, msUrnStr))

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(foliosReply)

        parsedReply.results.bindings.each { b ->
            if (b.folio) {
                folios.add( b.folio.value)
            }
        }
        return folios
    }

    /** Gets an ordered list of folio page URNs for a manuscript.
    * @param msUrnStr A Collection-level URN value for a collection
    * of folio pages, where the collection identifies a single manuscript.
    * @returns An ordered list of URN values for folio pages.
    */
    ArrayList getFoliosForMs(String msUrnStr) {
        def folioPages = []
        String foliosReply = getSparqlReply("application/json", qg.getFoliosForMs(msUrnStr))

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(foliosReply)

        parsedReply.results.bindings.each { b ->
            if (b.folio) {
                folioPages.add( b.folio.value)
            }
        }
        return folioPages

    }
    
/*
    def getChurikScore(CiteUrn urn) {
        def scoreMap = getScholiaScore(urn)
        def totalTrue = 0
        def totalFalse = 0
        scoreMap.keySet().each {
            if (scoreMap[it] == true) {
                totalTrue++;
            } else {
                totalFalse++;
            }
        }
        return [totalTrue, totalFalse, scoreMap.keySet().size()]
    }
*/


    /** Constructs a map of the number of scholia on a given folio.
    *  for each scholia document.  The key to the map is the work component
    *  the document's URN (e.g., "msA", "msAim").
    * 
    * @param urn The folio in question.
    * @throws Exception if unable to parse the document identifier
    * as a CTS URN.
    */
// not here
/*
    LinkedHashMap getScholiaPerDocForFolio(CiteUrn urn) {
        def scholiaMap = [:]
        String reply = getSparqlReply("application/json",qg.tokensPerDocOnFolioQuery(urn))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(reply)
        parsedReply.results.bindings.each { b ->
            try {
                CtsUrn doc = new CtsUrn(b.doc.value)
                scholiaMap[doc.getWork(false)] = b.tokens.value
            } catch (Exception e) {
                throw new Exception("getScholiaPerDocForFolio: bad luck, ${e}")
            }
        }
        return scholiaMap
    }
*/


    /** Ranks all Iliadic lines on this folio in one of three
    * Churik zones.
    * @param folio The folio in question.
    * @returns A map keyed by the URN of each Iliadic line,
    * with value from the ChurikZone enum.
    */

// not here
/*
    LinkedHashMap getLineRanking(CiteUrn folio) {
        def zoneMap = [:]
        String iliadReply = getSparqlReply("application/json",qg.getIliadForFolio(folio))
        def iliadSlurp = new groovy.json.JsonSlurper()
        def iliadParsed = iliadSlurp.parseText(iliadReply)
        def lineCount = 0
        iliadParsed.results.bindings.each { b ->
            lineCount++;
            if (lineCount < 7) {
                zoneMap[b.ln.value] = ChurikZone.TOP
            } else if (lineCount < 19) {
                zoneMap[b.ln.value] = ChurikZone.EXTERIOR
            } else {
                zoneMap[b.ln.value] = ChurikZone.BOTTOM
            }
        }
        return zoneMap
    }


    LinkedHashMap getScholiaScore(CiteUrn folio, String docSiglum) {
        def docScore = [:]
        def score = getScholiaScore(folio)
        score.keySet().each { k ->
            try {
                CtsUrn urn = new CtsUrn(k)
                if (urn.getWork(false) == docSiglum) {
                    docScore[k] = score[k]
                } else {
                    // IGNORE ${urn.getWork(false)} != ${docSiglum}
                }
            } catch (Exception e) {
                System.err.println "getScholiaScore: failed on key ${k}:  ${e}"
            }
        }
        return docScore
    }
*/
/*
    LinkedHashMap getScholiaScore(CiteUrn folio) {
        def iliadMap = getLineRanking(folio)
        def scholiaMap = [:]
        String reply = getSparqlReply("application/json",qg.getScholiaData(folio))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(reply)
        parsedReply.results.bindings.each { b ->
            try {
                CiteUrn scholionRoi = new CiteUrn(b.roi.value)
                def scholCoords = scholionRoi.getExtendedRef().split(/,/)
                def scholTop = scholCoords[1].toFloat()

                CiteUrn pageRoi = new CiteUrn(b.pageImg.value)
                def pgLimits = pageRoi.getExtendedRef().split(/,/)

                String iliadLn = b.iliad.value

                def zone = null
                def topY = pgLimits[1].toFloat()
                def ht =  pgLimits[3].toFloat()
                def quarter = ht / 4                 

                if (scholTop < (topY + quarter)) {
                    zone = ChurikZone.TOP
                } else if (scholTop < (topY + 2 * quarter)) {
                    zone = ChurikZone.EXTERIOR
                } else {
                    zone = ChurikZone.BOTTOM
                }
                if (iliadMap[iliadLn] == zone) {
                scholiaMap[b.scholion.value] = true
                } else {
                scholiaMap[b.scholion.value] = false
                }

            } catch (Exception e) {
                throw new Exception("getScholiaScore: failed to parse binding ${b}: ${e}")
            }
        }
        return scholiaMap
    }

*/


}

