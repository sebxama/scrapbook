![alt text](https://github.com/sebxama/scrapbook/raw/master/photo.png "Cognescent") 

[<h2>Cognescent</h2>](https://github.com/users/sebxama/projects/1)

Hello, World! I'm Sebastian Samaruga (ssamarug@gmail.com), software developer from Buenos Aires, Argentina. I'm currently mildly seasoned in the development of business applications in the Java platform and related technologies and stacks.
But what has got me scratching my head a lot in the last couple of years is the "Semantic Web". I know it is a technology paradigm far away from it's realization. But, perhaps, with the advent of Big Data and such it could finally find its niche.
Meanwhile I dump all my head scratching thoughts into this blog and a related GitHub repository I'd like to share. It is an endless work in progress draft and a scrapbook of spare assorted ideas waiting to be realized...

## Features

This is basically an outline of the features being to be provided in a working application / service. The aimed scope is to build an integration platform capable of <i>understanding</i> data, schema and behavior (data, information and knowledge in BI terms) capturing the meaning of contexts, types and instances coming from diverse sources and systems, enabling the building of an overlay layer / facade of those integrated data sources.

### Aggregation

Attribute based type inference. If a set of subjects share the same properties, for example: 'worksFor' and 'hasPosition' they will be aggregated into an 'Employee' type. FCA enabled type Aggregation.

#### Kinds

Aggregation is performed in what is called 'Kinds'. There is a Kind type for every Statement Context, Subject, Predicate and Object (CSPO). Kinds aggregate CSPO Occurrences Resources by its instances, attributes & values. For the Context Kind, instances are the Statement Context, attributes are the Statement Predicate and the values are the Statement Object. For the Subject Kind, the instances are the Statement Subject, the attributes are the Statement Predicate and the values are the Statement Object. For the Property Kind, the instances are the Statements Predicate, the attributes are the Statement Subject and the values are the Statement Object. For the Object Kind the instances are the Statement Object, the attributes are the Statement Predicate and the values are the Statement Subject.

This way we are aggregating and typing not only Subjects (Employee), but also their relationships (Predicates: Employment) and their property values (Objects: Employeer).

### Augmentation / Alignment

Attributes and basic link prediction. Alignment to upper ontologies. Ontology and Schema Matching: resolve type, instance and behavior equivalence between different sources / schemas referring to the same concepts but with a different vocabulary. Graph enabled ML inference.

#### Rules and Implications

In the beginning every CSPO Statement is an Implication (fact). By means of Context aggregation (Context Kind) and Kinds Aggregation a Rule Statement (KindStatement) can be asserted in the way:

```
(context: ContextKind, LHS: SubjectKind, Concept: PropertyKind, RHS: ObjectKind);
```

So, a Rule stating:

```
(Son, Father, BrotherOf, Uncle);
```

produces / matches the Statements:
```
(:aSon, :aSonFather, :fatherBrother, :aSonUncle);
```

Rules can be asserted / inferred or come from an Upper Ontology aligning / Ontology Matching process.

### Activation

Expose types and resources as roles and actors in a use case driven (Contexts / Interactions) fashion. Activate entities according its aggregated behaviors available in a given context. 

## Installation

### RDF4J Repository

The (Spring Boot) Application is configured (application.properties) to read it's data from an [RDF4J](https://rdf4j.org) repository. In this site downloads section you'll find a couple of web appications (WARs) needed for running the examples. The first is the RDF Workbench which allows you to create the reporitory to be configured in Spring, and to load some example data (RDF Triples). One could use [D2RQ](https://d2rq.org) to query and retrieve triples from many relational databases and populate the repository with some example data. In principle any example ontology (graph) should work.

### Application Demo

The current development achieved property based type inference and association rule mining (infer attributes by means of [FCA](https://towardsdatascience.com/a-demystifying-introduction-to-formal-context-analysis-fca-ab8ce029782e)). For running one should start the RDF Repository Server RDF4J Web Application loaded with some data as stated before and start the server (SpringBoot Maven based project). Then point your browser, according to your configuration, to one of the RESTController endpoints configured: for example: [http://localhost:8181/core/aggregation/performAggregation](http://localhost:8181/core/aggregation/performAggregation). Currently it only aggregate Subjects and it only prints debug statements to the console that depicts what's happening in the inference and aggregation processes.

## Next Steps

### DCI (Data, Context and Interactions)

What [DCI](https://discuss.neos.io/t/design-pattern-architecture-dci-data-context-interaction/5823) provides is a paradigm where Aggregated (type inference), Augmented (links / properties prediction) types and data could be behavior Activated. This should enable means to recreate the Use Cases of origin datasources by identifying types roles, contexts and data interactions. If one could be able of ordering Kinds (types), for example: Single, Married / Married, Divorced only from the data available by means of inference as one could infer types according attributes, then we could depict an API (Context) where the roles plays their interactions according to its state (Marriage, Divorce). This interactions according contexts (Use Cases) could be exposed in a DDD (Domain Driven Development) fashion API which allows to invoke 'available' contexts according backing model state (protocol).

The Interactions in Contexts may update / create new data, which should be able to be synchronized with the origin datasources. This would enable the framework to act as an 'integration' overlay between systems already deployed. If one loaded the datasources of, for example, 'Sales' and 'Invoicing' then the framework could act as an integration actor between the two, combining their use cases and enforcing their workflows.

#### REST Implementation (WIP)

In principle, everything is a Resource (RDF Paradigm) and should be network retrieveable. For Contexts arrangement and Interactions execution, type and data resources are involved and should be uniformly described to bind them to roles (types) and actors (state) playing those roles. The intention is to develop a supporting environment / facade which enables rendering of the current state in a general application consumeable manner, ideally enabling a reactive / functional 'discovery' pattern (TBD).

So, the API / Protocol should be 'discoverable' in a machine readable fashion for exposing the client agents to possible actions given a current context and interaction of roles / actors states. Possible implementation choices includes:

JAF (Java Beans Activation Framework): [Jakarta Activation](https://jakarta.ee/specifications/activation/2.1/jakarta-activation-spec-2.1)<br />
[This Article](https://www.infoworld.com/article/2077786/rest-easy-with-the-javabeans-activation-framework.html?page=1)<br />
Apache Isis (Restful Objects): [Here](https://causeway.apache.org/versions/1.14.0/guides/ugvro.html)<br />
HAL (Hypertext Application Language): [Here](https://stateless.co/hal_specification.html)<br />
Spring REST HATEOAS (Hypermedia As The Engine Of Application State) Implementation: [Docs](https://docs.spring.io/spring-hateoas/docs/current/reference/html/)<br />

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.

https://sebxama.blogspot.com
Best.

