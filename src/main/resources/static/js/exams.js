// Examination list, details, and registration warning handling

let currentPendingExamId = null;

document.addEventListener("DOMContentLoaded", async () => {
    ensureWarningModal();

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

            grid.innerHTML = exams.map(exam => {
                const dateVal = exam.examinationDate || exam.examDate || 'TBA';
                const statusClass = (exam.registrationStatus || 'closed').toLowerCase().replace('_', '-');
                const safeName = escapeAttr(exam.examName || '');
                const safeCode = escapeAttr(exam.examCode || '');
                const safeDate = escapeAttr(dateVal);

                return `
                <div class="exam-card">
                    <div class="exam-card-header">
                        <div>
                            <h3 class="exam-card-title">${escapeHtml(exam.examName)}</h3>
                            <small style="color:var(--gray);">${escapeHtml(exam.examCode || '')}</small>
                        </div>
                        <span class="badge badge-${statusClass}">${exam.registrationStatus}</span>
                    </div>
                    <p class="exam-card-meta">
                        <strong>Organization:</strong> ${escapeHtml(exam.conductingOrganization)}<br>
                        <strong>Exam Date:</strong> ${dateVal}<br>
                        <strong>Registration Window:</strong> ${exam.registrationStartDate} to ${exam.registrationEndDate}
                    </p>
                    <p style="font-size: 0.9rem; color: #4b5563; margin-bottom: 1rem; line-height: 1.4;">
                        ${escapeHtml(exam.description || 'No description provided.')}
                    </p>
                    <div class="exam-card-footer">
                        <a href="/exam-details.html?id=${exam.id}" class="btn btn-secondary btn-sm">View Details</a>
                        ${exam.canRegister
                            ? `<button onclick="promptRegistrationWarning('${exam.id}', '${safeName}', '${safeCode}', '${safeDate}')" class="btn btn-primary btn-sm">Register</button>`
                            : `<button class="btn btn-secondary btn-sm" disabled title="Registration ${exam.registrationStatus.toLowerCase()}">Closed</button>`
                        }
                    </div>
                </div>
            `}).join("");
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
        const dateVal = exam.examinationDate || exam.examDate || 'TBA';
        const safeName = escapeAttr(exam.examName || '');
        const safeCode = escapeAttr(exam.examCode || '');
        const safeDate = escapeAttr(dateVal);

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

        const statusClass = (exam.registrationStatus || 'closed').toLowerCase().replace('_', '-');

        container.innerHTML = `
            <div class="card">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.25rem;">
                    <div>
                        <h2>${escapeHtml(exam.examName)}</h2>
                        <span style="color:var(--gray); font-size:0.9rem;">Code: <strong>${escapeHtml(exam.examCode || 'N/A')}</strong></span>
                    </div>
                    <span class="badge badge-${statusClass}">${exam.registrationStatus}</span>
                </div>
                <div class="form-grid" style="margin-bottom: 1.5rem;">
                    <div class="form-group">
                        <label>Conducting Organization</label>
                        <p style="font-size: 1rem; font-weight: 500;">${escapeHtml(exam.conductingOrganization)}</p>
                    </div>
                    <div class="form-group">
                        <label>Exam Date</label>
                        <p style="font-size: 1rem; font-weight: 500;">${dateVal}</p>
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

                <div style="margin-bottom: 1.25rem;">
                    <label style="font-size: 0.875rem; font-weight: 600; color: #374151;">Description</label>
                    <p style="font-size: 0.95rem; color: #4b5563; margin-top: 0.35rem; line-height: 1.6;">
                        ${escapeHtml(exam.description || 'No detailed instructions provided.')}
                    </p>
                </div>

                <!-- Registration Warning Box -->
                <div class="warning-box">
                    <div class="warning-box-header">
                        <span>⚠️</span> Important Examination Registration Warning & Advisory
                    </div>
                    <div class="warning-box-body">
                        Please review your candidate details thoroughly before registering:
                        <ul class="warning-box-list">
                            <li><strong>Permanent Record:</strong> Your full name, date of birth, and identification details from your profile will be permanently stamped on your examination admit card and registration certificate.</li>
                            <li><strong>Strict Single Registration:</strong> You may register only once for this examination cycle. Duplicate registration attempts will be automatically rejected.</li>
                            <li><strong>Non-Transferable & Final:</strong> Once registered, examination entries cannot be modified, transferred, or retracted.</li>
                            <li><strong>Capacity & Deadline:</strong> Registration closes automatically on <strong>${exam.registrationEndDate}</strong> or when maximum candidate capacity is reached.</li>
                        </ul>
                    </div>
                </div>

                <div style="display: flex; gap: 12px; align-items: center; margin-top: 1.5rem;">
                    <a href="/exams.html" class="btn btn-secondary">Back to Examinations</a>
                    ${exam.canRegister
                        ? (profileCompleted
                            ? `<button id="btn-register-action" onclick="promptRegistrationWarning('${exam.id}', '${safeName}', '${safeCode}', '${safeDate}')" class="btn btn-primary">Register for Exam</button>`
                            : `<a href="/profile.html?status=incomplete" class="btn btn-warning" style="background:#f59e0b; color:white;">Complete Profile to Register</a>`
                          )
                        : `<button class="btn btn-secondary" disabled>Registration Unavailable (${exam.registrationStatus})</button>`
                    }
                </div>
            </div>
        `;

        if (urlParams.get("action") === "register" && exam.canRegister && profileCompleted) {
            promptRegistrationWarning(exam.id, exam.examName, exam.examCode, dateVal);
        }

    } catch (err) {
        console.error("Error loading exam details:", err);
    }
}

