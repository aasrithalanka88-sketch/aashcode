# ===================================================================
# Skill Swap - Standalone Local Development HTTP Server
# Zero-dependency, immediate local execution on port 8080
# ===================================================================

$port = 8080
$prefix = "http://localhost:$port/"
$listener = New-Object System.Net.HttpListener
$listener.Prefixes.Add($prefix)

try {
    $listener.Start()
    Write-Host "==========================================================" -ForegroundColor Green
    Write-Host "  🚀 Skill Swap Platform is LIVE on port $port!" -ForegroundColor Cyan
    Write-Host "  🌐 Open: http://localhost:8080/peers" -ForegroundColor Yellow
    Write-Host "==========================================================" -ForegroundColor Green
} catch {
    Write-Host "Failed to start listener on $prefix : $_" -ForegroundColor Red
    exit 1
}

# Seed In-Memory Database
$script:peers = @(
    @{
        id = 1; name = "Alex Johnson"; email = "alex@skillswap.com"; initials = "AJ";
        headline = "Senior Full Stack Java & Cloud Architect";
        location = "New York, USA"; experience = 7;
        availability = "Weekdays 6 PM - 9 PM EST, Saturdays flexible";
        teaching = @("Java", "Spring Boot", "Docker", "REST APIs");
        learning = @("UI/UX Design", "Figma", "Digital Marketing");
        bio = "Over 7 years building scalable enterprise Java services, Spring Boot APIs, and cloud deployments."
    },
    @{
        id = 2; name = "Maya Lin"; email = "maya@skillswap.com"; initials = "ML";
        headline = "Lead Product Designer & Figma Design System Pro";
        location = "San Francisco, USA"; experience = 5;
        availability = "Tuesdays & Thursdays 5 PM - 8 PM PST";
        teaching = @("UI/UX Design", "Figma", "Design Systems", "Prototyping");
        learning = @("Python", "Data Science", "SQL");
        bio = "5 years designing products for tech startups. Passionate about typography and intuitive UX."
    },
    @{
        id = 3; name = "David Chen"; email = "david@skillswap.com"; initials = "DC";
        headline = "AI & Machine Learning Engineer | Data Scientist";
        location = "Toronto, Canada"; experience = 6;
        availability = "Mon/Wed/Fri evenings, Weekend mornings";
        teaching = @("Python", "Machine Learning", "Data Analysis", "TensorFlow");
        learning = @("Spanish", "Public Speaking", "Product Management");
        bio = "AI Researcher and ML practitioner. Excited to swap code lessons for spoken languages."
    },
    @{
        id = 4; name = "Elena Gomez"; email = "elena@skillswap.com"; initials = "EG";
        headline = "Native Spanish Educator & Polyglot Coach";
        location = "Madrid, Spain"; experience = 4;
        availability = "Daily 14:00 - 18:00 UTC";
        teaching = @("Spanish", "French", "Conversational Spanish");
        learning = @("Java", "Web Development", "HTML/CSS");
        bio = "Certified Spanish educator with 4 years helping students worldwide speak with natural confidence."
    },
    @{
        id = 5; name = "Marcus Miller"; email = "marcus@skillswap.com"; initials = "MM";
        headline = "Acoustic & Electric Guitarist | Music Producer";
        location = "Austin, Texas"; experience = 8;
        availability = "Flexible afternoons & late nights";
        teaching = @("Guitar", "Music Production", "Songwriting");
        learning = @("Java", "Spring Boot", "App Development");
        bio = "Session guitarist and indie sound engineer. Love breaking down music theory into practical shapes."
    }
)

$script:bookings = [System.Collections.ArrayList]@(
    [PSCustomObject]@{
        sessionId = "SS-JAVA-2026"; learner = "Elena Gomez"; mentor = "Alex Johnson";
        mentorId = 1; skill = "Java"; dateTime = "Tomorrow at 6:00 PM";
        duration = 60; platform = "Google Meet"; status = "CONFIRMED";
        notes = "Introductory Java session: object-oriented concepts and variables."
    },
    [PSCustomObject]@{
        sessionId = "SS-DESG-9842"; learner = "Alex Johnson"; mentor = "Maya Lin";
        mentorId = 2; skill = "UI/UX Design"; dateTime = "Thursday at 7:30 PM";
        duration = 45; platform = "Google Meet"; status = "CONFIRMED";
        notes = "Reviewing wireframe layout and user journey for Skill Swap web app."
    },
    [PSCustomObject]@{
        sessionId = "SS-SPAN-3174"; learner = "David Chen"; mentor = "Elena Gomez";
        mentorId = 4; skill = "Spanish"; dateTime = "3 days ago";
        duration = 60; platform = "Zoom"; status = "COMPLETED";
        notes = "Basic conversational Spanish greetings and everyday vocabulary."
    }
)

