# Replicating the Mise en Page project #

## Prerequisites ##


- [gradle][gradle] (the project's build system)
- a SPARQL end point with the data Mise en Page analyzes.  This can be satisfied either with:
    -  internet access, so you can use the Homer Multitext project's SPARQL endpoint (default)
    -  a SPARQL endpoint with the Homer Multitext project's RDF data installed, accessible locally or over the internet

Optionally, if you have access to your own servlet container, you can install Mise en Page there.

[gradle]: http://www.gradle.org/

[git]: https://github.com/neelsmith/mep


## Quick install ##

- clone or fork the [project git repository][git]

To run locally:

    gradle jettyRunWar


To build a war file to install in your own servlet container:

    gradle war



##Configuration options

TBA