Author:	Nikolas Churik  
              Neel Smith  
Title:	Design and layout of the richest manuscript of the *Iliad*  


# Design and layout of the richest manuscript of the *Iliad*

## Argument and scholarly significance ##

Scholars in many disciplines have shown increasing interest in the materiality of texts.  A group of early, large-format manuscripts of the *Iliad* with scholarly notes, or *scholia*, has been especially important in the work of scholars like Maria Maniaci in developing quantitative metrics that illuminate how medieval scribes worked with their source material to design the layout of richly annotated manuscripts.  Maniaci considers how scribes planned the layout of a page to account for the number of scholia, the length of the commentary's text, and a desire to keep the commentary physically near the text commented on.

While Maniaci develops her theories by manually testing them against  a handful of manuscript pages, we have implemented her metrics in a code library, and applied them to the scores of manuscript pages from the Venetus A manuscript  in the publicly available archive of the Homer Multitext project currently covering the first seven books of the *Iliad*.  By looking systematically at a wide range of material, including sparsely annotated pages as well as pages dense with commentary, we are able to identify previously unrecognized principles guiding the physical layout of manuscript pages.  These discoveries are important for understanding the transmission of ancient scholarship, and the methods we have developed are broadly relevant for editors relating texts to the physical artifacts preserving the texts.


## Digital collections and tools we intend to use ##

Our work depends on the digital archive of the Homer Multitext project (HMT), which both authors have been intimately involved with.  (Smith is a principal architect of the project;  Churik has been a regular contributor for almost two years.)

In 2012, HMT began to develop a graph model of all relations among citable objects in the project's archive, now numbering several hundreds of thousands of arcs.  A distinctive feature of this model is that every node in the graph is identified by a [URN in the CITE architecture][citearch].   (For an introductory note about the HMT graph, see [this post][graph].)  

HMT makes a downloadable version of the complete graph available in TTL format; a network service provides an API that can return all adjacent graph nodes for a given URN either in JSON format or in an [RDF vocabulary serialized in XML][rdfvocab].

The HMT graph provides a comprehensive description of the content of HMT.  Many applications can be implemented as views on the graph by simply attaching an XSLT transformation to a query of the graph.   The classic application of a facsimile browser uniting images of a folio with diplomatic edition of the text can be implemented in this way, for example.  (Compare this [view for human readers of the graph as a facsimile browser][facsim1] with the [same source data in JSON format][jsoneg].) 

In this publication, we present tools that consult the HMT graph service, and for a specified folio page evaluate the spatial relationship of textual and non-textual features to each other and to the space of the page.


[facsim1]:	http://beta.hpcc.uh.edu/tomcat/facsimile/graph?urn=urn%3Acite%3Ahmt%3AmsA.12r&app=facsimile

[jsoneg]: http://beta.hpcc.uh.edu/tomcat/facsimile/graph?urn=urn%3Acite%3Ahmt%3AmsA.12r&format=json

## Anticipated audience

Based on preliminary presentations of our work at events such as the recent London symposium on "[Digital Approaches to Medieval Text and Image][digipal]", we anticipate that this project will be of interest not only to classicists, Byzantinists and paleographers, but to digital humanists and scholars interested in the materiality of texts more generally.

[digipal]:  http://www.digipal.eu/blogs/news/programme/


## Potential technical approaches ##
The CITE architecture originally developed for the HMT is broadly applicable to scholarship in digital humanities.  In the CITE architecture, all entities are identified with URNs.   Relations among any pair of URNs can be represented with RDF triplets.  

This provides a simple but very powerful framework for identification and retrieval of citable scholarly information.  Every textual and graphic feature recorded in the HMT has a unique URN, and is related to visual evidence that is also expressed with URN values.  Beyond  referring to images with unique identifiers, the CITE conventions also allow reference to scale-independent regions of interest, so we can automatically resolve object identifiers to spatial locations or vice versa.  (See, for example, an [automatically generated visual inventory of scholia for a given folio][visinv].)  This means that it is straightforward to compute the relations of every recorded observation within the space of an image  (and hence to automate Maniaci's metrics.) 

[visinv]: http://folio.furman.edu/cfc/imgmap?coll=urn%3Acite%3Ahmt%3Ascholinv.inv11&img=urn%3Acite%3Ahmt%3Achsimg.VA012RN-0013&map=ScholionURN&imgProp=VisualEvidence


We are especially interested in presenting our research in the Built-Upon series because we believe Built-Upon offers unique opportunities to explore how to incorporate references to structured citation of evidence within a scholarly narrative.  We have begun experimenting with incorporating semantically rich URN references in narrative argument expressed in a lightweight markdown format, as a source for generating both highly interactive digital presentations and static print-quality formats.


## Conclusions ##

By applying automated analysis of the spatial layout of scholia in the oldest complete manuscript of the *Iliad*, the Venetus A, we show that placement of commentary on the page is not simply a compromise between quantity of material and proximity to the text commented on, but  follows an organizational scheme negotiated between scribe and reader.  Commentary on the top quarter of the *Ilad* text (typically 6 lines, since most folios have 24 or 25 lines) is placed in an upper register, the last quarter in a lower register, and the middle half (typically lines 7-18 of the *Iliad* text) in the column at the exterior of the page edge.  These rules apply even when the result is that a note is *further removed* from the text it comments on. The layout of a uniquely rich manuscript like the Venetus A therefore is not simply a problem of how to fit large quantities of material into a compressed space, but how to compose a page in accordance with the cognitive habits and expectations of the reader.  This principle could not have been identified without a systematic and automated analysis of the kind made possible here by the HMT archive.  

Our project thus gives us a new insight into how medieval scribes worked.  It also offers a fuller understanding of how digital archives can make possible systematic study of spatial placement of features on folio pages, and an opportunity to explore how scholarly narrative can be built upon citable entities identified with the URNs of the CITE architecture.





Nikolas Churik

Neel Smith


[rdfvocab]: http://www.homermultitext.org/hmt-doc/standards/rdfvocabulary.html

[graph]: http://homermultitext.blogspot.com/2012/10/from-graphs-to-applications.html

[facsim]: http://beta.hpcc.uh.edu/tomcat/facsimile/

[citearch]: http://www.homermultitext.org/hmt-doc/cite/index.html
