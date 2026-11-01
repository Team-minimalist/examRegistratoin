// Student Dashboard Logic

document.addEventListener("DOMContentLoaded", async () => {
    try {
        const authRes = await fetch("/api/auth/me");
        if (authRes.status === 401) {
            window.location.href = "/login.html";
            return;
        }

        const authData = await authRes.json();
        if (!authData.success || !authData.data) {
            window.location.href = "/login.html";
            return;
        }

        const user = authData.data;

        // Populate student info
        document.getElementById("student-name").textContent = user.name || user.username;
        document.getElementById("student-email").textContent = user.email;

        const warningBanner = document.getElementById("profile-warning-banner");
        if (!user.profileCompleted) {
            warningBanner.style.display = "block";
        } else {
            warningBanner.style.display = "none";
        }

        // Fetch available exams count
        const examsRes = await fetch("/api/exams");
        if (examsRes.ok) {
            const examsData = await examsRes.json();
            const openExams = (examsData.data || []).filter(e => e.registrationStatus === "OPEN");
            document.getElementById("stat-available-exams").textContent = openExams.length;
            renderUpcomingExams(examsData.data || []);
        }

        // Fetch registered exams count
        const regRes = await fetch("/api/registrations/me");
        if (regRes.ok) {
            const regData = await regRes.json();
            const registrations = regData.data || [];
            document.getElementById("stat-registered-exams").textContent = registrations.length;
            renderRecentRegistrations(registrations);
        }

    } catch (err) {
        console.error("Dashboard error:", err);
    }
});

function renderUpcomingExams(exams) {
    const container = document.getElementById("upcoming-exams-list");
    if (!container) return;

    const available = exams.filter(e => e.active && e.registrationStatus !== "COMPLETED").slice(0, 3);

    if (available.length === 0) {
        container.innerHTML = `<p style="color: var(--gray); font-size: 0.9rem;">No upcoming exams currently listed.</p>`;
        return;
    }

    container.innerHTML = available.map(exam => `
        <div class="exam-card" style="margin-bottom: 1rem;">
            <div class="exam-card-header">
                <h4 class="exam-card-title">${escapeHtml(exam.examName)}</h4>
                <span class="badge badge-${exam.registrationStatus.toLowerCase().replace('_', '-')}">${exam.registrationStatus}</span>
            </div>
            <p class="exam-card-meta">
                <strong>Org:</strong> ${escapeHtml(exam.conductingOrganization)}<br>
                <strong>Exam Date:</strong> ${exam.examDate}<br>
                <strong>Deadline:</strong> ${exam.registrationEndDate}
            </p>
            <div class="exam-card-footer">
                <a href="/exam-details.html?id=${exam.id}" class="btn btn-secondary btn-sm">View Details</a>
                ${exam.canRegister ? `<a href="/exam-details.html?id=${exam.id}&action=register" class="btn btn-primary btn-sm">Register Now</a>` : ''}
            </div>
        </div>
    `).join("");
}

function renderRecentRegistrations(registrations) {
    const tbody = document.getElementById("recent-registrations-tbody");
    if (!tbody) return;

    if (registrations.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" style="text-align: center; color: var(--gray); padding: 1.5rem;">You have not registered for any examinations yet.</td></tr>`;
        return;
    }

    tbody.innerHTML = registrations.slice(0, 5).map(reg => `
        <tr>
            <td><strong>${escapeHtml(reg.registrationNumber)}</strong></td>
            <td>${escapeHtml(reg.examName)}</td>
            <td>${reg.examDate || 'N/A'}</td>
            <td>${reg.registeredAt ? reg.registeredAt.substring(0, 10) : 'N/A'}</td>
            <td>
                <a href="/registration-success.html?id=${reg.id}" class="btn btn-secondary btn-sm">View Slip</a>
            </td>
        </tr>
    `).join("");
}

function escapeHtml(str) {
    if (!str) return "";
    return str.replace(/[&<>'"]/g, tag => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        "'": '&#39;',
        '"': '&quot;'
    }[tag] || tag));
}

