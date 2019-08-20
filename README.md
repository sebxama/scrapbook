# Hello, World!

I'm Sebastian Samaruga, software developer from Buenos Aires, Argentina. I'm currently mildly seasoned in the development of business applications in the Java platform and related technologies and stacks.
But what has got me scratching my head a lot in the last couple of years is the "Semantic Web". I know it is a technology paradigm far away from it's realization. But, perhaps, with the advent of Big Data and such it could finally find its niche.
Meanwhile I dump all my head scratching thoughts into this blog and a related GitHub repository I'd like to share. It is an endless work in progress draft and a scrapbook of spare assorted ideas waiting to be realized...

Note: Going through my most recent attempts of having something concrete for sharing in plain English I realize one mistake I'm committing: I'm trying to describe combustion vehicles (Hypermedia Applications) saying that petroleum exists (Semantic Intelligence).

As long as my post are going I've just got a stack of (incoherent) "analysis" documents as the result of my work. And I had only those until now because I was stuck because of the previously mentioned mistake (ah, and because of my Bipolar Disease maniac episodes...).

I should try to describe applications instead and see how and where fuel should burn properly inside a motion vehicle to generate traction. Every semicolon I write is updated into my GitHub repository, so, sorry if you browse that "scrapbook" and you don't find anything even intelligible.

Project description:

# Semantics Augmented ESB / EAI

The idea of the project is to "augment" an ESB for EAI platform and to enable it allowing it to make "inferences" regarding which routes to use, "discovering" sources / destinations of an event message(s) which then it transforms / enriches according destination "semantics" and format(s).

This featuring the exposure of a generic facade which allows to see in an "homologated" view the applications or services and their data, schema and behavior (actions) that could be integrated into the tool.

Different integrated applications are enriched with this facade and with the events that, given the inferred routes and transformations, augments theirs data, schema and behaviors, invoking activities corresponding to each destiny semantics.

# Sample Use Case: Goals App

Goals App: purpose / goals / domain driven syndication of  integrated business / social / cloud application features. User / Groups / Roles Purpose(s), Goal(s), Task(s) "intelligent" tracking oriented focus providing an abstraction and integration layer of players process flows / interactions and players process assets management and semantic orchestration.

Goals App: Semantically annotated gestures / interactions (contexts, purposes messages / interactions / resources / content). Subject context occurrence role attributes values (metaclass, class, instance, occurrences).

Goals App: API Facade for rendering aggregated data roles in contexts interactions topics / subjects assets (conceptual domain contexts axis / state views / activations: Forms / Flows). Example: domain declared Customer (actor / role), Product, Order, Purchase, Invoice, etc. topics / subjects assets rendered in contexts (Sales Report, Expenses Report, etc. embedded / linked dashboards). Wizards.

Goals App: Browse / search / activate: history / relations / referrer context / interaction / gestures roles traceability / (dialogs). Gestures / interactions (actor / asset, actor / actor). Wizards.

Goals App: Hypermedia contents APIs (embedded / embeddable resources: Semantic contextual Wiki / Apache Stanbol / CMS: hypermedia augmentation, knowledge / behavior maps). Integration: augmentation / sync backends / apps. Extension: services / APIs. Annotate / augment link content. DAV protocol (integration / extension facades).

Low level Resource / Message / Context model / layers API. REST. Render DOM Context / OGM Domain (model) instances: Restful Objects / Apache Isis / HAL / GraphQL (meta / domains models endpoints) like APIs. Forms / Flows MVC / DCI APIs (connectors / clients / adapters).

# Adapter (Connector / Client):

Synchronization: Functional. Monads (source / dest: domain / range). Functors (APIs: templates / event drivers for function composition / translation). Inverse functions: backend IO protocols / formats. Adapter endpoint resolution: activates on backends protocols / formats / data.

Adapter (Connector / Client): Model Encoding. Container (reactive message / event driven) APIs. Model / Container APIs interactions.

Adapter / Model "statements" IO abstraction (Forms / Flows: Message events attribute / values).

Message (events): bidirectional CRUD streams (Adapter "template" methods, Model Message declarations):

Adapter: Context layer (semiotic interpreter).

onCreate;

onRetrieve;

onUpdate;

onDelete;

# To Do

Rendering: S: current document URL, P: link tag body, O: href, rel: Context (referrer). Navigation: GET / headers. GET (navigate, possible resources, posible contexts / subjects / attributes): CRUD / Contexts aggregation / transforms / matching. Encoding: CRUD / browse layers (CSPO Patterns Forms / Flows layers de-aggregations / faceted traversals).

Encoding:

Encoding, APIs: REST HATEOAS, JSON-LD, HAL. Distributed (normalized) address ID spaces.

TBD:

Layers down / up traversal:

C: Anchor rel (referrer);
S: Current URL;
P: Anchor tag body;
O: Anchor href;

Layers up / down traversal:

C: Current URL;
S: Anchor rel / referrer;
P: Anchor tag body;
O: Anchor href;