// Create and insert modal into DOM if not present
function ensureWarningModal() {
    if (document.getElementById("registration-warning-modal")) return;

    const modalHtml = `
        <div id="registration-warning-modal" class="modal-overlay" role="dialog" aria-modal="true" aria-labelledby="modal-warning-title">
            <div class="modal-dialog">
                <div class="modal-header">
                    <h3 id="modal-warning-title"><span>⚠️</span> Confirm Examination Registration</h3>
                    <button type="button" class="modal-close-btn" onclick="closeRegistrationWarningModal()" aria-label="Close">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="modal-exam-summary">
                        <p><strong>Examination:</strong> <span id="modal-exam-name"></span></p>
                        <p><strong>Code:</strong> <span id="modal-exam-code"></span> &nbsp;|&nbsp; <strong>Date:</strong> <span id="modal-exam-date"></span></p>
                    </div>

                    <div class="warning-box" style="margin: 0;">
                        <div class="warning-box-header" style="font-size: 0.95rem;">
                            <span>⚠️</span> Candidate Declaration & Advisory
                        </div>
                        <div class="warning-box-body" style="font-size: 0.85rem;">
                            <ul class="warning-box-list" style="margin-left: 1rem;">
                                <li>I confirm that my personal and academic profile details are accurate and verified.</li>
                                <li>I understand that this registration is final and cannot be modified or re-submitted.</li>
                                <li>I acknowledge that duplicate registration attempts will be automatically rejected.</li>
                            </ul>
                        </div>
                    </div>

                    <label class="warning-declaration-label">
                        <input type="checkbox" id="warning-ack-checkbox" onchange="toggleConfirmButton(this.checked)">
                        <span>I have read and agree to all examination instructions, and confirm that all submitted profile details are accurate.</span>
                    </label>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeRegistrationWarningModal()">Cancel</button>
                    <button type="button" id="btn-modal-confirm-register" class="btn btn-primary" disabled onclick="executeConfirmedRegistration()">Confirm & Register</button>
                </div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML("beforeend", modalHtml);

    // Close on backdrop click
    const modal = document.getElementById("registration-warning-modal");
    modal.addEventListener("click", (e) => {
        if (e.target === modal) closeRegistrationWarningModal();
    });

    // Close on Escape key
    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape" && modal.classList.contains("active")) {
            closeRegistrationWarningModal();
        }
    });
}

// Display warning modal
function promptRegistrationWarning(examId, examName, examCode, examDate) {
    ensureWarningModal();
    currentPendingExamId = examId;

    document.getElementById("modal-exam-name").textContent = examName || "Selected Examination";
    document.getElementById("modal-exam-code").textContent = examCode || "N/A";
    document.getElementById("modal-exam-date").textContent = examDate || "TBA";

    // Reset checkbox and confirm button
    const checkbox = document.getElementById("warning-ack-checkbox");
    checkbox.checked = false;
    const confirmBtn = document.getElementById("btn-modal-confirm-register");
    confirmBtn.disabled = true;
    confirmBtn.textContent = "Confirm & Register";

    const modal = document.getElementById("registration-warning-modal");
    modal.classList.add("active");
}

function closeRegistrationWarningModal() {
    const modal = document.getElementById("registration-warning-modal");
    if (modal) modal.classList.remove("active");
    currentPendingExamId = null;
}

function toggleConfirmButton(isChecked) {
    const btn = document.getElementById("btn-modal-confirm-register");
    if (btn) btn.disabled = !isChecked;
}

async function executeConfirmedRegistration() {
    if (!currentPendingExamId) return;

    const btn = document.getElementById("btn-modal-confirm-register");
    if (btn) {
        btn.disabled = true;
        btn.textContent = "Registering...";
    }

    const examId = currentPendingExamId;
    closeRegistrationWarningModal();
    await registerForExam(examId);
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

function escapeAttr(str) {
    if (!str) return "";
    return String(str).replace(/'/g, "\\'").replace(/"/g, '&quot;');
}
