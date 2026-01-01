# Restaurant Simulator (Java OOP)

A Java-based restaurant simulation project built to practice and strengthen Object-Oriented Programming (OOP) skills through modeling a real-world domain.  
The project focuses on clean domain modeling, clear responsibilities between classes, and service-layer orchestration of restaurant workflows (tables, orders, kitchen, and billing).

> **Goal (CV-focused):** demonstrate practical OOP design, separation of concerns, object collaboration, state handling with enums, and rule enforcement with exceptions.

---

## Project Overview

This project simulates how a restaurant operates by modeling core entities and workflows:

- **Dishes** that exist in the restaurant
- A **Menu** that holds available dishes
- **Orders** that contain multiple items (dish + quantity)
- A **Service layer** that manages restaurant operations:
  - Tables management (seating, freeing, state changes)
  - Orders management (creating and updating orders)
  - Kitchen management (preparing orders)
  - Billing management (calculating totals and handling payment flows)

The project was intentionally chosen because the restaurant domain naturally includes multiple interacting objects and state transitions, making it ideal for practicing real OOP design.

---

## Key Learning Outcomes (What I Practiced)

### 1) Domain Modeling Before Coding
Before writing code, I mapped the restaurant domain:
- What objects exist in a restaurant?
- What attributes must each object hold?
- What are the basic actions each object should support?
- How should objects collaborate with each other?

This helped me design the system around real entities and relationships rather than implementing features “randomly”.

### 2) Designing Fields: Primitive vs Object References
While building each class, I made deliberate decisions about:
- When a field should be a **primitive/value type** (e.g., `int`, `double`, `String`) to represent core attributes
- When a field should be an **object reference** to model relationships (e.g., an `OrderItem` holding a `Dish`)

This shaped the composition and interaction patterns across the system.

### 3) Object Collaboration (Core OOP in Practice)
A major focus was making classes work together through well-defined methods.  
Example from the model layer:
- `OrderItem` uses `Dish.getPrice()` to compute its line total
- `Order` aggregates `OrderItem` totals to compute the full order price

This approach avoids duplicated logic and keeps a clear “source of truth” (e.g., the dish price lives in `Dish`).

### 4) State Management with Enums
I learned to model finite, valid system states using enums (instead of “magic strings” or numbers).  
Examples of modeled states include:
- Table states (e.g., free/occupied/waiting for bill)
- Order states (e.g., created/in preparation/ready/paid)

Enums made the code safer, clearer, and easier to reason about.

### 5) Enforcing Business Rules with Exceptions
I created and used exceptions to enforce restaurant rules and prevent invalid actions (e.g., trying to operate on a table in an incompatible state).  
This helped define system constraints explicitly and ensured methods fail fast when inputs or states are invalid.

### 6) Separation of Concerns (Model vs Services)
I built the project in layers:
- **Model layer**: entities and small composable operations
- **Service layer**: larger workflows and orchestration logic

This separation keeps domain objects focused and allows services to manage flows (tables → orders → kitchen → billing).

---

## Architecture

### Packages

- `app` — application entry point / demo runs
- `model` — core domain entities (Dish, Menu, Order, OrderItem, …)
- `service` — business logic orchestration (tables, orders, kitchen, billing)
- `enums` — system states (order/table statuses)
- `exception` — custom exceptions for rule enforcement

---

## Model Layer (Core Entities)

### `Dish`
Represents a single dish in the restaurant:
- `id`, `name`, `price`, `prepTimeMinutes`
- Read-only access through getters (encapsulation)
- Helpful `toString()` for debugging and printing

### `Menu`
In-memory restaurant menu:
- Stores dishes by `id` for fast lookup
- Enforces unique dish IDs
- Provides both safe lookup (`Optional`) and strict lookup (throwing when missing)
- Returns read-only views for external access

### `OrderItem`
Represents a single “line item” inside an order:
- Links a `Dish` with an ordered `quantity`
- Provides `getTotalPrice()` and controlled quantity updates
- Validates inputs (dish must exist, quantity must be positive)

### `Order`
Represents a full order composed of multiple `OrderItem` entries:
- Stores items by dish ID (one line per dish)
- Adding the same dish increases quantity instead of duplicating lines
- Calculates:
  - total number of dishes (sum of quantities)
  - total number of lines (distinct dishes)
  - total order price (sum of line totals)
- Returns read-only collections for safety

---

## Service Layer (Restaurant Workflows)

I implemented a service layer to represent real restaurant operations and “big actions” that span multiple entities.

Main services include:

- **Table Management Service**
  - seating, freeing, and handling table state transitions
  - coordinating order creation per table

- **Order Management Service**
  - creating and updating orders using menu items
  - ensuring operations match system rules and states

- **Kitchen Service**
  - managing preparation workflows and order readiness
  - tracking orders through kitchen-related statuses

- **Billing Service**
  - coordinating payment workflows
  - calculating totals using the order model
  - updating table/order states as part of closing a table

### Design Challenge (Service ↔ Model Integration)
The most challenging part was designing how services should:
- use model classes (`Dish`, `Menu`, `Order`, `Table`, …)
- rely on model methods to perform workflows
- enforce valid execution flows and state transitions

This stage required thinking carefully about:
- what data should live in the model layer
- what logic belongs in services
- and the correct sequence of operations in a restaurant scenario

---

## Development Process (Phases)

### Phase 1 — Model Foundation
Built the core domain entities first (dishes, menu, orders) and ensured they can:
- represent restaurant data correctly
- perform small, reliable computations
- collaborate cleanly through methods

### Phase 2 — Service Layer Orchestration
Added services to handle restaurant workflows and connect the model into end-to-end flows.

### Phase 3 — Integration Review & Cleanup
Reviewed the system as a whole:
- ensured components work together correctly
- looked for duplicated logic and repeated operations
- refined interactions for consistency and maintainability

### Phase 4 — Testing & Validation
Wrote tests and ran scenarios to confirm:
- model behavior is correct
- services orchestrate flows properly
- key workflows behave as expected

After validating the system through tests, the project was finalized.

---

## Highlights for Recruiters

- Practical **OOP domain modeling** (not a simple CRUD exercise)
- Clean separation between **entities** and **workflows** (Model vs Service)
- Real usage of:
  - **Composition** (Menu → Dish, Order → OrderItem, OrderItem → Dish)
  - **Encapsulation** (private fields + controlled exposure)
  - **Enums** for state management
  - **Exceptions** for rule enforcement
- Validation and confidence through **tests + scenario runs**

---

## Possible Future Extensions
(Planned ideas / natural next steps)
- Persistence (file/DB) for menu and history
- UI layer / CLI improvements
- Advanced scheduling & timing simulation for kitchen prep
- Discounts, splitting bills, or multiple payments
- Reporting (daily revenue, popular dishes, table turnover time)

---

## Tech Stack
- **Java**
- OOP principles (encapsulation, composition, state-driven behavior)
- Collections (`Map`, `LinkedHashMap`, unmodifiable views)
- Enums & custom exceptions
- Tests (scenario-based validation)

---

## Author
**Karen** — Information Systems / Software Engineering student  
Project built as an OOP practice and portfolio piece.
