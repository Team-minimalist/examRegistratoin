// Examination list and details handling

document.addEventListener("DOMContentLoaded", async () => {
    // Check if on details page or list page
    const detailsContainer = document.getElementById("exam-details-container");
    if (detailsContainer) {
        loadExamDetails();
    } else {
        loadExamList();
    }
});

async function loadExamList() {
    const grid = document.getElementById("exams-grid");
    if (!grid) return;

    try {
        const res = await fetch("/api/exams");
        const data = await res.json();

        if (res.ok && data.success) {
            const exams = data.data || [];
            if (exams.length === 0) {
                grid.innerHTML = `<div class="full-width card" style="text-align: center; color: var(--gray);">No examinations are currently scheduled.</div>`;
                return;
            }

            grid.innerHTML = exams.map(exam => `
                <div class="exam-card">
                    <div class="exam-card-header">
                        <h3 class="exam-card-title">${escapeHtml(exam.examName)}</h3>
                        <span class="badge badge-${exam.registrationStatus.toLowerCase().replace('_', '-')}">${exam.registrationStatus}</span>
                    </div>
                    <p class="exam-card-meta">
                        <strong>Organization:</strong> ${escapeHtml(exam.conductingOrganization)}<br>
                        <strong>Exam Date:</strong> ${exam.examDate}<br>
                        <strong>Registration Window:</strong> ${exam.registrationStartDate} to ${exam.registrationEndDate}
                    </p>
                    <p style="font-size: 0.9rem; color: #4b5563; margin-bottom: 1rem; line-height: 1.4;">
                        ${escapeHtml(exam.description || 'No description provided.')}
                    </p>
                    <div class="exam-card-footer">
                        <a href="/exam-details.html?id=${exam.id}" class="btn btn-secondary btn-sm">View Details</a>
                        ${exam.canRegister
                            ? `<button onclick="registerForExam('${exam.id}')" class="btn btn-primary btn-sm">Register</button>`
                            : `<button class="btn btn-secondary btn-sm" disabled title="Registration ${exam.registrationStatus.toLowerCase()}">Closed</button>`
                        }
                    </div>
                </div>
            `).join("");
        }
    } catch (err) {
        console.error("Error loading exams:", err);
    }
}

async function loadExamDetails() {
    const urlParams = new URLSearchParams(window.location.search);
    const examId = urlParams.get("id");
    const container = document.getElementById("exam-details-container");
    const alertBox = document.getElementById("details-alert");

    if (!examId) {
        container.innerHTML = `<div class="alert alert-danger">No examination ID specified.</div>`;
        return;
    }

    try {
        const res = await fetch(`/api/exams/${examId}`);
        const data = await res.json();

        if (!res.ok || !data.success) {
            container.innerHTML = `<div class="alert alert-danger">${data.message || 'Exam not found.'}</div>`;
            return;
        }

        const exam = data.data;

        // Check if student profile is complete
        let profileCompleted = true;
        try {
            const authRes = await fetch("/api/auth/me");
            const authData = await authRes.json();
            if (authData.success && authData.data) {
                profileCompleted = authData.data.profileCompleted;
            }
        } catch (e) {
            // Ignore if not logged in
        }

        container.innerHTML = `
            <div class="card">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.25rem;">
                    <h2>${escapeHtml(exam.examName)}</h2>
                    <span class="badge badge-${exam.registrationStatus.toLowerCase().replace('_', '-')}">${exam.registrationStatus}</span>
                </div>
                <div class="form-grid" style="margin-bottom: 1.5rem;">
                    <div class="form-group">
                        <label>Conducting Organization</label>
                        <p style="font-size: 1rem; font-weight: 500;">${escapeHtml(exam.conductingOrganization)}</p>
                    </div>
                    <div class="form-group">
                        <label>Exam Date</label>
                        <p style="font-size: 1rem; font-weight: 500;">${exam.examDate}</p>
                    </div>
                    <div class="form-group">
                        <label>Registration Start Date</label>
                        <p style="font-size: 1rem; font-weight: 500;">${exam.registrationStartDate}</p>
                    </div>
                    <div class="form-group">
                        <label>Registration End Date</label>
                        <p style="font-size: 1rem; font-weight: 500;">${exam.registrationEndDate}</p>
                    </div>
                </div>

                <div style="margin-bottom: 1.75rem;">
                    <label style="font-size: 0.875rem; font-weight: 600; color: #374151;">Description</label>
                    <p style="font-size: 0.95rem; color: #4b5563; margin-top: 0.35rem; line-height: 1.6;">
                        ${escapeHtml(exam.description || 'No detailed instructions provided.')}
                    </p>
                </div>

                <div style="display: flex; gap: 12px; align-items: center;">
                    <a href="/exams.html" class="btn btn-secondary">Back to Examinations</a>
                    ${exam.canRegister
                        ? (profileCompleted
                            ? `<button id="btn-register-action" onclick="registerForExam('${exam.id}')" class="btn btn-primary">Register for Exam</button>`
                            : `<a href="/profile.html?status=incomplete" class="btn btn-warning" style="background:#f59e0b; color:white;">Complete Profile to Register</a>`
                          )
                        : `<button class="btn btn-secondary" disabled>Registration Unavailable (${exam.registrationStatus})</button>`
                    }
                </div>
            </div>
        `;

        if (urlParams.get("action") === "register" && exam.canRegister && profileCompleted) {
            registerForExam(exam.id);
        }

    } catch (err) {
        console.error("Error loading exam details:", err);
    }
}

async function registerForExam(examId) {
    const alertBox = document.getElementById("details-alert") || document.getElementById("page-alert");
    if (alertBox) alertBox.style.display = "none";

    try {
        const res = await fetch(`/api/exams/${examId}/register`, {
            method: "POST"
        });

        const data = await res.json();

        if (res.ok && data.success) {
            window.location.href = `/registration-success.html?id=${data.data.id}`;
        } else {
            if (alertBox) {
                alertBox.className = "alert alert-danger";
                alertBox.textContent = data.message || "Failed to register for exam.";
                alertBox.style.display = "block";
                window.scrollTo({ top: 0, behavior: "smooth" });
            } else {
                alert(data.message || "Failed to register for exam.");
            }
        }
    } catch (err) {
        alert("An error occurred during registration. Please try again.");
    }
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

