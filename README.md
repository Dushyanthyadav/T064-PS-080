# PS-080: EV Charging Network Billing and Slot Reservation Engine
**Team ID:** T064  
**Team Members:** Divya Sanjana S (1AT24CG024), Dushyanth Yadav V (1AT24CG025)  
**Track:** 10 SECONDS Capstone Project (Semester V)

---

## Architecture Overview
Headless, deterministic back-end billing engine written in **Java 21** implementing:
* Multi-tier Time-Of-Use (TOU) dynamic energy tariffs.
* Charger-type hardware multipliers (AC 7 kW, DC 30 kW, DC 60 kW).
* Real-time wallet balance validation, running charge tracking, and safety auto-cutoff.
* Single-command execution and automated regression test runner.

---

## Build & Run Instructions

### Prerequisites
* Java Development Kit (JDK 17 or 21)
* Apache Maven 3.8+

### Execution Commands

```bash
# 1. Compile the engine
mvn clean compile

# 2. Execute automated test suite (Week 2 requirement)
mvn test

# 3. Package and run CLI application
mvn package
java -cp target/ev-charging-engine-1.0.0.jar com.tenseconds.ev.Main
```
