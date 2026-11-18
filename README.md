# 🤝 Skill Swap - Peer-to-Peer Skill Exchange Platform

**Live Demo:** [https://aashcode-1.onrender.com/](https://aashcode-1.onrender.com/)

**Skill Swap** is a complete, production-ready Java web application built on **Spring Boot 3**, **Spring Data JPA**, and **Thymeleaf**. It connects learners and mentors directly for reciprocal knowledge sharing—enabling users to teach what they excel at and learn what they desire.

---

## 🌟 Tech Stack & Architecture

- **Language:** Java 17+
- **Backend Framework:** Spring Boot 3.2.5 (Spring MVC, Spring Data JPA, Spring Validation)
- **Frontend / View Engine:** Thymeleaf with Bootstrap 5.3 & Bootstrap Icons (responsive mobile-friendly UI)
- **Database:** Embedded H2 Database (zero-configuration in-memory database for local development/testing) + optional MySQL support
- **Design Pattern:** Clean Layered Architecture:
  - **Controllers:** `AuthController`, `PeerController`, `SkillController`, `BookingController`, `ApiController`, `HomeController`, `GlobalControllerAdvice`
  - **Services & Impls:** `UserService`, `PeerService`, `SkillService`, `BookingService`
  - **Repositories:** `UserRepository`, `PeerProfileRepository`, `SkillRepository`, `SessionBookingRepository`
  - **Entity Models:** `User`, `PeerProfile`, `Skill`, `SessionBooking`, Enums (`BookingStatus`, `SkillCategory`, `SkillLevel`, `SkillType`)
  - **DTOs:** `UserRegisterDto`, `UserLoginDto`, `PeerRegistrationDto`, `BookingRequestDto`, `CancelBookingDto`, `WantedSkillGroupDto`

---

## 🚀 Key Features Implemented

1. **User Authentication & Session Management (Login & Registration):**
   - Register with Name, Email, Password, and Bio.
   - Login with session tracking using HTTP session state.
   - 1-click Demo Account Switcher on the navbar for instant testing.
2. **Register as a Peer:**
   - Register / edit peer profile with professional headline, experience years, location, availability, and background.
   - Specify skills you can **TEACH** (offered) with category, description, and proficiency level (Beginner to Expert).
   - Specify skills you want to **LEARN** (desired) with target category and current skill level.
3. **View All Peers:**
   - Public directory of all active peers displaying their headline, location, experience, teaching skills, and wanted skills.
   - Real-time search and filter by skill category.
4. **Skill Want / Learn Directory:**
   - Categorized directory displaying all skills peers want to learn across the community.
   - Associates each wanted skill with the respective peers seeking to learn it and what skills they offer in return.
5. **Search Skill Matches:**
   - Search specifically for skills you want to learn (e.g. `Java`, `Figma`, `Spanish`, `Python`).
   - Instantly finds matching peers offering to teach that skill with proficiency badges and quick booking links.
6. **Session Booking & Collision-Resistant Unique Session ID:**
   - Book a session with any mentor for a desired skill at any scheduled date and time.
   - Choosable duration (30, 45, 60, 90, 120 mins) and preferred platform (Google Meet, Zoom, Teams, Discord).
   - Generates a unique, collision-resistant **Session ID** (format: `SS-XXXX-XXXX`, e.g. `SS-2026-B4T9`).
   - Displays confirmation screen with copyable Session ID.
7. **Booking History Dashboard:**
   - Dedicated dashboard with KPI metric cards (Total, Confirmed, Completed, Cancelled).
   - Tabbed view: **Sessions as Learner** and **Sessions as Mentor**.
   - Full history table with Session ID, Counterpart, Date/Time, Duration, Platform, Status, and Actions.
8. **Session Cancellation by Unique Session ID:**
   - Cancel sessions directly by typing or pasting the unique Session ID into the cancellation portal (`/bookings/cancel`).
   - Or cancel with 1 click directly from the Booking History table.
   - Status updates immediately to `CANCELLED` with reason and timestamp.

---

## 📂 Project Directory Layout

```
java/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .mvn/wrapper/maven-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/com/skillswap/
│   │   │   ├── SkillSwapApplication.java
│   │   │   ├── config/
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── PeerController.java
│   │   │   │   ├── SkillController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   ├── ApiController.java
│   │   │   │   ├── HomeController.java
│   │   │   │   └── GlobalControllerAdvice.java
│   │   │   ├── dto/
│   │   │   │   ├── BookingRequestDto.java
│   │   │   │   ├── CancelBookingDto.java
│   │   │   │   ├── PeerRegistrationDto.java
│   │   │   │   ├── UserLoginDto.java
│   │   │   │   ├── UserRegisterDto.java
│   │   │   │   └── WantedSkillGroupDto.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── PeerProfile.java
│   │   │   │   ├── Skill.java
│   │   │   │   ├── SessionBooking.java
│   │   │   │   └── enums/
│   │   │   │       ├── BookingStatus.java
│   │   │   │       ├── SkillCategory.java
│   │   │   │       ├── SkillLevel.java
│   │   │   │       └── SkillType.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── PeerProfileRepository.java
│   │   │   │   ├── SkillRepository.java
│   │   │   │   └── SessionBookingRepository.java
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   ├── PeerService.java
│   │   │   │   ├── SkillService.java
│   │   │   │   ├── BookingService.java
│   │   │   │   └── impl/
│   │   │   │       ├── UserServiceImpl.java
│   │   │   │       ├── PeerServiceImpl.java
│   │   │   │       ├── SkillServiceImpl.java
│   │   │   │       └── BookingServiceImpl.java
│   │   │   └── util/
│   │   │       └── SessionHelper.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       │   ├── css/custom.css
│   │       │   └── js/app.js
│   │       └── templates/
│   │           ├── layout/
│   │           │   ├── navbar.html
│   │           │   ├── footer.html
│   │           │   └── alerts.html
│   │           ├── auth/
│   │           │   ├── login.html
│   │           │   └── register.html
│   │           ├── peer/
│   │           │   ├── list.html
│   │           │   ├── profile.html
│   │           │   └── register.html
│   │           ├── skill/
│   │           │   ├── directory.html
│   │           │   └── search.html
│   │           ├── booking/
│   │           │   ├── book.html
│   │           │   ├── success.html
│   │           │   ├── history.html
│   │           │   └── cancel.html
│   │           ├── home.html
│   │           └── error.html
│   └── test/java/com/skillswap/
│       ├── SkillSwapApplicationTests.java
│       └── service/BookingServiceTest.java
```

---

## ⚡ How to Run the Application

### Deploy to Render

This is a Java/Spring Boot application, not a Node.js app, so a `node index.js`
start command will not work. Render uses the included `Dockerfile` and
`render.yaml` to build and run the application with Java 17. To deploy, create
a new **Blueprint** in Render from this repository; Render will read
`render.yaml` and use the `PORT` value it assigns to the web service.

### 1. Prerequisites
- **Java Development Kit (JDK 17 or higher)** installed (e.g. Eclipse Temurin, Oracle JDK, or Amazon Corretto).
- Optional: Apache Maven (if not using the Maven wrapper).

### 2. Run via Command Line

**Using Maven:**
```bash
mvn clean spring-boot:run
```

**Using the Maven Wrapper (Windows PowerShell / CMD):**
```powershell
.\mvnw.cmd spring-boot:run
```

**Using the Maven Wrapper (macOS / Linux):**
```bash
./mvnw spring-boot:run
```

### 3. Run in IDE (IntelliJ IDEA / Eclipse / VS Code)
1. Open the folder `java` as a Maven Project.
2. Locate `com.skillswap.SkillSwapApplication.java`.
3. Right-click and select **Run 'SkillSwapApplication'**.

### 4. Open in Browser
Once started, open your web browser to:
- **Application Web UI:** [http://localhost:8080](http://localhost:8080)
- **H2 Database Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:mem:skillswapdb`
  - User Name: `sa`
  - Password: *(leave empty)*

---

## 👥 Seeded Demo Accounts & Credentials

The application automatically seeds 5 realistic peers with offered & desired skills on initial startup:

| Name | Email | Password | Teaches | Wants to Learn |
|---|---|---|---|---|
| **Alex Johnson** | `alex@skillswap.com` | `password123` | Java, Spring Boot, Docker | UI/UX Design, Figma |
| **Maya Lin** | `maya@skillswap.com` | `password123` | UI/UX Design, Figma | Python, Data Science |
| **David Chen** | `david@skillswap.com` | `password123` | Python, Machine Learning | Spanish, Public Speaking |
| **Elena Gomez** | `elena@skillswap.com` | `password123` | Spanish, French | Java, Web Development |
| **Marcus Miller** | `marcus@skillswap.com` | `password123` | Guitar, Music Production | Java, Spring Boot |

> 💡 **Tip:** You can switch between demo accounts instantly with the **"Demo Accounts"** dropdown on the top-right navbar, or by clicking the demo buttons on the `/login` page!

New passwords and seeded demo passwords are stored as BCrypt hashes. Existing accounts with legacy plain-text passwords are upgraded to BCrypt after a successful login.

---

## 🧪 Preloaded Session IDs for Instant Testing

The system pre-populates sample sessions so you can test tracking and cancellation immediately:

1. **`SS-JAVA-2026`**
   - Learner: Elena Gomez &bull; Mentor: Alex Johnson
   - Skill: Java &bull; Status: `CONFIRMED`
   - Test canceling this at: `/bookings/cancel`
2. **`SS-DESG-9842`**
   - Learner: Alex Johnson &bull; Mentor: Maya Lin
   - Skill: UI/UX Design &bull; Status: `CONFIRMED`
3. **`SS-SPAN-3174`**
   - Learner: David Chen &bull; Mentor: Elena Gomez
   - Skill: Spanish &bull; Status: `COMPLETED`

---

## 🌐 REST API Endpoints Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/peers` | List all active peers (optional: `?search=...`) |
| `GET` | `/api/peers/{id}` | Get peer profile details by ID |
| `GET` | `/api/skills/wanted` | Get wanted skills grouped with interested peers |
| `GET` | `/api/skills/offered` | List all offered skills |
| `GET` | `/api/skills/search?q={skill}` | Search peers teaching a specific skill |
| `POST` | `/api/bookings?learnerId={id}` | Book a session (returns generated `sessionId`) |
| `GET` | `/api/bookings/{sessionId}` | Retrieve booking by unique Session ID |
| `POST` | `/api/bookings/{sessionId}/cancel` | Cancel booking by Session ID |
| `GET` | `/api/bookings/user/{userId}` | Get complete booking history for a user |
