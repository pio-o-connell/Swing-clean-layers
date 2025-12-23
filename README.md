# Concordia

<img src="documentation/images/java-swing-widget.jpg" alt="Widget" style="width:60%;">



## Overview
---

# concordia

**Project Overview:**  
"Concordia" takes an old  **Java Swing** created in 2014, and uses **augmented AI**, to create a **swing front end locally and servlet for remote access from common code base**.  

**Project Purpose:**  
- **Reorganize a legacy Concordia app into clean layers**<br><br>
        WareHouse<br>
        ├── domain        (pure data + rules)<br>
        ├── repository    (JDBC / persistence)<br>
        ├── service       (business logic)<br>
        ├── controller    (Concordia + Servlet adapters)<br>
        ├── ui            (Concordia components only)<br>
        ├── infrastructure (bootstrapping, wiring)<br>

- **Infrastructure** created within app negating any need for Spring or DI      
- **Annotations** as labels ( semantic markers) 
- **Remote Acccess** using **servlet**, **Local Access** application building on **Java Concordia libraries**
- Integration of **augmented AI tools** to enhance development speed and functionality.  
- Fully **responsive design** for **Servlet** with attention to accessibility and user experience.  

**Live Project:** [Harmonia](#)  



<h2 align="center" id="TOC">Index</h2>

- [Concordia](#concordia)
  - [Overview](#overview)
- [concordia](#concordia)
  - [Phases-one](#phases-one)
    - [Domain first](#domain-first)
  - [Phases-two](#phases-two)
    - [Repository extraction](#repository-extraction)
    - [Move database code without changing SQL](#move-database-code-without-changing-sql)
  - [Phases-three](#phases-three)
    - [Introduce Services (thin at first)](#introduce-services-thin-at-first)
  - [Phases-four](#phases-four)
    - [Controller layer (adapters)](#controller-layer-adapters)
  - [Phases-five](#phases-five)
    - [Clean the Concordia UI (liberation phase)](#clean-the-concordia-ui-liberation-phase)
  - [Phases-six](#phases-six)
    - [Infrastructure / Wiring](#infrastructure--wiring)
  - [Phases-seven](#phases-seven)
    - [Remove legacy shortcuts](#remove-legacy-shortcuts)
    - [Annotations](#annotations)
  

## Phases-one
---

### Domain first


<details>
    <summary>Details
    </summary>
    Phase 1 – Domain first 
        1️⃣ Move domain classes
            WareHouse.domain

        No logic changes
        Next, start with the domain layer. This is the safest step. Move your core model classes — Company, Item, history, User, Index — into a domain package. Do not add logic, do not add database code, do not add UI code. These classes should only represent data and relationships. Serialization is fine here.
</details>
<br>

[Back To Top](#concordia)

## Phases-two
---

### Repository extraction 


<details>
    <summary>Details
    </summary>
        2️⃣ Create repository package
            WareHouse.repository
</details>


### Move database code without changing SQL

<details>
    <summary>Details
    </summary>
        3️⃣ Move database code without changing SQL

        | Old class         | New repository                                             |
        | ----------------- | ---------------------------------------------------------- |
        | `Databases`       | `CompanyRepository`, `ItemRepository`, `HistoryRepository` |
        | `DatabaseBackup`  | `BackupRepository`                                         |
        | `DatabaseRestore` | `RestoreRepository`                                        |
        | `Serialized`      | `FileBackupRepository`                                     |
        Rules:

            One repository = one persistence concern

            JDBC only here

            Repositories return domain objects

            Add labels:
            @Repository
            public class ItemRepository { ... }

        No behaviour change yet.

        After that, extract the repository layer. This is where most of your current code actually belongs. All JDBC and file persistence logic moves here. Your existing DatabaseBackup, DatabaseRestore, Serialized, and similar classes become repositories. Each repository should focus on one persistence responsibility. At this stage, you are still not improving logic — only relocating it. These classes can be labelled with @Repository as a semantic marker, even without Spring.

</details>

<br>

[Back To Top](#swing---clean---layers)

## Phases-three
---

### Introduce Services (thin at first)
    

<details>
    <summary>Details
    </summary>
       4️⃣ Create service package
        WareHouse.service

        Create thin services:

        InventoryService

        BackupService

        @Service
        public class BackupService {
            private final BackupRepository repo;

            public void backup(List<Company> companies) {
                repo.backup(companies);
            }
        }

        At this stage:

        Services mostly delegate

            That’s OK
        RestoreService
        Then introduce the service layer. Services sit above repositories and represent business use cases such as inventory management, backups, restores, or reporting. Initially, services can be very thin and simply delegate to repositories. That is expected and correct. Label them with @Service to make intent clear.

</details>

<br>

[Back To Top](#swing---clean---layers)







## Phases-four
---

###  Controller layer (adapters)


<details>
    <summary>Details
    </summary>
        Phase 4 – Controller layer (adapters)
        5️⃣ Create controller package
            WareHouse.controller

            Purpose:

            Translate UI intent into service calls

            Controllers:

            InventoryController

            BackupController

            RestoreController

            Rules:

            Concordia talks only to controllers

            Controllers talk to services

            No JDBC

            No UI widgets in services


            @Component
            public class BackupController {
                private final BackupService service;

                public void backupPressed() {
                    service.backup();
                }
            }



        Next, add a controller layer. Controllers are the bridge between the Concordia UI (or servlets) and services. The UI should no longer call repositories or services directly. Controllers translate user actions like “Backup pressed” or “Delete item” into service calls. These are application controllers, not web controllers. Label them with @Component.
</details>
<br>

[Back To Top](#concordia)




## Phases-five
---

### Clean the Concordia UI (liberation phase)

<details>
    <summary>Details
    </summary>
        6️⃣ Simplify Concordia classes

        Now the UI becomes dumb (in a good way).

        Concordia does ONLY:

        Read text fields

        Show dialogs

        Call controller methods

        ❌ No SQL
        ❌ No business logic
        ❌ No static globals

        This is where Concordia suddenly becomes pleasant again.

        After that, clean up the Concordia UI. The UI should now only read user input, show dialogs, and call controller methods. There should be no SQL, no business logic, and no persistence code in Concordia classes. This is where Concordia becomes simple and manageable again.


</details>
<br>

[Back To Top](#concordia)



## Phases-six
---


### Infrastructure / Wiring




<details>
    <summary>Details
    </summary>

        WareHouse.infrastructure.ApplicationBootstrap sets up the database connection, repositories, service, controller, and launches the Mainframe. 
        This centralizes all object creation and wiring, replacing the need for Spring or any DI framework. You can now launch your app from ApplicationBootstrap. #

</details>
<br>

[Back To Top](#swing---clean---layers)



## Phases-seven
---

### Remove legacy shortcuts

<details>
    <summary>Details
    </summary>
    Only now:

        Remove static globals

        Remove maindriver.Company

        Remove hidden dependencies

        Remove circular references

        At this point:
        ✔ Testable
        ✔ Understandable
        ✔ Framework-ready (if you ever want Spring)






        Only at the end should you remove legacy shortcuts like static globals, hidden dependencies, and circular references. Do this last, when the structure is stable.


Phase 7 – Kill legacy shortcuts



Only now:

 fully remove the old main or further refactor!

Remove static globals

Remove maindriver.Company

Remove hidden dependencies

Remove circular references

At this point:
✔ Testable
✔ Understandable
✔ Framework-ready include 






Only at the end should you remove legacy shortcuts like static globals, hidden dependencies, and circular references. Do this last, when the structure is stable.


Annotations are labels, not magic:

@Repository

@Service

@Component






</details>
<br>

[Back To Top](#swing---clean---layers)


### Annotations 
---

<details>
    <summary>Details
    </summary>
    Semantic annotations are defined only as semantic markers- they do not carry meaning for humans (and future tools), but and have no runtime behaviour.

    They:
        do not create objects
        do not inject dependencies
        do not manage transactions
        do not change execution

    In this project @Service, @Repository, @Component, @Controller and @Transactional are used 
    They exist to say:
        “This class plays this role in the architecture.”

</details>
<br>

[Back To Top](#swing---clean---layers)






















