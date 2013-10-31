#mep#

Mise-en-page:  automated visual analysis of the Venetus A manuscript.

##Licenses  ##
See the separate file LICENSES.md.

## Overview ##

`mep` computes the numbers of *scholia* and word-tokens on a given page of the Venetus A manuscript, and generates two analyses of the spatial arrangement of *scholia* on the page.  The first analysis implements the theories of Maria Maniaci;  the second implements a modification of Maniaci's theories proposed by Nik Churik, and described in an article in the "Built-upon" series.  (Reference to be added.)

## Data model ##

`mep` is developed using the CITE architecture.


`mep ` depends on CTS editions of:

- the *Iliad* text of the manuscript
- the text of *scholia* 


`mep` depends on the following CITE collections:

- an ordered collection of folios
- an inventory of scholia 

`mep` depends on the following relations expressed as CITE indices:

- an index of page to default image
- an index of scholia to the default image of a page (including RoI)
- an index of the entire *Iliad* text block on page (spacing of lines is regular enough to assume each line takes an approximately equal fractional height of the block)
- an index of text

- page area of default image


## Algorithms for analyzing mise-en-page ##

Comparison of two theories

| Feature | Maniaci theory | Churik theory |
|---------|----------------|---------------|
| *Scholia* zones | 3 equal thirds of page | Top, exterior, bottom zones defined by top and bottom of *Iliad* text block |
| *Iliad* zones | 3 equal thirds of page | 6-7 lines for top and bottom, 12 for central zone (~ exterior  *scholia* zone) |

Each theory predicts that, in general, *scholia* will fall in the same zone as the *Iliad* text they comment on.

For each page, we score each scholion as correctly or incorrectly predicted by each theory.


## Overview of code ##

Detailed API docs can be built with the task `gradle groovydoc`.

- The class `MepPage` models a physical page of the manuscript.
- The class `MepGraph` represents the graph of relations that `mep` works with, and abstracts all interaction with the source of that graph in a SPARQL endpoint.
- The class `QueryGenerator` forms SPARQL query strings for the various tasks that `MepGraph` requires.

Typically therefore a `MepPage` assembles information about itself with requests for information from `MepGraph`; the `MepGraph` in turn queries its SPARQL endpoint to gather that information.

Many unit tests test for the same expected data from both a `MepGraph` and a `MepPage`.