Functors functional declaration:
(((a: O, b: S), c: P), d: C);

(((O, S), P), C): Referring Context. Augmentations functors signature. Traversal performs functor augmentations "backwards" traversal direction concatenating type, role, interactions transforms incrementally.

Encoding (example): recursive CSPO IDs: (metaclass, class, instance, occurrence) IDs. URLs: domain/CID:SID:PID:OID. Graph URLs / rels traversal.

Addressing: URLs encode complete (possible) state flows: reified model state URLs (faceted browse / CRUD).

REST HATEOAS: Link rel (account): deposit, whitdraw, etc. Flow Behavior "referrer" rel.

Resource entity 'whitdrawal': context interaction. Actions 'possible'. Behavior Flow "referrer" rel.

Resource Monad: encode protocol functors. Endpoint address activation behavior facades. Graph state / rels traversal: Monad encodes entire state location flows to current CSPO URL IDs state (traceability in interaction context rels). Abstract Form / Flow attrs / rels.

Functors resolution on API addresses URLs: resource monads rels / attrs activation.

Model / Domain levels of common model / domain monads, functors: model / domain abstract augmentations / behaviors. Declared in model contexts messages / augmentation instances.

Behavior layer renders domains possible aggregated augmentations / messages of model functors composition. Rendered in domain levels as concrete contexts operations: named context operations over abstract model functors behaviors.

Functors:

Functors: model layers aggregations declarations / instances. Type, Role, Alignment levels. Domain / range: CSPO contexts (Template, Transform signatures). Transform: Mapping Message. Hypermedia events dataflows triggered functors (signature bindings).

Message: Functor Declaration. (events / grammar: protocols).

Augmentation: Functor Instance.

Functors functional declaration:
(((a: O, b: S), c: P), d: C);

(dado rango y alcance, universo: U de una relación: P, inferir dominio y codominio, campo: C). TBD.

Type functor: contexts stream.
(((Mapping, Augmentation), Template), Resource);
Context layer class / instances.

Role functor: type contexts occurrences stream.
(((Template, Resource), Augmentation), Role);
Type Context layer class occurrence (Subject) in aggregated context layers.

Alignment functor: type occurrence attributes / values in contexts interactions stream.
(((Augmentation, Role), Resource), Statement);
Type Subject occurrence attributes / values (statements / augmentation "kinds").

Behavior flows functor composition: Behavior, Flow, Class, Kind, Entity layers aggregation. Determine type, role, alignment augmentations. Example: type (Class Model) in context (Flow Entity) in interaction (Behavior Kind).

Encoding:

Encoding: metaclass, class, instance, occurrence (contextual / nested / orders / ops) CSPO IDs. CURIEs.

Encoding: Sets CSPO Contexts specification (sets quad encoding).

Encoding: Functor application. Predicate: functor behavior, domain: statement predicate, transform / range: statement object.

Encoding: Levels (OntResource context hierarchy) reification: Message as Predicate, etc. Resource Monad (context statement / signatures). Functor aggregation: levels (type, role, alignment).

Encoding: Grammars. OntResource hierarchy reification: rules (contexts) / non terminals (reified Predicates / Kinds). Aligned OntResource URLs: terminals. Augmentations: productions (functors).

# Model:

Model layers:

OntResource: Resolves reified aligned / matched aggregated Resources.

Predicate: 'kind', aggregates roles attributes / values. Grammar.

(OntResource, OntResource, OntResource, OntResource);

(Predicate, OntResource, OntResource, OntResource); For a Predicate occurrence, attributes / values.

(Message, Predicate, OntResource, OntResource); For a Message Predicate occurrence, possible attributes / values. Functor declaration.

(Context, Message, Predicate, OntResource); Occurrence (object) for a Context (interpreter) Message (sign) Predicate (concept). Adapter.

(Transform, Context, Message, Predicate);

(Mapping, Transform, Context, Message);

(Template, Mapping, Transform, Context);

(Augmentation, Template, Mapping, Transform);

(Resource, Augmentation, Template, Mapping); Type Functor Augmentation instance.

(Role, Resource, Augmentation, Template); Role Functor Augmentation instance.

(Statement, Role, Resource, Augmentation); Augmentation of which Statement is result of. Alignment Functor Augmentation instance.

(Model, Statement, Role, Resource);

(Entity, Model, Statement, Role); Model (Backends) aligned entities.

(Kind, Entity, Model, Statement);

(Class, Kind, Entity, Model);

(Flow, Class, Kind, Entity);

(Behavior, Flow, Class, Kind); Statement, proposition.

(Value, Behavior, Flow, Class); Value on which Behavior occurrence holds.

(Unit, Value, Behavior, Flow);

(Dimension, Unit, Value, Behavior);

(Measure, Dimension, Unit, Value); Truth values. Equivalent Measure(s), comparisons (order / hierarchies). Measure Dimension attributes / values.


Sebastian Samaruga
https://snxama.blogspot.com
