package com.skillswap.config;

import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.SessionBooking;
import com.skillswap.entity.Skill;
import com.skillswap.entity.User;
import com.skillswap.entity.enums.BookingStatus;
import com.skillswap.entity.enums.SkillCategory;
import com.skillswap.entity.enums.SkillLevel;
import com.skillswap.entity.enums.SkillType;
import com.skillswap.repository.PeerProfileRepository;
import com.skillswap.repository.SessionBookingRepository;
import com.skillswap.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PeerProfileRepository peerProfileRepository;
    private final SessionBookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PeerProfileRepository peerProfileRepository,
                           SessionBookingRepository bookingRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.peerProfileRepository = peerProfileRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Already seeded
        }

        System.out.println("🌱 Seeding Skill Swap platform sample peers, skills, and bookings...");

        // 1. Alex Johnson (Java & Spring Boot Expert -> Wants UI/UX)
        User alex = new User("Alex Johnson", "alex@skillswap.com", passwordEncoder.encode("password123"),
                "Senior backend engineer passionate about distributed systems, microservices, and mentoring developers.");
        alex.setPhone("+1-555-0101");
        userRepository.save(alex);

        PeerProfile alexProfile = new PeerProfile(alex,
                "Senior Full Stack Java & Cloud Architect",
                "Over 7 years building scalable enterprise Java services, Spring Boot APIs, and cloud deployments.",
                7, "Weekdays after 6:00 PM EST, Saturdays flexible", "New York, USA (EST)");
        alexProfile.addSkill(new Skill("Java", SkillCategory.PROGRAMMING, SkillType.TEACH, SkillLevel.EXPERT, "Modern Java 17/21, Streams, Multithreading, OOP"));
        alexProfile.addSkill(new Skill("Spring Boot", SkillCategory.PROGRAMMING, SkillType.TEACH, SkillLevel.EXPERT, "Spring Data JPA, REST APIs, Security, Microservices"));
        alexProfile.addSkill(new Skill("Docker", SkillCategory.PROGRAMMING, SkillType.TEACH, SkillLevel.ADVANCED, "Containerization & basic Kubernetes"));
        alexProfile.addSkill(new Skill("UI/UX Design", SkillCategory.DESIGN, SkillType.LEARN, SkillLevel.BEGINNER, "Want to learn user research and wireframing for web apps"));
        alexProfile.addSkill(new Skill("Figma", SkillCategory.DESIGN, SkillType.LEARN, SkillLevel.BEGINNER, "Need guidance on auto-layout, components, and responsive design"));
        peerProfileRepository.save(alexProfile);

        // 2. Maya Lin (UI/UX Designer -> Wants Python & Data)
        User maya = new User("Maya Lin", "maya@skillswap.com", passwordEncoder.encode("password123"),
                "Lead Product Designer specializing in design systems, user accessibility, and high-fidelity prototypes.");
        maya.setPhone("+1-555-0102");
        userRepository.save(maya);

        PeerProfile mayaProfile = new PeerProfile(maya,
                "Lead Product Designer & Figma Design System Pro",
                "5 years designing products for startups and enterprise tech companies. Passionate about typography and intuitive UX.",
                5, "Tuesdays & Thursdays 5 PM - 8 PM, Sunday afternoons", "San Francisco, USA (PST)");
        mayaProfile.addSkill(new Skill("UI/UX Design", SkillCategory.DESIGN, SkillType.TEACH, SkillLevel.EXPERT, "User flows, wireframing, heuristic evaluations"));
        mayaProfile.addSkill(new Skill("Figma", SkillCategory.DESIGN, SkillType.TEACH, SkillLevel.EXPERT, "Design tokens, component variants, auto-layout, animations"));
        mayaProfile.addSkill(new Skill("Python", SkillCategory.PROGRAMMING, SkillType.LEARN, SkillLevel.BEGINNER, "Eager to learn Python basics to automate data workflows"));
        mayaProfile.addSkill(new Skill("Data Science", SkillCategory.DATA_SCIENCE, SkillType.LEARN, SkillLevel.BEGINNER, "Interested in user analytics and metrics visualization"));
        peerProfileRepository.save(mayaProfile);

        // 3. David Chen (Machine Learning / Python -> Wants Spanish & Public Speaking)
        User david = new User("David Chen", "david@skillswap.com", passwordEncoder.encode("password123"),
                "AI Researcher and Machine Learning practitioner. Excited to swap code lessons for spoken languages!");
        david.setPhone("+1-555-0103");
        userRepository.save(david);

        PeerProfile davidProfile = new PeerProfile(david,
                "AI & Machine Learning Engineer | Data Scientist",
                "6 years working in predictive modeling, deep learning architectures, and big data processing.",
                6, "Mon/Wed/Fri evenings, Weekend mornings", "Toronto, Canada (EST)");
        davidProfile.addSkill(new Skill("Python", SkillCategory.PROGRAMMING, SkillType.TEACH, SkillLevel.EXPERT, "Data structures, OOP, Pandas, NumPy, FastAPI"));
        davidProfile.addSkill(new Skill("Machine Learning", SkillCategory.DATA_SCIENCE, SkillType.TEACH, SkillLevel.EXPERT, "Scikit-Learn, PyTorch, Model Evaluation, NLP"));
        davidProfile.addSkill(new Skill("Spanish", SkillCategory.LANGUAGES, SkillType.LEARN, SkillLevel.BEGINNER, "Planning a trip to South America; want conversational skills"));
        davidProfile.addSkill(new Skill("Public Speaking", SkillCategory.OTHER, SkillType.LEARN, SkillLevel.BEGINNER, "Need help delivering technical conference keynotes"));
        peerProfileRepository.save(davidProfile);

        // 4. Elena Gomez (Spanish Instructor -> Wants Web Development)
        User elena = new User("Elena Gomez", "elena@skillswap.com", passwordEncoder.encode("password123"),
                "Certified Spanish language educator and cultural enthusiast with students worldwide.");
        elena.setPhone("+34-600-0104");
        userRepository.save(elena);

        PeerProfile elenaProfile = new PeerProfile(elena,
                "Native Spanish Educator & Polyglot Coach",
                "Native speaker with 4 years teaching conversational fluency, Spanish grammar, and business correspondence.",
                4, "Daily 14:00 - 18:00 UTC", "Madrid, Spain (CET)");
        elenaProfile.addSkill(new Skill("Spanish", SkillCategory.LANGUAGES, SkillType.TEACH, SkillLevel.EXPERT, "Pronunciation, natural conversation, grammar masterclass"));
        elenaProfile.addSkill(new Skill("French", SkillCategory.LANGUAGES, SkillType.TEACH, SkillLevel.ADVANCED, "B2/C1 French conversational practice"));
        elenaProfile.addSkill(new Skill("Java", SkillCategory.PROGRAMMING, SkillType.LEARN, SkillLevel.BEGINNER, "Looking to build custom interactive web quizzes in Java"));
        elenaProfile.addSkill(new Skill("Web Development", SkillCategory.PROGRAMMING, SkillType.LEARN, SkillLevel.BEGINNER, "HTML, CSS, JavaScript foundations"));
        peerProfileRepository.save(elenaProfile);

        // 5. Marcus Miller (Guitar & Audio Producer -> Wants Spring Boot)
        User marcus = new User("Marcus Miller", "marcus@skillswap.com", passwordEncoder.encode("password123"),
                "Session guitarist and indie sound engineer. Keen to learn software development to build music tools.");
        marcus.setPhone("+1-555-0105");
        userRepository.save(marcus);

        PeerProfile marcusProfile = new PeerProfile(marcus,
                "Acoustic & Electric Guitarist | Music Producer",
                "Over 8 years performing and producing. Love breaking down music theory into fun, practical fretboard shapes.",
                8, "Flexible afternoons and late nights", "Austin, Texas (CST)");
        marcusProfile.addSkill(new Skill("Guitar", SkillCategory.MUSIC, SkillType.TEACH, SkillLevel.EXPERT, "Chords, scales, fingerpicking, rhythm & solos"));
        marcusProfile.addSkill(new Skill("Music Production", SkillCategory.MUSIC, SkillType.TEACH, SkillLevel.ADVANCED, "Ableton Live, Logic Pro, EQ & compression"));
        marcusProfile.addSkill(new Skill("Java", SkillCategory.PROGRAMMING, SkillType.LEARN, SkillLevel.BEGINNER, "Want to code audio synthesis utilities in Java"));
        marcusProfile.addSkill(new Skill("Spring Boot", SkillCategory.PROGRAMMING, SkillType.LEARN, SkillLevel.BEGINNER, "Want to build a backend for my studio booking site"));
        peerProfileRepository.save(marcusProfile);

        // Seed Sample Bookings
        // Booking 1: Elena booked Alex for Java (Unique Session ID: SS-JAVA-2026)
        SessionBooking b1 = new SessionBooking(elena, alex, "Java",
                LocalDateTime.now().plusDays(2).withHour(18).withMinute(0).withSecond(0).withNano(0),
                60, "Google Meet", "Introductory Java session: object-oriented concepts and variables.");
        b1.setSessionId("SS-JAVA-2026");
        b1.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(b1);

        // Booking 2: Alex booked Maya for UI/UX Design (Unique Session ID: SS-DESG-9842)
        SessionBooking b2 = new SessionBooking(alex, maya, "UI/UX Design",
                LocalDateTime.now().plusDays(4).withHour(19).withMinute(30).withSecond(0).withNano(0),
                45, "Google Meet", "Reviewing wireframe layout and user journey for Skill Swap web app.");
        b2.setSessionId("SS-DESG-9842");
        b2.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(b2);

        // Booking 3: David booked Elena for Spanish (Unique Session ID: SS-SPAN-3174)
        SessionBooking b3 = new SessionBooking(david, elena, "Spanish",
                LocalDateTime.now().minusDays(3).withHour(15).withMinute(0).withSecond(0).withNano(0),
                60, "Zoom", "Basic conversational Spanish greetings and everyday vocabulary.");
        b3.setSessionId("SS-SPAN-3174");
        b3.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(b3);

        System.out.println("✅ Sample data initialized successfully!");
        System.out.println("   - 5 Peers created (Alex, Maya, David, Elena, Marcus)");
        System.out.println("   - Preloaded Session IDs for cancellation/lookup testing:");
        System.out.println("     * SS-JAVA-2026 (Confirmed)");
        System.out.println("     * SS-DESG-9842 (Confirmed)");
        System.out.println("     * SS-SPAN-3174 (Completed)");
    }
}
