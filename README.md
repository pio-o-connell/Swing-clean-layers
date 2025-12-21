# Swing - Clean - Layers

<img src="documentation/images/java-swing-widget.jpg" alt="Widget" style="width:60%;">



## Overview
---

# swing-clean-layers

**Project Overview:**  
"Swing Clean Layers"  takes an old  **Java Swing** of mine, and with **augmented AI**, to create a **swing front end locally and servlet for remote access**.  

**Project Purpose:**  
- **Reorganize a legacy Swing app into clean layers**
- **Target layers**<br>
        WareHouse<br>
        ├── domain        (pure data + rules)<br>
        ├── repository    (JDBC / persistence)<br>
        ├── service       (business logic)<br>
        ├── controller    (Swing + Servlet adapters)<br>
        ├── ui            (Swing components only)<br>
        ├── infrastructure (bootstrapping, wiring)<br>
        
- **Annotations** as labels, **@Repository @Service @Component**
- **Remote Acccess** using **servlet**
- Integration of **augmented AI tools** to enhance development speed and functionality.  
- Fully **responsive design** for **Servlet** with attention to accessibility and user experience.  

**Live Project:** [Harmonia](#)  



<h2 align="center" id="TOC">Index</h2>

- [Swing - Clean - Layers](#swing---clean---layers)
  - [Overview](#overview)
- [swing-clean-layers](#swing-clean-layers)
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
    - [Clean the Swing UI (liberation phase)](#clean-the-swing-ui-liberation-phase)
  - [Phases-six](#phases-six)
    - [Infrastructure / Wiring](#infrastructure--wiring)
  - [Phases-seven](#phases-seven)
    - [Remove legacy shortcuts](#remove-legacy-shortcuts)

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

[Back To Top](#swing---clean---layers)

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

            Swing talks only to controllers

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



        Next, add a controller layer. Controllers are the bridge between the Swing UI (or servlets) and services. The UI should no longer call repositories or services directly. Controllers translate user actions like “Backup pressed” or “Delete item” into service calls. These are application controllers, not web controllers. Label them with @Component.
</details>
<br>

[Back To Top](#swing---clean---layers)




## Phases-five
---

### Clean the Swing UI (liberation phase)

<details>
    <summary>Details
    </summary>
        6️⃣ Simplify Swing classes

        Now the UI becomes dumb (in a good way).

        Swing does ONLY:

        Read text fields

        Show dialogs

        Call controller methods

        ❌ No SQL
        ❌ No business logic
        ❌ No static globals

        This is where Swing suddenly becomes pleasant again.

        After that, clean up the Swing UI. The UI should now only read user input, show dialogs, and call controller methods. There should be no SQL, no business logic, and no persistence code in Swing classes. This is where Swing becomes simple and manageable again.


</details>
<br>

[Back To Top](#swing---clean---layers)



## Phases-six
---


### Infrastructure / Wiring




<details>
    <summary>Details
    </summary>
        7️⃣ Centralise object creation

        Create:

            WareHouse.infrastructure

        Add:

                public class ApplicationBootstrap {
                public static void main(String[] args) {
                    Connection con = ...
                    ItemRepository itemRepo = new ItemRepository(con);
                    InventoryService service = new InventoryService(itemRepo);
                    InventoryController controller = new InventoryController(service);
                    new MainFrame(controller);
                }
            }


        This replaces Spring by hand.





        Then introduce an infrastructure or bootstrap layer. This is where you manually wire everything together since you are not using Spring. You create the database connection, repositories, services, controllers, and pass controllers into the UI. This replaces dependency injection frameworks with explicit construction.



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







</details>
<br>

[Back To Top](#swing---clean---layers)

















