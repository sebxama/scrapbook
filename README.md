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

# To Do

# Model:

(OntResource, OntResource, OntResource, OntResource);

OntResource represents aggregated / matched different identifiers / URIs referring to the same subject.

(Predicate, OntResource, OntResource, OntResource);

For a Predicate occurrence, attributes / values.

(Message, Predicate, OntResource, OntResource);

For a Message Predicate occurrence, possible attributes.

(Context, Message, Predicate, OntResource);

Occurrence (object) for a Context (interpreter) Message (sign) Predicate (concept).

(Transform, Context, Message, Predicate);

(Mapping, Transform, Context, Message);

(Template, Mapping, Transform, Context);

(Augmentation, Template, Mapping, Transform);

(Resource, Augmentation, Template, Mapping);

(Role, Resource, Augmentation, Template);

(Statement, Role, Resource, Augmentation); Augmentation of which Statement is result of.

(Model, Statement, Role, Resource);

(Entity, Model, Statement, Role); Model (Backends) aligned entities.

(Kind, Entity, Model, Statement);

(Class, Kind, Entity, Model);

(Flow, Class, Kind, Entity);

(Behavior, Flow, Class, Kind);

(Measure, Behavior, Flow, Class);

(Value, Measure, Behavior, Flow);

(Unit, Value, Measure, Behavior);

(Dimension, Unit, Value, Measure);

Example: Application (protocol) shows Dimension, select Unit / Value and assert Measure.

Pick (matched / new) corresponding Behavior. Select available / new Flow.

Pick Flow Class and assign Kind (DCI Role). Assign / create Entity (model alignment / assignation).

Follow up in occurrences hierarchy: CRUD / CUD available / possible. Perform Augmentations.

# Adapters (Connectors / Clients):

Synchronization: Functional. Monads (source / dest: domain / range). Functors (APIs: templates / event drivers for function composition / translation). Inverse functions: backend protocols / formats. Adapter endpoint resolution: activates on backends protocols / formats / data.

Adapter (Connector / Client): Model Encoding. Container (reactive message / event driven) Model APIs. To do.


Sebastian Samaruga

https://www.linkedin.com/in/sebastian-samaruga-66811b182

https://snxama.blogspot.com
