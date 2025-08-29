# Bajaj Finserv Submission  
# Bajaj Finserv Qualifier Submission – SQL Question 2 (Java Track)

**Question 2:**  
For each employee, calculate the number of employees in the **same department** who are **younger** (DOB later) than them.  

The result should return:  
- `EMP_ID`  
- `FIRST_NAME`  
- `LAST_NAME`  
- `DEPARTMENT_NAME`  
- `YOUNGER_EMPLOYEES_COUNT`  

And must be **ordered by EMP_ID in descending order**.

---

## Candidate Details
- **Name:** Sai Sathwik Matury  
- **RegNo:** 22BRS1018  
- **Email:** sathwik.790@gmail.com  

---

## Project Overview
This application automatically:

- Generates a webhook on startup  
- Selects the correct SQL problem based on the candidate’s registration number (**even → Question 2**)  
- Writes the SQL query to a file (`final-sql-question2.sql`)  
- Submits the solution to the webhook URL using JWT authentication  

---

## Problem Statement
The application solves a SQL problem involving two tables:

- **DEPARTMENT**: Contains department information  
- **EMPLOYEE**: Contains employee details with department references  

**Task (SQL Question 2):**  
For each employee, calculate the number of employees in the **same department** who are **younger** (DOB later) than them.  

The result should return:  
- `EMP_ID`  
- `FIRST_NAME`  
- `LAST_NAME`  
- `DEPARTMENT_NAME`  
- `YOUNGER_EMPLOYEES_COUNT`  

And must be **ordered by EMP_ID in descending order**.

---

## Solution
The SQL query:  
- Selects employee details  
- Joins department table for department names  
- Uses a correlated subquery to count younger employees in the same department  
- Orders results by employee ID descending  

---

## Technical Details
- **Framework:** Spring Boot 3.3.x  
- **Java Version:** 17  
- **Build Tool:** Maven  
- **HTTP Client:** WebClient (Spring WebFlux)  
- **JSON Processing:** Jackson  

---

## Project Structure
```bash
src/
├── main/
│ ├── java/
│ │ └── com/
│ │ └── example/
│ │ └── bfj/
│ │ ├── BfjJavaQualifierApplication.java # Main application class
│ │ ├── AppConfig.java # WebClient configuration
│ │ ├── AppProperties.java # Candidate details binding
│ │ ├── HiringClient.java # Core business logic
│ │ └── dto/ # DTOs for requests/responses
│ └── resources/
│ └── application.yml # Candidate details & config
├── pom.xml # Maven dependencies
└── README.md 
```

---

## How to Run

### Prerequisites
- Java 17 or higher  
- Maven 3.6 or higher  

### Build and Run
```bash
# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run

# Or build JAR and run
mvn clean package
java -jar target/bfj-java-qualifier-1.0.0.jar
```

---

## JAR File

The application can be packaged as an executable **JAR** file for easy distribution and execution.  

### Build JAR
Run the following command to build the JAR file:

```bash
mvn clean package
```

This will generate a JAR in the `target/` directory, for example:
```bash
target/bfj-java-qualifier-1.0.0.jar
```

### Run JAR

You can run the packaged JAR directly with:

```bash
java -jar target/bfj-java-qualifier-1.0.0.jar
```

---

## Expected Output

```bash
On successful execution, you should see logs like:
Starting BfjJavaQualifierApplication...
App started. Preparing request for 22BRS1018
Webhook generated successfully: [webhook_url]
Got access token: [token]
Wrote SQL to final-sql-question2.sql
Submitting SQL...
Submission response: {"success":true,"message":"Webhook processed successfully"}
Done.
```

---

## SQL Query (Question 2)

```sql
SELECT
    e.EMP_ID,
    e.FIRST_NAME,
    e.LAST_NAME,
    d.DEPARTMENT_NAME,
    (
        SELECT COUNT(*)
        FROM EMPLOYEE e2
        WHERE e2.DEPARTMENT = e.DEPARTMENT
          AND e2.DOB > e.DOB
    ) AS YOUNGER_EMPLOYEES_COUNT
FROM EMPLOYEE e
JOIN DEPARTMENT d
  ON d.DEPARTMENT_ID = e.DEPARTMENT
ORDER BY e.EMP_ID DESC;
```

---

## Notes

- The application runs automatically on startup  
- No controllers or manual endpoints are exposed  
- Uses **Spring WebClient** for HTTP communication  
- JWT token is automatically included in the `Authorization` header  
- SQL query is also saved locally in **`final-sql-question2.sql`**

---

