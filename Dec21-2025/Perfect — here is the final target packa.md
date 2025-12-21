Perfect — here is the final target package sketch, expressed conceptually and in plain language, exactly matching what you’re doing (Swing + Servlet, no Spring, annotations as markers only).

1. warehouse.domain

What it is:
Pure business model. No UI, no SQL, no Swing, no Servlets.

Contains:

Company

Item

history

User

Index

Value objects (dates, IDs if you add them later)

Rules:

May be Serializable

May have simple validation

Must not know how data is stored or displayed

2. warehouse.repository

What it is:
Persistence boundary. Everything that touches JDBC, files, or serialization lives here.

Contains:

DatabaseBackup → backup repository

DatabaseRestore → restore repository

Serialized → file/serialization repository

Databases (your loader / initializer)

JDBC helpers, connection handling

Annotations (semantic only):

@Repository

Rules:

Talks to DB, filesystem

Returns / accepts domain objects only

No Swing, no UI logic

3. warehouse.service

What it is:
Business use cases. Orchestrates repositories.

Contains (examples):

InventoryService

BackupService

RestoreService

ReportService

Example responsibilities:

“Backup warehouse”

“Restore warehouse”

“Generate report between dates”

Annotations:

@Service

@Transactional (document intent, even if manual)

Rules:

No UI code

No SQL

Coordinates multiple repositories

4. warehouse.controller

What it is:
Application controllers. Not web-specific.

Contains:

InventoryController

BackupController

ReportController

UserController

Responsibilities:

Accept requests from Swing or Servlets

Call service methods

Return results or throw application exceptions

Annotations:

@Component

5. warehouse.ui.swing

What it is:
Desktop UI only.

Contains:

Mainframe

Dialogs

Tables

Buttons, listeners

Rules:

Knows controllers only

No SQL

No business logic

No repositories

6. warehouse.ui.servlet

What it is:
Web entry points.

Contains:

Servlets

Filters

Request/response mapping

Rules:

Calls controllers

Converts HTTP → controller calls

No JDBC, no business logic

7. warehouse.infrastructure

What it is:
Manual wiring and bootstrapping (your “DIY Spring”).

Contains:

ApplicationBootstrap / Main

Connection factory

Object graph creation

Responsibilities:

Create Connection

Create repositories

Create services

Create controllers

Inject into UI

8. warehouse.annotations

What it is:
Semantic markers only.

Contains:

@Repository

@Service

@Component

@Transactional

Rules:

No behaviour

Pure documentation + reflection hooks (future)

Mental Model (this is key)

UI → Controller → Service → Repository → DB / File
Domain flows through all layers, but owns none of them.

Swing and Servlets become just clients of the same application core.

What this architecture is called

Layered Architecture

Clean Architecture (lite)

Hexagonal-adjacent (without adapters yet)

Legacy Swing Application Modernisation

And crucially:

Framework-independent enterprise design

If you want, next we can:

map each of your existing classes into this structure explicitly

design the bootstrap wiring

or refactor one concrete class live (e.g. DatabaseBackup → BackupRepository → BackupService)

You’re doing this exactly the right way.


map each of your existing classes into this structure explicitly

design the bootstrap wiring