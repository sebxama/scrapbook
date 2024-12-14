![alt text](https://github.com/sebxama/scrapbook/raw/master/photo.png "Scrapbook") 

[<h3>Scrapbook</h3>](https://github.com/users/sebxama/projects/1)

This is a first draft of a document in respect to what could be (feedback needed) a BI (Business Integration) and EAI (Enterprise Applications Integration) through Semantic Web framework / toolkit:

Integrate the domains of various applications into a unified frontend or interface. Extract all data sources from the applications to be integrated and represent them in a unified way.

Find relationships and equivalences between the data of the applications to be unified and their possible interactions. Use cases of / between applications.

Expose through an API the possible interactions to be invoked, their contexts roles and transactions interactions actors, and synchronize transaction data with the original applications.

---

TL;DR:

The idea is that by doing an "ETL" of all the tables / schemas / APIs / documents of your domain and applications, translating the sources into triples (nodes, arcs: knowledge graph) the framework can infer your entity types, relationships and the contexts, "use cases", possible between your applications generating a generic overlay (APIs, Frontend) in which to integrate in a unified, conversational and "discoverable" interface (API, web assistant, chatbot) the integrated contexts interaction in / between the source applications.

To unify and integrate diverse data sources, I transform all the information from each source into triples (Entity, Attribute, Value) and their context into a graph in the "Datasources" component. The other components deal with inference (aggregation), alignment and "activation" (exposing the description of the possible contexts and their interactions in / between the integrated applications). The last component could be a generic frontend or an API endpoint to interact according to the metadata of each context (use case).

The architecture would be microservices with five components, which for now are "black boxes", interfaces for reactive microservices to implement their algorithms with functional / streams programming.

The components are:

* Datasources Service: ETL (tabular, APIs, documents to triples knowledge graph). Populate initial graph. Synchronization with the backends of the integrated source applications according to the interactions of the contexts (Activation inference generated APIs).

* Aggregation Service: Contexts / Relationships, Types, States inference. From the "raw" data, infer types and meta-types (state) of the entities of the datasources to be integrated through their attributes and their values in a given context (relationships).

I consider entities with the same attributes as the same type, superset / subset of attributes: type hierarchy. Attributes with the same values, same states. Superset / subset of values / states: order inference.

* Alignment Service: Ontology Matching. Find equivalent contexts / types / states / entities / relationships. Missing Links / Attributes inference. Upper ontology* alignment.

* Activation Service: Use Case Types (Contexts / Roles) and Instances (Interactions / Actors) inference, APIs description metadata. DCI Design Pattern*. Possible / past interactions (transactions) for each context / actors roles.

* API Service / Generic Frontend: Generic discoverable / browseable Use Case (Contexts / Interactions) APIs from Activation Service metadata.

All services would have an administration interface for each step of the workflow with a graph-oriented backend (RDF4J or Neo4j), leveraging Graph NNs and LLMs / NLP through functional / reactive programming of the component microservices and their tasks.

Defining the "schema" of the graphs for each input/output of each component. Through functional and "reactive" programming, implementing algorithms that incrementally "parse" graphs and their respective inferences in each service so that the system is dynamic and iterative (incremental integration)

Simple example (use cases): I have fruits and vegetables, I can open a greengrocer's. I want to open a greengrocer's, I need fruits and vegetables. Actors: supplier, greengrocer, customer. Contexts / Interactions: supply, sale, etc.

Another example: I have these indicators that I inferred from the ETL, what reports can I put together? I want a report about these aspects of this topic, what indicators (roles) do I need to add.

Ultimately, it is about creating a "generator" of unified interfaces for the integration of current or legacy applications or data sources (DBs, APIs, documents, etc.) in order to expose diverse sources in an unified way, such as a web frontend (generic use case wizards), chatbots, API endpoints, etc.

The nodes and arcs of the graph triples are URIs and have a "retrievable" internal representation with metadata that each service / layer populates through the "helper" services: Registry, Naming (NLP) and Index service shared by each layer.

There is something called "Web3" that uses decentralized blockchain for the management of identifiers (URIs as DIDs: W3C Decentralized Identifiers*) and their interactions and semantics (smart contracts for example). Since the nodes and arcs of the graphs are URIs, it would not be unreasonable to use the Java APIs that are available on GitHub for this (DIDs) to facilitate the interaction of different instances or deployments of this framework between different organizations.

https://github.com/sebxama/sebxama

https://github.com/sebxama/scrapbook

* Upper ontology: https://en.wikipedia.org/wiki/Upper_ontology

* DCI: https://en.wikipedia.org/wiki/Data,_context_and_interaction

* Web3: https://en.wikipedia.org/wiki/Web3

* W3C DIDs: https://en.wikipedia.org/wiki/Decentralized_identifier


<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.

https://sebxama.blogspot.com
