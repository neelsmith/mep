package edu.holycross.shot.mep


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn


/** A class using knowledge of the HMT schema to generate appropriate
* SPARQL queries for analyzing visual layout of manuscript.
*/
class QueryGenerator {

    /** Empty constructor.*/
    QueryGenerator() {
    }

    String pageBlock(String folioUrnStr) {
        return """
select ?folio ?img ?imgroi ?pageblock WHERE {
?img <http://www.homermultitext.org/cite/rdf/illustrates> ?folio .
?img <http://www.homermultitext.org/cite/rdf/hasExtendedRef> ?imgroi .
?imgroi <http://www.homermultitext.org/cite/rdf/illustrates> ?pageblock .

FILTER (str(?folio) = "${folioUrnStr}")
FILTER (regex(str(?pageblock),"urn:cite:hmt:pageroi"))
}
"""
    }

    String orderedScholia(String folioUrnStr)  {

return """
prefix cts:	<http://www.homermultitext.org/cts/rdf/> 
prefix cite:	<http://www.homermultitext.org/cite/rdf/> 

SELECT ?folio ?doc ?schol  ?scholSeq WHERE {
?schol cite:appearsOn ?folio .

?schol cts:hasSequence ?scholSeq .

?schol <http://www.homermultitext.org/cts/rdf/belongsTo>    ?doc . 


FILTER (str(?folio) = "${folioUrnStr}") .
FILTER (regex(str(?schol), "urn:cts:greekLit:tlg5026") ) .

}

order by ?doc ?scholSeq 
"""
    }



    String orderedIliadLines(String folioUrnStr)  {

return """
prefix cts:	<http://www.homermultitext.org/cts/rdf/> 
prefix cite:	<http://www.homermultitext.org/cite/rdf/> 

SELECT ?folio  ?il  ?ilSeq WHERE {
?il cite:appearsOn ?folio .

?il cts:hasSequence ?ilSeq .

FILTER (str(?folio) = "${folioUrnStr}") .
FILTER (regex(str(?il), "urn:cts:greekLit:tlg0012") ) .

}

order by ?ilSeq 
"""
    }

    String magicQueryForFolio(String folioUrnStr) {
return """
prefix cts:	<http://www.homermultitext.org/cts/rdf/> 
prefix cite:	<http://www.homermultitext.org/cite/rdf/> 
prefix hmt:	<http://www.homermultitext.org/hmt/rdf/> 
prefix citedata:	<http://www.homermultitext.org/hmt/citedata/> 
prefix dcterms:	<http://purl.org/dc/terms/> 
prefix rdf:	<http://www.w3.org/1999/02/22-rdf-syntax-ns#>
prefix  xsd:	<http://www.w3.org/2001/XMLSchema#> 
prefix lex:	<http://data.perseus.org/rdfverbs/> 


SELECT ?pageroi ?schol ?scholroi ?token ?il  WHERE {
?folio ?v ?schol .
?schol cts:hasTextContent ?schtxt .
?pg  citedata:pageroi_ImageRoI ?pageroi .
?pg citedata:pageroi_Folio ?folio  .
?schol hmt:commentsOn ?il .
?il cts:hasSequence ?ilSeq .
?token hmt:psg ?schol .
?schol cite:illustratedBy ?scholroi .

FILTER (str(?folio) = "${folioUrnStr}") .
FILTER (regex(str(?schol), "urn:cts:greekLit:tlg5026") ) .

}
"""
    }

    // get folios for book : orderd series of urns
    // get folios for range : ordered series of urns
    // getIliadForFolio:  ordered series of lines
    // get scholiaRoIs for folio
    // get scholia tokens for folio (a density measure)


    String getFoliosForBook(CtsUrn bookUrn) {
        return """
        ${MepDefinitions.prefixPhrase}
        SELECT ?folio  WHERE {
         ?folio hmt:seq ?s .
 
         {  SELECT ?seq1 
         (ROUND (?seq1) as ?min)
         WHERE {
           ?f1 hmt:seq ?seq1 .
           FILTER (str(?f1) = "${firstFolio}" ) .
         }
        } .

        {  SELECT ?seq2 
        (ROUND (?seq2) as ?max)
        WHERE {
          ?f2 hmt:seq ?seq2 .
          FILTER (str(?f2) = "${lastFolio}" ) .
        }
        } .

        FILTER (?s >= ?min) .
        FILTER (?s <= ?max) .
        FILTER ( regex(str(?folio),"${msUrnStr}" )) . 
       }
       ORDER BY ?s 
       """

    }    


    String getIliadForFolio(CiteUrn folio) {
        return """
        ${MepDefinitions.prefixPhrase}

       SELECT ?ln 
        WHERE {
          ?ln hmt:appearsOn ?f .
          ?ln hmt:seq ?seq .

          FILTER (str(?f) = "${folio}") .
          FILTER (regex(str(?ln), "urn:cts:greekLit:tlg0012." ) ) .
        }
        ORDER BY ?seq
        """
    }

