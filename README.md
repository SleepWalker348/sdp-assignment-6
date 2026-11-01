# Smart Code Inspection & Compliance Engine
## Visitor Pattern

### 1. What problem does Visitor solve in your project?
The Visitor pattern separates data structures (`ProjectElement` hierarchy: files and directories) from the operations performed on them (`ProjectVisitor` hierarchy: security audit, statistics, quality gates, compliance checks). This allows adding new inspection rules and reports without altering the element classes.

### 2. Where are Element and Visitor?
* **Elements:** Located in package `sdp.element` (`ProjectElement`, `SourceCodeFile`, `TestFile`, `ConfigurationFile`, `DependencyFile`, `Directory`).
* **Visitors:** Located in package `sdp.visitor` (`ProjectVisitor`, `SecurityAuditVisitor`, `ProjectStatisticsVisitor`, `QualityGateVisitor`, `ComplianceVisitor`, `TechnicalDebtVisitor`).

### 3. What is double dispatch?
Double dispatch is a mechanism where the polymorphic execution depends on the dynamic types of *two* objects:
1. First dispatch: Calling `element.accept(visitor)` dynamically resolves to the concrete element implementation (e.g., `SourceCodeFile.accept`).
2. Second dispatch: Inside `accept`, calling `visitor.visit(this)` dynamically resolves the correct overloaded `visit(...)` method on the concrete visitor based on the element's exact type.

### 4. Why not put all operations directly inside the Element classes?
Placing operations inside element classes violates the Single Responsibility Principle (SRP) and Open/Closed Principle (OCP). Every time a new audit or report is introduced, all file classes would need to be modified, leading to bloated, hard-to-maintain code.

### 5. Is it easier to add a new Visitor or a new Element? Why?
It is **significantly easier to add a new Visitor**. Adding a new Visitor requires creating one new class implementing `ProjectVisitor` without touching existing element classes (OCP). Conversely, adding a new concrete element requires modifying the `ProjectVisitor` interface and updating **every existing concrete visitor** to implement a new `visit(NewElement)` method.

### 6. When would Visitor be a bad choice?
Visitor is a poor choice when the element hierarchy is frequently changing (adding or removing concrete element types), as every visitor must be updated each time a new element type is introduced.

### 7. How is Visitor different from Strategy?
* **Strategy Pattern** encapsulates a single algorithm used by a context object (usually chosen via composition).
* **Visitor Pattern** defines an operation across an entire heterogeneous object structure (often composite), executing specific methods tailored to each component type in the structure.

### 8. Which SOLID principles can you identify?
* **Open/Closed Principle (OCP):** New visitors can be added without modifying existing element classes.
* **Single Responsibility Principle (SPP):** Each visitor handles a distinct audit or calculation concern.
* **Interface Segregation Principle (ISP):** Clients depend on cleanly separated element and visitor interfaces.


## Visitor Limitation Challenge: Adding `DatabaseMigrationFile`
If a completely new element `DatabaseMigrationFile` is added:
1. The `ProjectElement` interface remains unchanged.
2. The `DatabaseMigrationFile` class must be created implementing `ProjectElement`.
3. The `ProjectVisitor` interface must be updated with `void visit(DatabaseMigrationFile file);`.
4. **All concrete visitors** (`SecurityAuditVisitor`, `ProjectStatisticsVisitor`, `QualityGateVisitor`, `ComplianceVisitor`, `TechnicalDebtVisitor`) must be updated to implement `visit(DatabaseMigrationFile)`.