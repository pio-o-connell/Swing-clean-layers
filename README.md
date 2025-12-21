# Swing - Clean - Layers

<img src="documentation/images/java-swing-widget.jpg" alt="Widget" style="width:60%;">



## Overview
---

# swing-clean-layers

**Project Overview:**  
"Swing Clean Layers"  demonstrates my skills in **Java Swing**, along with **augmented AI**, to create a **swing front end locally and servlet for remote access**.  

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
    - [Kill legacy shortcuts](#kill-legacy-shortcuts)
    - [User Stories](#user-stories)
    - [Wireframes](#wireframes)
    - [Colour Scheme](#colour-scheme)
    - [Fonts](#fonts)
  - [Features](#features)
    - [Responsivity](#responsivity)
  - [Improvements](#improvements)
  - [Deployment](#deployment)
  - [Testing and Validation](#testing-and-validation)
      - [HTML Validation](#html-validation)
      - [CSS Validation](#css-validation)
      - [Python Validation](#python-validation)
      - [JS Validation](#js-validation)
      - [Lighthouse](#lighthouse)
      - [Automated Testing](#automated-testing)
      - [Manual Testing](#manual-testing)
- [Manual Testing](#manual-testing-1)
  - [AI Implementation](#ai-implementation)
    - [Code Creation](#code-creation)
    - [Debugging](#debugging)
    - [Performance and Experience](#performance-and-experience)
    - [Development Process](#development-process)
  - [Database](#database)
  - [References](#references)
  - [Tech Employed](#tech-employed)
  - [Learning Points](#learning-points)

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

### Kill legacy shortcuts

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

















### User Stories
<details>
  <summary>Details</summary>

  <div style="margin-left: 20px;">
    <ul>
      <li>Authentication & Profiles
        <ul>
          <li> As a SuperUser, I want to manage users, services, and bookings efficiently.</li>
          <li> As a SuperUser, I can edit profiles to keep user information up to date.</li>
          <li> As a SuperUser, I can moderate blogs and comments to ensure content is relevant and appropriate.</li>
          <li> As a User, I can successfully log in so that I can securely edit blogs/comments.</li>
          <li> As a Guest, I can register so that I can contribute blogs/comments securely.</li>
        </ul>
      </li>
      <li>Blog Management
        <ul>
          <li>Registered User can create blogs, adding to gardening knowledge pool.</li>
          <li>Guest can read all blogs.</li>
          <li>Registered User can update their blogs.</li>
          <li>Registered User can delete their blogs.</li>
        </ul>
      </li>
      <li>Comment Management
        <ul>
          <li>Visiting guest can create comments, adding to blog's knowledge pool.</li>
          <li>Visiting guest can read comments.</li>
          <li>Returning guest or registered User can update their comments.</li>
          <li>Returning guest or registered User can delete their comments.</li>
        </ul>
      </li>
      <li>Shopping Cart / Booking Creation
        <ul>
          <li>Visiting guest can create a shopping cart persisted in stored cookies or session data.</li>
          <li>Visiting guest can create a booking to complete a transaction. Information is retrieved from stored cookies or session data.</li>
          <li>Registered User can create a shopping cart.</li>
          <li>Registered User can create a booking to complete a transaction. Information is retrieved from the shopping cart.</li>
        </ul>
      </li>
      <li>Booking Management
        <ul>
          <li>As a SuperUser, I can approve bookings. Information is retrieved from Bookings.</li>
        </ul>
      </li>
    </ul>
  </div>
</details>

### Wireframes
<div style="margin-left: 20px;">
  <details>
    <summary>Mobile pages logged in</summary>
    <img src="documentation/images/Balsamiq/Images/mobile-loggedin-all.png" alt="mobile pages" style="width:60%;">
  </details>
</div>

<div style="margin-left: 20px;">
  <details>
    <summary>Mobile pages logged out</summary>
    <img src="documentation/images/Balsamiq/Images/mobile-loggedOut-all.png" alt="mobile pages" style="width:60%;">
  </details>
</div>

<div style="margin-left: 20px;">
  <details>
    <summary>XL and Large Home Page</summary>
    <img src="documentation/images/Balsamiq/Images/HomePage-XL-large.png" alt="mobile pages" style="width:60%;">
  </details>
</div>

<div style="margin-left: 20px;">
  <details>
    <summary>XL and Large Blog Page</summary>
    <img src="documentation/images/Balsamiq/Images/BlogPage-XL-large.png" alt="mobile pages" style="width:60%;">
  </details>
</div>

<div style="margin-left: 20px;">
  <details>
    <summary>XL and Large Comments Page</summary>
    <img src="documentation/images/Balsamiq/Images/CommentPage-XL-Large.png" alt="mobile pages" style="width:60%;">
  </details>
</div>

<div style="margin-left: 20px;">
  <details>
    <summary>XL Services Page</summary>
    <img src="documentation/images/Balsamiq/Images/XL-Services.png" alt="mobile pages" style="width:60%;">
  </details>
</div>

<div style="margin-left: 20px;">
  <details>
    <summary>Large Services Page</summary>
    <img src="documentation/images/Balsamiq/Images/Large-Services.png" alt="mobile pages" style="width:60%;">
  </details>
</div>

<div style="margin-left: 20px;">
  <details>
    <summary>Large and XL Booking Page</summary>
    <img src="documentation/images/Balsamiq/Images/XL-large-Booking.png" alt="mobile pages" style="width:60%;">
  </details>
</div>

<div style="margin-left: 20px;">
  <details>
    <summary>Large and XL Registration Page</summary>
    <img src="documentation/images/Balsamiq/Images/XL-large-Registration.png" alt="mobile pages" style="width:60%;">
  </details>
</div>


### Colour Scheme

There are a lot of colours in a garden already so I used , one PRIMARY colour for all text except a SECONDARY colour for a Call to Action and the use of whitespace where appropiate.
The main page is using the design principle of thirds to create a more balanced and interesting composition.

I used image picker website to choose a colour scheme https://imagecolorpicker.com/
The image is
<details>
<summary>Click to image from which  colour palette generated
</summary>
<img src="core/static/core/images/winters/winters-2.jpg" alt="Image from which colour palette generated" style="width:60%;">
</details><br>


<div style="margin-left: 40px;">
  <img src="documentation/images/SWATCH.jpg" alt="Image from which colour palette generated" style="width:60%;"><br>
</div>
<details>
<summary>Details</summary>

I used one **PRIMARY** colour for all text, a **SECONDARY** colour for the Call to Action, and whitespace appropriately.  
The main page uses the **rule of thirds** for a balanced composition.  
Colours were selected using [Image Color Picker](https://imagecolorpicker.com/).

| Main Palette       | Hex       | Usage                                   |
|-------------------|-----------|----------------------------------------|
| Flax              | #E9D98A   | Background                              |
| Dark Moss Green   | #506D1B   | CTA / Secondary colour                  |
| Ecru              | #C9B66B   | Highlight links / hover effects         |
| Eggshell          | #E6E2D2   | Background areas or cards               |
| Davy’s Grey       | #4D4D4D   | Primary colour / text                   |

Accessibility checks were done with [Colour Contrast Checker](https://colourcontrast.cc/), [WebAIM](https://webaim.org/resources/contrastchecker/), and [Adobe Color](https://color.adobe.com/create/color-contrast-analyzer).

<details>
<summary>Click to view Colour Contrast Checker results</summary>
<img src="core/static/core/images/Color/Colorcontrastcc.jpg" alt="Colour contrast results" style="width:60%;">
</details>

<details>
<summary>Click to view WebAIM results</summary>
<img src="core/static/core/images/Color/web-aim.jpg" alt="WebAIM contrast results" style="width:60%;">

</details>

<details>
<summary>Click to view Adobe Color results</summary>
<img src="core/static/core/images/Color/adobe-color-analyzer.jpg" alt="Adobe Color contrast results" style="width:60%;">
</details>

</details>




### Fonts
<details>
  <summary>Details</summary>
    <div style="margin-left: 20px;">
      Similar to colour, the font should be easy to read.<br> 
      Therefore, only **one font, Inter**, is necessary for titles, body text, and the call to action (CTA).<br>    
      This was implemented through Google Fonts using a direct import within the `style.css` file.<br><br>
  </div>
  <div style="margin-left: 20px;">
    <code>
    /* Google Fonts import */
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@100..900&family=Macondo&display=swap');
    </code>
  </div>
</details>
<br>

[Back To Top](#harmonia)


## Features
--- 
<details>
  <summary>Project Features & Pages</summary>

  <div style="margin-left: 20px;">
    <details>
      <summary>Home Page</summary>
      There is a fold in the page by design.  
      <br><br>
      <img src="README-images/Features/HomePage-Part1.jpg" alt="Home page image" width="40%">
      <img src="README-images/Features/HomePage-Part2.jpg" alt="Home page image" width="40%">
    </details>
  </div>

  <div style="margin-left: 20px;">
    <details>
      <summary>Login Page</summary>
      Login Page – There are four types of users: <strong>Admin, Customer, Blogger</strong>, and Guests can also browse.  
      <br><br>
      <img src="README-images/Features/CustomerLogin.jpg" alt="Login page image" width="40%">
      <img src="README-images/Features/AdminScreen.jpg" alt="Login page image" width="40%">
    </details>
  </div>

  <div style="margin-left: 20px;">
    <details>
      <summary>Registration Page</summary>
      Registration Page – A guest becomes a customer, then a blogger after submitting a blog.  
      <br><br>
      <img src="README-images/Features/RegistrationPage.jpg" alt="Register page image" width="40%">
      <img src="README-images/Features/email-sent.jpg" alt="Email sent confirmation" width="40%">
    </details>
  </div>

  <div style="margin-left: 20px;">
    <details>
      <summary>Logout Page</summary>
      Logout Page – Confirmation screen showing user is logged out and returned to the home page.  
      <br><br>
      <img src="README-images/Features/LoggedOut.jpg" alt="Logged out page" width="30%">
    </details>
  </div>

  <div style="margin-left: 20px;">
    <details>
      <summary>Blog Page</summary>
      A guest can create a blog by first submitting a test blog.  
      Once approved by the administrator, the customer becomes a registered blogger.  
      <br><br>
      <img src="README-images/Features/BlogPage-Guest.jpg" alt="Guest Blog Page" width="30%">
      <img src="README-images/Features/BlogPageBlogger.jpg" alt="Blogger Blog Page" width="30%">
      <img src="README-images/Features/MyBlogPostsBlogger.jpg" alt="My Blog Posts" width="30%">
    </details>
  </div>

  <div style="margin-left: 20px;">
    <details>
      <summary>Comment Page</summary>
      A guest can <strong>create, edit, delete</strong> their own comments.  
      A customer, fellow blogger, and the administrator can also comment.  
      <br><br>
      <img src="README-images/Features/GuestAddCommentBefore.jpg" alt="Guest comment before" width="30%">
      <img src="README-images/Features/GuestAddcommentAfter.jpg" alt="Guest comment after" width="30%">
      <img src="README-images/Features/RegisteredAddComment.jpg" alt="Registered user comment" width="30%">
    </details>
  </div>

  <div style="margin-left: 20px;">
    <details>
      <summary>Services / Add To Cart</summary>
      A guest, customer, blogger, and admin can add four different services to the cart.  
      <br><br>
      <img src="README-images/Features/Services-guest.jpg" alt="Guest Services Page" width="30%">
      <img src="README-images/Features/Services-Shoppingcart-Guest.jpg" alt="Guest Shopping Cart" width="30%">
      <img src="README-images/Features/Services-Cart-Customer.jpg" alt="Customer Shopping Cart" width="30%">
    </details>
  </div>

  <div style="margin-left: 20px;">
    <details>
      <summary>Booking</summary>
      A guest, customer, blogger, and admin can perform a booking.  
      A guest must become a customer before a booking is completed.  
      <br><br>
      <img src="README-images/Features/BookingServicesGuest.jpg" alt="Guest booking" width="40%">
      <img src="README-images/Features/BookingCompleteGuest.jpg" alt="Guest booking confirmation" width="40%">
      <img src="README-images/Features/BookingCustomer.jpg" alt="Customer booking" width="40%">
      <img src="README-images/Features/BookingCompleteCustomer.jpg" alt="Customer booking confirmation" width="40%">
    </details>
  </div>

  <div style="margin-left: 20px;">
    <details>
      <summary>Administration</summary>
      Administrator can monitor users, blogs, comments, services, and bookings.  
      <br><br>
      <img src="README-images/Features/AdminScreen.jpg" alt="Admin screen" width="30%">
      <img src="README-images/Features/AdminDashboard1.jpg" alt="Admin dashboard" width="60%">
    </details>
  </div>

  <div style="margin-left: 20px;">
    <details>
      <summary>Custom Error Pages</summary>
      Django only supports custom handlers for <strong>four</strong> HTTP error codes:
      <ul>
        <li>400 – Bad Request</li>
        <li>403 – Permission Denied</li>
        <li>404 – Page Not Found</li>
        <li>500 – Server Error</li>
      </ul>
      All other error codes are generated by the system or web server.  
      <br>
      <img src="core/static/core/images/errors/400.jpg" alt="400 Bad Request" width="20%">
      <img src="core/static/core/images/errors/403.jpg" alt="403 Forbidden" width="20%">
      <img src="core/static/core/images/errors/404.jpg" alt="404 Not Found" width="20%">
      <img src="core/static/core/images/errors/500.jpg" alt="500 Server Error" width="20%">
    </details>
  </div>

</details>
<br>

[Back To Top](#harmonia)

### Responsivity 
--- 
<details>
  <summary>Details</summary>
  <div style="margin-left: 20px;">
    <details>
      <summary>Home Page</summary>
      <br>
      <img src="README-images/Responsiveness/Home-page/320px584px/320px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Home-page/375px/375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Home-page/425px/425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Home-page/768px/768px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Home-page/1024px/1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Home-page/1440px/1440px.jpg" alt="Home page image" width="25%">
    </details>
  </div>
  <div style="margin-left: 20px;">
    <details>
      <summary>Guest or Customer Blog Page</summary>
      <br>
      <img src="README-images/Responsiveness/Blog-Page-Guest/320px584px/320px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Blog-Page-Guest/375px/375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Blog-Page-Guest/425px/425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Blog-Page-Guest/768px/768.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Blog-Page-Guest/1024px/1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Blog-Page-Guest/1440px/1440px.jpg" alt="Home page image" width="25%">
    </details>
  </div>
  <div style="margin-left: 20px;">
    <details>
      <summary>Blogger Blog Page</summary>
      <br>
      <img src="README-images/Responsiveness/Blog-Page-Blogger/320px584px/320px.jpg " alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Blog-Page-Blogger/375px/375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Blog-Page-Blogger/425px/425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Blog-Page-Blogger/768px/768px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Blog-Page-Blogger/1024px/1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Blog-Page-Blogger/1440px/1440px.jpg" alt="Home page image" width="25%">
    </details>
  </div>
  <div style="margin-left: 20px;">
    <details>
      <summary>Blogger DashBoard</summary>
      <br>
      <img src="README-images/Responsiveness/BlogPage-Dashboard/320px584px/320px.jpg " alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/BlogPage-Dashboard/375px/375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/BlogPage-Dashboard/425px/425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/BlogPage-Dashboard/768px/768px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/BlogPage-Dashboard/1024px/1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/BlogPage-Dashboard/1440px/1440px.jpg" alt="Home page image" width="25%">
    </details>
  </div>
  <div style="margin-left: 20px;">
    <details>
      <summary>Services</summary>
      <br>
      <img src="README-images/Responsiveness/Services-Page-Guest/320px584px/320px-1.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Services-Page-Guest/375px/375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Services-Page-Guest/425px/425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Services-Page-Guest/768px/768px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Services-Page-Guest/1024px/1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Services-Page-Guest/1440px/1440px.jpg" alt="Home page image" width="25%">
    </details>
  </div>
  <div style="margin-left: 20px;">
    <details>
      <summary>Bookings (as a guest) </summary>
      <br>
      <img src="README-images/Responsiveness/BookingPage-guest/320px584px/320px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/BookingPage-guest/375px/375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/BookingPage-guest/425px/425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/BookingPage-guest/768px/768px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/BookingPage-guest/1024px/1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/BookingPage-guest/1440px/1440px.jpg" alt="Home page image" width="25%">
    </details>
  </div>
  <div style="margin-left: 20px;">
    <details>
      <summary>Bookings (as a customer) </summary>
      <br>
      <img src="README-images/Responsiveness/Bookings-Page-registered/320px584px/320px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Bookings-Page-registered/375px/375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Bookings-Page-registered/425px/425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Bookings-Page-registered/768px/768px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Bookings-Page-registered/1024px/1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Bookings-Page-registered/1440px/1440px.jpg" alt="Home page image" width="25%">
    </details>
  </div>
  <div style="margin-left: 20px;">
    <details>
      <summary>Register (as a customer) </summary>
      <br>
      <img src="README-images/Responsiveness/Register-as-customer/320px584px/320px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-customer/375px/375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-customer/425px/425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-customer/768px/768px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-customer/1024px/1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-customer/1440px/1440px.jpg" alt="Home page image" width="25%">
    </details>
  </div>
  <div style="margin-left: 20px;">
    <details>
      <summary>Register (as a blogger) </summary>
      <br>
      <img src="README-images/Responsiveness/Register-as-blogger/First page/320px584px/320px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-blogger/First page/375px/375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-blogger/First page/425px/425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-blogger/First page/768px/768.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-blogger/First page/1024px/1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-blogger/First page/1440px/1440px.jpg" alt="Home page image" width="25%">
    </details>
  </div>
  <div style="margin-left: 20px;">
    <details>
      <summary>Register (as a blogger) - Part 2 </summary>
      <br>
      <img src="README-images/Responsiveness/Register-as-blogger/First page/320px584px/2ndpart-320px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-blogger/First page/375px/2ndpart-375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-blogger/First page/425px/2bdpart-425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-blogger/First page/768px/2ndpart-768px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-blogger/First page/1024px/2ndpart-1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Register-as-blogger/First page/1440px/2ndpart-1440px.jpg" alt="Home page image" width="25%">
    </details>
  </div>
  <div style="margin-left: 20px;">
    <details>
      <summary>Admin </summary>
      <br>
      <img src="README-images/Responsiveness/Admin/320px584px/320px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Admin/375px/375px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Admin/425px/425px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Admin/768px/768px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Admin/1024px/1024px.jpg" alt="Home page image" width="25%">
      <img src="README-images/Responsiveness/Admin/1440px/1440.jpg" alt="Home page image" width="25%">
    </details>
  </div>


</details>
<br>

[Back To Top](#harmonia)

## Improvements
--- 
<details>
    <summary>Details
    </summary>

- There are **outstanding media query adjustments** needed for each page to ensure full responsiveness on all screen sizes.  
- Further **UI/UX refinements** could enhance user experience, including spacing, button sizes, and layout consistency.  
- Optimization of **static files and media loading** could improve performance and reduce page load times.  
- Implementing additional **form validations and error handling** would make the application more robust.  
- Adding **unit tests and automated testing** could further ensure code reliability and maintainability.  

<br>
 <br>
<br>
 
</details>
<br>

[Back To Top](#harmonia)


## Deployment
---
<details>
  <summary>Details</summary>
  <div style="margin-left: 20px;">
    <p>
      This <a href="https://github.com/">GitHub</a> project was created using the 
      <a href="https://github.com/Code-Institute-Org/ci-full-template">Code Institute Template</a>, ensuring all necessary dependencies.
    </p>
    <p>Setup a repo using this method and template:</p>
    <ul>
      <li>Login to your GitHub profile.</li>
      <li>Navigate to the Code Institute Full Template.</li>
      <li>Click the dropdown for 'Use this template' and select "Create a new repository".</li>
      <li>Generate the necessary name and description for your repo and click 'Create repository from template'.</li>
      <li>Navigate to the new repo and click the green 'Open' button with the Gitpod logo.
        <ul>
          <li><strong>IMPORTANT</strong> - This button should only be clicked once to generate the new IDE workspace.</li>
        </ul>
      </li>
      <li>You can now work on your repository within the Code Institute Gitpod IDE workspace.</li>
    </ul>
    <p>Once the project repo is created, an early deployment for the live project should be performed. This allows for early and continuous testing using a variety of devices, as well as the Dev Tools available within browsers.</p>
    <p>Additional information on the deployment process can be found on the official 
      <a href="https://docs.github.com/en/pages/quickstart">GitHub Docs</a>.
    </p>
  </div>
</details>
<br>

[Back To Top](#harmonia)



## Testing and Validation
---

#### HTML Validation
<details>
  <summary>Details</summary>

  <div style="margin-left: 20px;">
    <p>The HTML was checked using the W3C markup validation service: 0 errors found on home page.</p>
    <br>
    <img src="README-images/Testing&Validation/html-validator/HTMLValidator-HomePage.png" alt="HTML W3C Results" style="width:30%;">
    <img src="README-images/Testing&Validation/html-validator/HTMLValidator-BlogPage.png" alt="HTML W3C Results" style="width:30%;">
    <img src="README-images/Testing&Validation/html-validator/HTMLValidator -Services.png" alt="HTML W3C Results" style="width:30%;">
    <img src="README-images/Testing&Validation/html-validator/HTMLValidator-Bookings.png" alt="HTML W3C Results" style="width:30%;">
    <img src="README-images/Testing&Validation/html-validator/HTMLValidator-Register.png" alt="HTML W3C Results" style="width:30%;">
  </div>
</details>

#### CSS Validation
<details>
  <summary>Details</summary>

  <div style="margin-left: 20px;">
    <p>The CSS was checked using the W3C validation service: 0 errors found (only vendor-prefix warnings for `-ms-flexbox`, `-webkit`, etc.).</p>
    <img src="README-images/Testing&Validation/CSS/HomePage-stylesheet.png" alt="CSS Jigsaw Results" style="width:30%;">
  </div>
</details>

#### Python Validation
<details>
  <summary>Details</summary>

  <div style="margin-left: 20px;">
    <p>All apps were checked using Flake8 tool to comply with PEP 8 standard.</p>
    <br>
    <img src="README-images/Testing&Validation/Pep8-using-flake8/Pep9 results .png" alt="PEP 8 conformance" style="width:30%;">
  </div>
</details>

#### JS Validation
<details>
  <summary>Details</summary>

  <div style="margin-left: 20px;">
    <p>All main pages were checked using JSHint tool. JSHint conforms to configurable JavaScript rules and ECMAScript versions rather than a single standard.</p>
    <img src="README-images/Testing&Validation/JSHint/AdminPage-JSHint.jpg" alt="Javascript conformance" style="width:30%;">
    <img src="README-images/Testing&Validation/JSHint/BlogPage-JSHinr.jpg" alt="Javascript conformance" style="width:30%;">
    <img src="README-images/Testing&Validation/JSHint/Services-JSLint.jpg" alt="Javascript conformance" style="width:30%;">
    <img src="README-images/Testing&Validation/JSHint/Bookings-JSHint.jpg" alt="Javascript conformance" style="width:30%;">
    <img src="README-images/Testing&Validation/JSHint/Login-JSHint.jpg" alt="Javascript conformance" style="width:30%;">
    <img src="README-images/Testing&Validation/JSHint/register-jshint.jpg" alt="Javascript conformance" style="width:30%;">
  </div>
</details>

#### Lighthouse
<details>
  <summary>Details</summary>

  <div style="margin-left: 20px;">
    <img src="README-images/Testing&Validation/Lighthouse/MainPage-LighthouseResults.jpg" alt="Lighthouse Results" style="width:40%;">
    <img src="README-images/Testing&Validation/Lighthouse/BlogPage-Lighthouse-Results.jpg" alt="Lighthouse Results" style="width:40%;">
    <img src="README-images/Testing&Validation/Lighthouse/BlogPageCard-LighthouseResults.jpg" alt="Lighthouse Results" style="width:40%;">
    <img src="README-images/Testing&Validation/Lighthouse/Services-LightHouseResults.jpg" alt="Lighthouse Results" style="width:40%;">
    <img src="README-images/Testing&Validation/Lighthouse/Bookings-LighthouseResults.jpg" alt="Lighthouse Results" style="width:40%;">
    <img src="README-images/Testing&Validation/Lighthouse/Register-LighthoouseResults.jpg" alt="Lighthouse Results" style="width:40%;">
    <img src="README-images/Testing&Validation/Lighthouse/Dashboard-LightHouseReport.jpg" alt="Lighthouse Results" style="width:40%;">
    <br><br>
    <p>All apps were checked using Lighthouse tool to comply with best practices and performance standards.</p>
  </div>
</details>


#### Automated Testing
<details>
  <summary>Details</summary>
  <div style="margin-left: 20px;">
    <p>A number of Python scripts were used to automatically generate large amounts of test data for the database. These scripts are located in the <code>DB-scripts</code> and <code>scripts</code> folders.</p>
    <p><strong>Data generation scripts:</strong></p>
    <ul>
        python DB-scripts/create-postgres-users.py<br>
        python DB-scripts/create-postgres-post_comments_v2.py --posts-per-blogger 2 <br>
        python scripts/populate_services.py<br>
        python scripts/populate_bookings.py<br>
    </ul>
    <p><strong>Verification scripts:</strong></p>
    <ul>
        python verify-users.py
        python verify_post_comments.py
    </ul>
      <p>These scripts were used to ensure that all database tables populated correctly and that relationships between users, posts, services, and bookings were valid.</p>
    </div>
</details>


#### Manual Testing 
<details>
  <summary>Details</summary>
  <div style="margin-left: 20px;">
 # Manual Testing

- Conducted extensive **manual testing** throughout the project to ensure all features worked as expected.  
- Tested **user registration, login, and role-based access** to verify correct behavior for different user types.  
- Performed **form validation and error handling checks** to ensure proper feedback for invalid inputs.  
- Verified **media uploads, file handling, and static files** to confirm correct storage and display.  
- Checked **site functionality across different scenarios**, including edge cases and unexpected user behavior.  
- Manual testing helped identify **bugs and usability issues** that automated tools might miss.  

</details>

<br>

[Back To Top](#harmonia)


## AI Implementation
---
<details>
  <summary>Details</summary>

  ### Code Creation
  I paired with GitHub Copilot and GPT-5.1-Codex (Preview) for boilerplate such as Django CRUD views, Bootstrap card layouts, and model docstrings. Every suggestion was reviewed, adapted to Harmonia’s requirements, and validated by running the test suite plus manual browser checks before merging.

  ### Debugging
  Copilot Chat helped interpret stack traces (e.g., `IntegrityError` when linking bookings to carts) and proposed fixes I then implemented and retested. I also asked for additional edge-case unit tests, which I executed locally to confirm regressions were fixed.

  ### Performance and Experience
  AI assistants highlighted spots where template loops could be optimized and suggested moving heavy assets behind lazy loading, which improved Lighthouse performance metrics by ~5–8 points on the Services and Blog pages.

  ### Development Process
  AI sped up documentation (sections of this README, user-story phrasing) and repetitive coding tasks while I retained ownership of architectural decisions, security reviews, and CI sign-off. Secrets were kept out of version control, and every AI-generated change was committed only after manual review and formatting via Flake8.
</details>
<br>

[Back To Top](#harmonia)

## Database
--- 
<details>
    <summary>Details
    </summary>
   <img src="documentation/images/DatabaseERdiagram.jpeg" alt="HTML W3C Results" style="width:30%;"><br><br>
    The database is a Postgres database hosted by Code institute <br><br>
   
</details>
<br>

[Back To Top](#harmonia)

## References
--- 
<details>
<summary>Details</summary>

  <h4>Documentation</h4>
  <ul>
      <li>
          <a href="https://docs.djangoproject.com/en/5.2/ref/models/querysets/" target="_blank">
              Django documentation – very useful for several sections
          </a>
      </li>
  </ul>

  <h4>W3Schools</h4>
  <ul>
      <li>
          <a href="https://www.w3schools.com/python/python_lists_comprehension.asp" target="_blank">
              Reminder on how list comprehension works
          </a>
      </li>
  </ul>

  <h4>Old Projects</h4>
  <ul>
      <li>
          <a href="https://github.com/pio-o-connell/Individual" target="_blank">
              GitHub – Older project reference
          </a>
      </li>
  </ul>
</details>
 <br>   

[Back To Top](#harmonia)

## Tech Employed
--- 
<details>
  <summary>Details</summary>

  <h3>Languages</h3>
  <img src="https://img.shields.io/badge/HTML5-Language-grey?logo=html5&logoColor=%23ffffff&color=%23E34F26" alt="HTML5 badge">
  <img src="https://img.shields.io/badge/CSS3-Language-grey?logo=css3&logoColor=%23ffffff&color=%231572B6" alt="CSS3 badge">
  <img src="https://img.shields.io/badge/javascript-Language-blue?logo=javascript&logoColor=%23ffffff&color=%23E34F26" alt="javascript badge">
  <img src="https://img.shields.io/badge/Python-3.12.8-grey?logo=python&logoColor=%23ffffff&color=%233776AB" alt="Python badge">

  <h3>Libraries and Frameworks</h3>
  <a href="https://getbootstrap.com/"><img src="https://img.shields.io/badge/Bootstrap-v5.3.3-grey?logo=bootstrap&logoColor=%23ffffff&color=%237952B3" alt="Bootstrap" ></a>
    <a href="https://getbootstrap.com/"><img src="https://img.shields.io/badge/Font_Awesome-Icons-grey?logo=fontawesome&logoColor=%23ffffff&color=%23538DD7" alt="Font Awesome"></a>
    <a href="https://getbootstrap.com/"><img src="https://img.shields.io/badge/Google_Fonts-Fonts-grey?logo=googlefonts&logoColor=%23ffffff&color=%234285F4" alt="Google Fonts"></a>


  <h3>Tools and Programs</h3>
  <img src="https://img.shields.io/badge/Balsamiq-Wireframes-grey?logoColor=%23ffffff&color=%23CC0100" alt="Balsamiq badge">
  <img src="https://img.shields.io/badge/Git-v2.51.0-grey?logo=git&logoColor=%23ffffff&color=%23F05032" alt="Git">
  <img src="https://img.shields.io/badge/GitHub-Repo_Hosting-white?logo=github&logoColor=%23ffffff&color=%23181717 " alt="Git hosting">
    <img src="https://img.shields.io/badge/Microsoft-Copilot-5E5E5E?style=flat-square&logo=microsoft&logoColor=white&color=0078D4" alt="Microsoft Copilot">
    <img src="https://img.shields.io/badge/ChatGPT-00A67E?style=flat-square&logo=openai&logoColor=white&color=00A67E" alt="ChatGPT">
    <img src="https://img.shields.io/badge/GitHub_Copilot-181717?logo=githubcopilot&logoColor=white&color=1B1F23" alt="GitHub Copilot">
    <a href="https://figma.com"><img src="https://img.shields.io/badge/Figma-green" alt="Figma"></a>
    <a href="https://squoosh.app">
    <img src="https://img.shields.io/badge/Squoosh-squoosh.app-0b69ff?style=for-the-badge" alt="Squoosh">
    </a>
</details>
<br>

[Back To Top](#harmonia)

## Learning Points
--- 
<details>
    <summary>Details
    </summary><br>

- I have previous experience in software development and testing, which helped in understanding the project requirements and workflow.

- This project allowed me to experience designing and implementing an application from the top down, from planning the architecture to deploying the final product.

- Using GitHub Copilot greatly accelerated development. It provides suggestions drawn from a large knowledge base, helping to quickly scaffold code and implement functionality.

- However, Copilot has limitations: it cannot visually detect problems, and human logic and interaction remain essential for debugging, design decisions, and user experience considerations.

- Application development with Python libraries is rapid and versatile, demonstrating the flexibility and power of the Python ecosystem for building web applications.
</details>
<br>

[Back To Top](#harmonia)