$script:currentUser = $script:peers[0]

function Get-HtmlHeader($title) {
    return @"
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>$title | Skill Swap</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        :root { --primary-color: #4f46e5; --bg-light: #f8fafc; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background-color: var(--bg-light); min-height: 100vh; display: flex; flex-direction: column; }
        main { flex: 1; }
        .avatar-circle { width: 44px; height: 44px; border-radius: 50%; background: linear-gradient(135deg, #4f46e5, #06b6d4); color: white; display: inline-flex; align-items: center; justify-content: center; font-weight: 700; font-size: 1rem; }
        .card-hover { border-radius: 0.75rem; border: 1px solid #e2e8f0; box-shadow: 0 2px 4px rgba(0,0,0,0.04); transition: transform 0.15s, box-shadow 0.15s; }
        .card-hover:hover { transform: translateY(-3px); box-shadow: 0 8px 16px rgba(0,0,0,0.08); }
        .badge-teach { background-color: #dbeafe; color: #1e40af; border: 1px solid #bfdbfe; font-weight: 500; }
        .badge-learn { background-color: #fef3c7; color: #92400e; border: 1px solid #fde68a; font-weight: 500; }
        .session-id-display { font-family: monospace; font-size: 1.5rem; font-weight: 800; background-color: #f1f5f9; border: 2px dashed #4f46e5; color: #4f46e5; padding: 0.5rem 1.25rem; border-radius: 0.5rem; display: inline-block; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-white sticky-top shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold text-primary fs-4" href="/">
                <i class="bi bi-arrow-left-right me-1"></i>Skill<span class="text-dark">Swap</span>
            </a>
            <div class="collapse navbar-collapse show" id="nav">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item"><a class="nav-link fw-semibold text-dark" href="/peers"><i class="bi bi-people me-1"></i>All Peers</a></li>
                    <li class="nav-item"><a class="nav-link fw-semibold text-dark" href="/skills/wanted"><i class="bi bi-journal-bookmark me-1"></i>Want to Learn Directory</a></li>
                    <li class="nav-item"><a class="nav-link fw-semibold text-dark" href="/skills/search"><i class="bi bi-search me-1"></i>Search Matches</a></li>
                    <li class="nav-item"><a class="nav-link fw-semibold text-danger" href="/bookings/cancel"><i class="bi bi-x-circle me-1"></i>Cancel Session</a></li>
                </ul>
                <div class="d-flex align-items-center gap-2">
                    <a class="btn btn-sm btn-outline-primary" href="/bookings/history"><i class="bi bi-calendar-check me-1"></i>Booking History</a>
                    <a class="btn btn-sm btn-primary" href="/peers/register"><i class="bi bi-person-plus me-1"></i>Register as Peer</a>
                    <span class="badge bg-light text-dark border ms-2">Logged in as: <strong>$($script:currentUser.name)</strong></span>
                </div>
            </div>
        </div>
    </nav>
    <main class="container my-4">
"@
}

function Get-HtmlFooter() {
    return @"
    </main>
    <footer class="bg-white border-top py-3 mt-auto text-center text-muted small">
        <div class="container">
            <strong>Skill Swap Platform</strong> &copy; 2026. Peer-to-Peer Skill Exchange. Active on <a href="http://localhost:8080/peers">http://localhost:8080/peers</a>
        </div>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
"@
}

# Main Loop
while ($listener.IsListening) {
    $context = $listener.GetContext()
    $req = $context.Request
    $res = $context.Response
    $rawUrl = $req.RawUrl
    $path = $req.Url.AbsolutePath
    $query = $req.Url.Query

    $content = ""
    $statusCode = 200

    # ROUTING
    if ($path -eq "/" -or $path -eq "/peers") {
        $peersHtml = ""
        foreach ($p in $script:peers) {
            $teachBadges = ($p.teaching | ForEach-Object { "<span class='badge badge-teach me-1 mb-1'>$_</span>" }) -join " "
            $learnBadges = ($p.learning | ForEach-Object { "<span class='badge badge-learn me-1 mb-1'>$_</span>" }) -join " "
            $peersHtml += @"
            <div class="col-md-6 col-lg-4">
                <div class="card card-hover p-4 h-100 d-flex flex-column bg-white">
                    <div class="d-flex align-items-center gap-3 mb-3">
                        <div class="avatar-circle">$($p.initials)</div>
                        <div>
                            <h5 class="fw-bold mb-0">$($p.name)</h5>
                            <small class="text-muted"><i class="bi bi-geo-alt"></i> $($p.location) &bull; $($p.experience) yrs exp</small>
                        </div>
                    </div>
                    <div class="text-primary small fw-semibold mb-2">$($p.headline)</div>
                    <p class="text-muted small mb-3">$($p.bio)</p>
                    <div class="mb-2">
                        <div class="small fw-bold text-primary mb-1"><i class="bi bi-mortarboard-fill me-1"></i>Can Teach (Offered):</div>
                        <div>$teachBadges</div>
                    </div>
                    <div class="mb-4">
                        <div class="small fw-bold text-warning mb-1"><i class="bi bi-lightbulb-fill me-1"></i>Wants to Learn (Desired):</div>
                        <div>$learnBadges</div>
                    </div>
                    <div class="mt-auto pt-3 border-top d-flex gap-2">
                        <a href="/bookings/new?mentorId=$($p.id)&skill=$([System.Web.HttpUtility]::UrlEncode($p.teaching[0]))" class="btn btn-sm btn-primary w-100">
                            <i class="bi bi-calendar-plus me-1"></i> Book Session
                        </a>
                    </div>
                </div>
            </div>
"@
        }

        $content = (Get-HtmlHeader "All Active Peers") + @"
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fw-bold mb-1"><i class="bi bi-people text-primary me-2"></i>All Active Peers</h2>
                <p class="text-muted mb-0">Browse registered peers, check their offered skills, and book a reciprocal swap session.</p>
            </div>
            <a href="/peers/register" class="btn btn-primary"><i class="bi bi-plus-lg me-1"></i>Register as a Peer</a>
        </div>
        <div class="row g-4">$peersHtml</div>
"@ + (Get-HtmlFooter)

    } elseif ($path -eq "/skills/wanted") {
        $wantedGroups = @{
            "UI/UX Design" = @{ category="Design"; peers=@("Alex Johnson"); quote="Want to learn wireframing & responsive layouts." }
            "Python" = @{ category="Programming"; peers=@("Maya Lin", "Marcus Miller"); quote="Want to learn Python for data analysis & scripting." }
            "Spanish" = @{ category="Languages"; peers=@("David Chen"); quote="Conversational Spanish practice." }
            "Java" = @{ category="Programming"; peers=@("Elena Gomez", "Marcus Miller"); quote="Foundations of Java and Spring Boot APIs." }
            "Figma" = @{ category="Design"; peers=@("Alex Johnson"); quote="Figma auto-layout & component library masterclass." }
        }

        $groupsHtml = ""
        foreach ($k in $wantedGroups.Keys) {
            $g = $wantedGroups[$k]
            $peerBadges = ($g.peers | ForEach-Object { "<span class='badge bg-light text-dark border me-1'>$_</span>" }) -join " "
            $groupsHtml += @"
            <div class="col-md-6">
                <div class="card card-hover p-4 bg-white h-100">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h4 class="fw-bold text-dark mb-0">$k</h4>
                        <span class="badge bg-warning text-dark">$($g.category)</span>
                    </div>
                    <p class="text-muted small mb-3">"$($g.quote)"</p>
                    <div class="mb-3">
                        <span class="small fw-semibold text-secondary">Peers Seeking this Skill:</span>
                        <div class="mt-1">$peerBadges</div>
                    </div>
                    <a href="/skills/search?q=$k" class="btn btn-sm btn-outline-primary mt-auto">
                        <i class="bi bi-search me-1"></i> Find Mentors Teaching $k
                    </a>
                </div>
            </div>
"@
        }

        $content = (Get-HtmlHeader "Want to Learn Directory") + @"
        <h2 class="fw-bold mb-1"><i class="bi bi-journal-bookmark text-warning me-2"></i>Want to Learn Directory</h2>
        <p class="text-muted mb-4">Skills members are actively looking to learn. Connect and offer to teach!</p>
        <div class="row g-4">$groupsHtml</div>
"@ + (Get-HtmlFooter)

    } elseif ($path -eq "/skills/search") {
        $q = ""
        if ($query -match "q=([^&]+)") {
            $q = [System.Web.HttpUtility]::UrlDecode($matches[1])
        }

        $matchedPeers = $script:peers
        if ($q -ne "") {
            $matchedPeers = $script:peers | Where-Object { 
                $p = $_
                ($p.teaching -join " ") -match $q -or $p.headline -match $q -or $p.name -match $q
            }
        }

        $matchesHtml = ""
        foreach ($p in $matchedPeers) {
            $matchesHtml += @"
            <div class="col-md-6 col-lg-4">
                <div class="card card-hover p-4 bg-white h-100 d-flex flex-column">
                    <div class="d-flex align-items-center gap-3 mb-2">
                        <div class="avatar-circle">$($p.initials)</div>
                        <div>
                            <h5 class="fw-bold mb-0">$($p.name)</h5>
                            <small class="text-muted">$($p.location)</small>
                        </div>
                    </div>
                    <div class="text-primary small fw-semibold mb-3">$($p.headline)</div>
                    <div class="small fw-bold text-success mb-1">Teaches:</div>
                    <div class="mb-3">$(($p.teaching | ForEach-Object { "<span class='badge badge-teach me-1'>$_</span>" }) -join ' ')</div>
                    <a href="/bookings/new?mentorId=$($p.id)&skill=$([System.Web.HttpUtility]::UrlEncode($q))" class="btn btn-sm btn-primary mt-auto">
                        <i class="bi bi-calendar-plus me-1"></i> Book Session
                    </a>
                </div>
            </div>
"@
        }

        $content = (Get-HtmlHeader "Search Skill Matches") + @"
        <div class="text-center mb-4">
            <h2 class="fw-bold mb-2">Search Skill Matches</h2>
            <form action="/skills/search" method="get" class="d-flex justify-content-center gap-2 mt-3 mx-auto" style="max-width: 500px;">
                <input type="text" name="q" value="$q" class="form-control" placeholder="Search skill (e.g. Java, Python, Spanish, Figma)...">
                <button type="submit" class="btn btn-primary px-4"><i class="bi bi-search"></i> Search</button>
            </form>
        </div>
        <div class="row g-4">$matchesHtml</div>
"@ + (Get-HtmlFooter)

    } elseif ($path -eq "/bookings/new") {
        $mentorId = 1
        if ($query -match "mentorId=([0-9]+)") { $mentorId = [int]$matches[1] }
        $skill = "Java"
        if ($query -match "skill=([^&]+)") { $skill = [System.Web.HttpUtility]::UrlDecode($matches[1]) }
        $mentor = ($script:peers | Where-Object { $_.id -eq $mentorId })[0]

        if ($req.HttpMethod -eq "POST") {
            # Generate Session ID
            $chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
            $rand = -join ((1..4) | ForEach-Object { $chars[(Get-Random -Maximum $chars.Length)] })
            $rand2 = -join ((1..4) | ForEach-Object { $chars[(Get-Random -Maximum $chars.Length)] })
            $newSessionId = "SS-$rand-$rand2"

            $newBooking = [PSCustomObject]@{
                sessionId = $newSessionId
                learner = $script:currentUser.name
                mentor = $mentor.name
                mentorId = $mentor.id
                skill = $skill
                dateTime = "Tomorrow at 6:00 PM"
                duration = 60
                platform = "Google Meet"
                status = "CONFIRMED"
                notes = "Scheduled peer-to-peer learning session."
            }
            $script:bookings.Insert(0, $newBooking)

            $res.Redirect("/bookings/confirmed?sessionId=$newSessionId")
            $res.Close()
            continue
        }

        $content = (Get-HtmlHeader "Book Learning Session") + @"
        <div class="row justify-content-center">
            <div class="col-lg-7">
                <div class="card card-hover p-4 bg-white mb-4">
                    <div class="d-flex align-items-center gap-3 mb-3">
                        <div class="avatar-circle">$($mentor.initials)</div>
                        <div>
                            <span class="badge bg-primary text-white">Selected Mentor</span>
                            <h4 class="fw-bold mb-0">$($mentor.name)</h4>
                            <small class="text-muted">$($mentor.headline)</small>
                        </div>
                    </div>
                    <form action="/bookings/new?mentorId=$($mentor.id)&skill=$([System.Web.HttpUtility]::UrlEncode($skill))" method="post">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Skill to Learn</label>
                            <input type="text" name="skill" value="$skill" class="form-control" required>
                        </div>
                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Date</label>
                                <input type="date" name="date" class="form-control" value="$(Get-Date -Format 'yyyy-MM-dd')" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Time</label>
                                <input type="time" name="time" class="form-control" value="18:00" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Platform</label>
                            <select class="form-select">
                                <option>Google Meet</option>
                                <option>Zoom</option>
                                <option>Microsoft Teams</option>
                            </select>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-semibold">Session Agenda / Questions</label>
                            <textarea class="form-control" rows="3" placeholder="What would you like to cover?">Reviewing core concepts and coding hands-on.</textarea>
                        </div>
                        <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">
                            <i class="bi bi-check2-circle me-1"></i> Confirm Booking & Generate Session ID
                        </button>
                    </form>
                </div>
            </div>
        </div>
"@ + (Get-HtmlFooter)

    } elseif ($path -eq "/bookings/confirmed") {
        $sid = "SS-JAVA-2026"
        if ($query -match "sessionId=([^&]+)") { $sid = [System.Web.HttpUtility]::UrlDecode($matches[1]) }
        $b = ($script:bookings | Where-Object { $_.sessionId -eq $sid })[0]
        if (-not $b) { $b = $script:bookings[0] }

        $content = (Get-HtmlHeader "Booking Confirmed") + @"
        <div class="row justify-content-center">
            <div class="col-lg-7 text-center">
                <div class="card card-hover p-5 bg-white">
                    <i class="bi bi-check-circle-fill text-success display-3 mb-3"></i>
                    <h2 class="fw-bold text-success mb-1">Session Successfully Booked!</h2>
                    <p class="text-muted mb-4">Your peer learning session is confirmed.</p>
                    
                    <div class="p-4 bg-light rounded border mb-4">
                        <div class="text-muted small fw-semibold text-uppercase mb-1">Unique Session ID</div>
                        <div class="session-id-display">$($b.sessionId)</div>
                    </div>

                    <div class="text-start bg-light p-3 rounded mb-4">
                        <div class="row g-2">
                            <div class="col-6"><strong>Learner:</strong> $($b.learner)</div>
                            <div class="col-6"><strong>Mentor:</strong> $($b.mentor)</div>
                            <div class="col-6"><strong>Skill:</strong> <span class="badge badge-teach">$($b.skill)</span></div>
                            <div class="col-6"><strong>Time:</strong> $($b.dateTime)</div>
                        </div>
                    </div>

                    <div class="d-flex gap-2 justify-content-center">
                        <a href="/bookings/history" class="btn btn-primary px-4">View Booking History</a>
                        <a href="/bookings/cancel?sessionId=$($b.sessionId)" class="btn btn-outline-danger px-4">Cancel Session</a>
                    </div>
                </div>
            </div>
        </div>
"@ + (Get-HtmlFooter)

    } elseif ($path -eq "/bookings/history") {
        $rowsHtml = ""
        foreach ($b in $script:bookings) {
            $badgeColor = if ($b.status -eq "CONFIRMED") { "bg-success" } elseif ($b.status -eq "CANCELLED") { "bg-danger" } else { "bg-primary" }
            $actionBtn = if ($b.status -eq "CONFIRMED") {
                "<a href='/bookings/cancel?sessionId=$($b.sessionId)' class='btn btn-sm btn-outline-danger'>Cancel</a>"
            } else {
                "<span class='text-muted small'>None</span>"
            }
            $rowsHtml += @"
            <tr>
                <td><code class="fw-bold fs-6 text-primary">$($b.sessionId)</code></td>
                <td><strong>$($b.skill)</strong></td>
                <td>$($b.mentor)</td>
                <td>$($b.dateTime)</td>
                <td><span class="badge $badgeColor">$($b.status)</span></td>
                <td class="text-end">$actionBtn</td>
            </tr>
"@
        }

        $content = (Get-HtmlHeader "Booking History") + @"
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fw-bold mb-1"><i class="bi bi-calendar-check text-primary me-2"></i>Booking History Dashboard</h2>
                <p class="text-muted mb-0">Complete history of all scheduled skill swap sessions.</p>
            </div>
            <a href="/bookings/cancel" class="btn btn-outline-danger"><i class="bi bi-x-circle me-1"></i>Cancel by Session ID</a>
        </div>
        <div class="card card-hover bg-white overflow-hidden">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Session ID</th>
                        <th>Skill</th>
                        <th>Mentor / Counterpart</th>
                        <th>Scheduled Time</th>
                        <th>Status</th>
                        <th class="text-end">Action</th>
                    </tr>
                </thead>
                <tbody>$rowsHtml</tbody>
            </table>
        </div>
"@ + (Get-HtmlFooter)

    } elseif ($path -eq "/bookings/cancel") {
        $sid = ""
        if ($query -match "sessionId=([^&]+)") { $sid = [System.Web.HttpUtility]::UrlDecode($matches[1]) }

        if ($req.HttpMethod -eq "POST") {
            # Read body
            $reader = New-Object System.IO.StreamReader($req.InputStream)
            $body = $reader.ReadToEnd()
            if ($body -match "sessionId=([^&]+)") {
                $sid = [System.Web.HttpUtility]::UrlDecode($matches[1])
            }
            # Cancel matching booking
            foreach ($b in $script:bookings) {
                if ($b.sessionId -eq $sid.Trim().ToUpper()) {
                    $b.status = "CANCELLED"
                }
            }
            $res.Redirect("/bookings/history")
            $res.Close()
            continue
        }

        $content = (Get-HtmlHeader "Cancel Session") + @"
        <div class="row justify-content-center">
            <div class="col-lg-6">
                <div class="card card-hover p-4 p-md-5 bg-white border-danger">
                    <div class="text-center mb-4">
                        <i class="bi bi-x-circle-fill text-danger display-4 mb-2"></i>
                        <h3 class="fw-bold text-danger">Cancel Session Booking</h3>
                        <p class="text-muted small">Enter the unique <strong>Session ID</strong> to cancel the booking.</p>
                    </div>
                    <form action="/bookings/cancel" method="post">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Unique Session ID</label>
                            <input type="text" name="sessionId" value="$sid" class="form-control font-monospace fs-5 text-uppercase" placeholder="e.g. SS-JAVA-2026" required autofocus>
                            <div class="form-text">Try: <code>SS-JAVA-2026</code> or <code>SS-DESG-9842</code></div>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-semibold">Reason for Cancellation</label>
                            <textarea class="form-control" rows="2" placeholder="Optional reason..."></textarea>
                        </div>
                        <button type="submit" class="btn btn-danger w-100 py-2 fw-semibold" onclick="return confirm('Confirm cancellation?');">
                            Confirm Cancellation
                        </button>
                    </form>
                </div>
            </div>
        </div>
"@ + (Get-HtmlFooter)

    } elseif ($path -eq "/peers/register") {
        if ($req.HttpMethod -eq "POST") {
            $reader = New-Object System.IO.StreamReader($req.InputStream)
            $body = $reader.ReadToEnd()
            $res.Redirect("/peers")
            $res.Close()
            continue
        }

        $content = (Get-HtmlHeader "Register as Peer") + @"
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card card-hover p-4 p-md-5 bg-white">
                    <h3 class="fw-bold mb-1">Register as a Peer</h3>
                    <p class="text-muted small mb-4">Specify the skills you can <strong>teach</strong> and the skills you want to <strong>learn</strong>.</p>
                    <form action="/peers/register" method="post">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Professional Headline</label>
                            <input type="text" class="form-control" value="Senior Java Developer & Mentor" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Skills You Can Teach (Comma-separated)</label>
                            <input type="text" class="form-control" value="Java, Spring Boot, Microservices" required>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-semibold">Skills You Want to Learn (Comma-separated)</label>
                            <input type="text" class="form-control" value="UI/UX Design, Figma, Spanish" required>
                        </div>
                        <button type="submit" class="btn btn-primary px-5 py-2 fw-semibold">Save & Publish Profile</button>
                    </form>
                </div>
            </div>
        </div>
"@ + (Get-HtmlFooter)

    } else {
        $res.Redirect("/peers")
        $res.Close()
        continue
    }

    # Send response
    $buffer = [System.Text.Encoding]::UTF8.GetBytes($content)
    $res.ContentLength64 = $buffer.Length
    $res.ContentType = "text/html; charset=UTF-8"
    $res.StatusCode = $statusCode
    $res.OutputStream.Write($buffer, 0, $buffer.Length)
    $res.Close()
}
