# Stepper Project
## Overview
The Stepper Project is a Java-based workflow management system that allows users to design, execute, and manage workflows (referred to as "flows") composed of reusable components (steps). It is built with a multithreaded, client-server architecture to support simultaneous operations and provide a scalable, user-friendly platform for managing complex processes.

This project demonstrates advanced Java development skills, focusing on modularity, GUI integration, and effective data management.

---

## Features
1. **Flow Definition and Execution**:

-Design flows by assembling reusable components (steps).
-Execute flows while tracking progress and collecting runtime data.

2. **Multithreaded Execution**:
-Supports simultaneous execution of multiple flows.
-Ensures efficient resource utilization and real-time monitoring.

3.**User Management**:
-Role-based access control for flow management and execution.
-Separate permissions for administrators and regular users.

4.**Data Tracking and Analytics**:
-Collect and store data from flow executions.
-Generate insights and reports based on historical execution data.

5.**Graphical User Interface (GUI)**:
-Intuitive GUI for designing, managing, and executing workflows.
-Real-time updates on flow execution status and performance metrics.

6. **Serialization**:
-JSON-based serialization for saving and restoring workflows and configurations.

7. **Client-Server Architecture**:
-Centralized server for managing flows, users, and data.
-Lightweight client applications for interaction.
8. **Error Handling**:
-Comprehensive validation during flow creation and execution.
-Detailed error messages for troubleshooting.

## Technologies and Skills Demonstrated

- Java OOP: Modular design with reusable components.
- Multithreading: Efficient execution of concurrent workflows.
- Client-Server Model: Communication and data exchange between server and clients.
- JSON Serialization: Persistent storage and retrieval of workflows.
- GUI Development: Interactive user interfaces with real-time feedback.
- Role-Based Access Control: Fine-grained permission management.
- Data Analytics: Aggregation and analysis of flow execution data.

## System Architecture
**Engine System**
Workflow Definition: Allows mapping inputs to outputs and defining workflows.
Execution Management: Tracks inputs, outputs, logs, and summaries during workflow execution.
Statistics Collection: Gathers and displays execution data for workflows and steps.

**Graphical User Interface (GUI)**
The GUI is built with JavaFX and includes:
1. Dashboard: Displays all workflows and their details.
2. Execution Screen: Launch workflows with required inputs and monitor progress.
3. History Screen: View past executions and their detailed results.
4. Statistics Screen: Displays workflow execution data in tables and charts.
   
**Client-Server Architecture**
*Server Features*:
Role Management: Define and assign roles with specific workflow permissions.
User Management: Create and manage user accounts.
*Client Features*:
Workflow Execution: Users can run workflows and view personal execution history.
Role-Based Access Control: Permissions determine accessible workflows.

## Technologies Used
- Java: Core programming language.
- JavaFX: GUI development.
- CSS: Styling for the GUI.
- Multithreading: Enables asynchronous execution.
- Client-Server Model: Supports multiple users with distinct permissions.

## How It Works
**For Administrators**
- Upload flow definition files to the system.
- Manage user roles and permissions.
- View and analyze all user executions.
**For Regular Users**
- Log in with a unique username.
- Execute authorized flows and view results.
- Access personal execution history and statistics.
**For Managers**
- Have elevated permissions to access all flows and execution histories