    String getScholiaData(CiteUrn folio) {
        return """
        ${MepDefinitions.prefixPhrase}
        SELECT ?scholion ?roi ?pageImg ?iliad
        WHERE {
         ?f hmt:hasOnIt ?scholion .
         ?f mep:pageRoi ?pageImg .
         ?scholion hmt:illustratedBy ?roi .
         ?scholion hmt:commentsOn ?iliad .
         FILTER (str(?f) = "${folio}") .
        }
        """
    }




    String getScholiaWithCoords(CiteUrn folio) {
        return """
        ${MepDefinitions.prefixPhrase}
        SELECT ?scholion ?roi ?pageImg
        WHERE {
         ?f hmt:hasOnIt ?scholion .
         ?f mep:pageRoi ?pageImg .
         ?scholion hmt:illustratedBy ?roi .
         FILTER (str(?f) = "${folio}") .
        }
        """
    }


    // inclusive range retrieval
    String getFolioRange(CiteUrn firstFolio, CiteUrn lastFolio, String msUrnStr) {
        return """
        ${MepDefinitions.prefixPhrase}
        SELECT ?folio  WHERE {
         ?folio hmt:seq ?s .
 
         {  SELECT ?seq1 
         (ROUND (?seq1) as ?min)
         WHERE {
           ?f1 hmt:seq ?seq1 .
           FILTER (str(?f1) = "${firstFolio}" ) .
         }
        } .

        {  SELECT ?seq2 
        (ROUND (?seq2) as ?max)
        WHERE {
          ?f2 hmt:seq ?seq2 .
          FILTER (str(?f2) = "${lastFolio}" ) .
        }
        } .

        FILTER (?s >= ?min) .
        FILTER (?s <= ?max) .
        FILTER ( regex(str(?folio),"${msUrnStr}" )) . 
       }
       ORDER BY ?s 
       """
       }



    /** Constructs a query to find an ordered sequence
    * of folio page URNs matching a Collection-level
    * CITE URN identifying a manuscript's collection 
    * of folio pages.
    * @param msUrnStr Collection-level CITE URN identifying a MS.
    * @returns SPARQL query to find ordered list of folio page  URNs.
    */
    String getFoliosForMs(String msUrnStr) {
        return """
         ${MepDefinitions.prefixPhrase}
         SELECT ?folio
         WHERE {
           ?folio hmt:seq ?seq .
           FILTER (regex(str(?folio), "${msUrnStr}" )) .
          }
          ORDER BY ?seq 
          """
     }

    String tokensOnFolioQuery(CiteUrn urn) {
        return """
        ${MepDefinitions.prefixPhrase}
        SELECT ?token
        WHERE {
          ?f hmt:hasOnIt ?psg .
          ?subref hmt:psg ?psg .
          ?token lex:occursIn ?subref .
          FILTER (str(?f) = "${urn}")
         }
         """
    }


    String tokensPerDocOnFolioQuery(CiteUrn urn) {
        return """
        ${MepDefinitions.prefixPhrase}

        SELECT ?doc (COUNT (?token) AS ?tokens)
        WHERE {
          ?f hmt:hasOnIt ?psg .
          ?subref hmt:psg ?psg .
          ?token lex:occursIn ?subref .
          ?psg hmt:belongsTo ?doc .
          FILTER (str(?f) = "${urn}")
         }
         GROUP BY ?doc
         """
    }

    String scholiaDocs() {
      return """
      ${MepDefinitions.prefixPhrase}
      SELECT DISTINCT ?doc WHERE {
         ?psg hmt:belongsTo ?doc .
         FILTER (regex(str(?doc), "urn:cts:greekLit:tlg5026" ) ) .
      }
      """
     }


    String scholiaPerDocOnFolioQuery(CiteUrn urn) {
        return """
        ${MepDefinitions.prefixPhrase}
       
        SELECT ?doc (COUNT (?psg) AS ?scholia)
        WHERE {
          ?f hmt:hasOnIt ?psg .
    
          ?psg hmt:belongsTo ?doc .
          FILTER (str(?f) = "${urn}")
         }
         GROUP BY ?doc
         """
    }



    String scholiaOnFolioWithBoundsQuery(CiteUrn urn) {
        return """
         ${MepDefinitions.prefixPhrase}
        SELECT ?scholion 
        WHERE {
          ?f hmt:hasOnIt ?scholion .
          FILTER (str(?f) = "${urn}") .
          FILTER (regex(str(?scholion), "urn:cts:greekLit:tlg5026." ) ) .
        }
        """
    }


    /*
    * should this query be ordered by document sequence?
    */
    String scholiaOnFolioQuery(CiteUrn urn) {
        return """
         ${MepDefinitions.prefixPhrase}
        SELECT ?scholion 
        WHERE {
          ?f hmt:hasOnIt ?scholion .
          FILTER (str(?f) = "${urn}") .
          FILTER (regex(str(?scholion), "urn:cts:greekLit:tlg5026." ) ) .
        }
        """
    }


    String defaultImageQuery(CiteUrn urn) {
        return """
         ${MepDefinitions.prefixPhrase}
         SELECT ?img
         WHERE {
         ?folio hmt:hasDefaultImage ?img .
         FILTER (str(?folio) = "${urn}") .
         }
        """
    }
}
