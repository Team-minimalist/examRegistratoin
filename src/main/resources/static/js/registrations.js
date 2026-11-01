// Student Registrations and Printable Confirmation Slip

document.addEventListener("DOMContentLoaded", async () => {
    const slipContainer = document.getElementById("confirmation-slip-container");
    if (slipContainer) {
        loadRegistrationSlip();
    } else {
        loadMyRegistrations();
    }
});

async function loadMyRegistrations() {
    const tbody = document.getElementById("my-registrations-tbody");
    if (!tbody) return;

    try {
        const res = await fetch("/api/registrations/me");
        if (res.status === 401) {
            window.location.href = "/login.html";
            return;
        }

        const data = await res.json();
        if (res.ok && data.success) {
            const list = data.data || [];
            if (list.length === 0) {
                tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: var(--gray); padding: 2rem;">You have not registered for any examinations yet.</td></tr>`;
                return;
            }

            tbody.innerHTML = list.map(reg => `
                <tr>
                    <td><strong>${escapeHtml(reg.registrationNumber)}</strong></td>
                    <td>${escapeHtml(reg.examName)}</td>
                    <td>${escapeHtml(reg.conductingOrganization)}</td>
                    <td>${reg.examDate || 'N/A'}</td>
                    <td><span class="badge badge-open">${reg.status}</span></td>
                    <td>
                        <a href="/registration-success.html?id=${reg.id}" class="btn btn-secondary btn-sm">View Slip / Print</a>
                    </td>
                </tr>
            `).join("");
        }
    } catch (err) {
        console.error("Error loading my registrations:", err);
    }
}

async function loadRegistrationSlip() {
    const urlParams = new URLSearchParams(window.location.search);
    const regId = urlParams.get("id");
    const container = document.getElementById("confirmation-slip-container");

    if (!regId) {
        container.innerHTML = `<div class="alert alert-danger">No registration record specified.</div>`;
        return;
    }

    try {
        const res = await fetch(`/api/registrations/${regId}`);
        const data = await res.json();

        if (!res.ok || !data.success) {
            container.innerHTML = `<div class="alert alert-danger">${data.message || 'Registration details not found.'}</div>`;
            return;
        }

        const reg = data.data;

        container.innerHTML = `
            <div class="confirmation-slip">
                <div class="slip-header">
                    <h2>Examination Registration Slip</h2>
                    <p style="color: var(--gray); font-size: 0.9rem; margin-top: 0.25rem;">Government / Institutional Examination Board</p>
                    <div style="margin-top: 1rem;">
                        <span class="badge badge-open" style="font-size: 0.9rem; padding: 0.4rem 1rem;">${reg.status}</span>
                    </div>
                </div>

                <div class="slip-details-grid">
                    <div class="slip-field">
                        <span class="field-label">Registration Number</span>
                        <span class="field-value" style="color: var(--primary); font-size: 1.25rem;">${escapeHtml(reg.registrationNumber)}</span>
                    </div>
                    <div class="slip-field">
                        <span class="field-label">Registration Date</span>
                        <span class="field-value">${reg.registeredAt ? reg.registeredAt.replace('T', ' ').substring(0, 19) : 'N/A'}</span>
                    </div>
                    <div class="slip-field">
                        <span class="field-label">Candidate Name</span>
                        <span class="field-value">${escapeHtml(reg.studentName)}</span>
                    </div>
                    <div class="slip-field">
                        <span class="field-label">Candidate Email</span>
                        <span class="field-value">${escapeHtml(reg.studentEmail)}</span>
                    </div>
                    <div class="slip-field full-width">
                        <span class="field-label">Examination Name</span>
                        <span class="field-value">${escapeHtml(reg.examName)}</span>
                    </div>
                    <div class="slip-field">
                        <span class="field-label">Conducting Organization</span>
                        <span class="field-value">${escapeHtml(reg.conductingOrganization)}</span>
                    </div>
                    <div class="slip-field">
                        <span class="field-label">Scheduled Exam Date</span>
                        <span class="field-value" style="color: #b91c1c;">${reg.examDate}</span>
                    </div>
                </div>

                <div style="font-size: 0.85rem; color: #64748b; background: #f8fafc; padding: 1rem; border-radius: 6px; margin-bottom: 2rem;">
                    <strong>Instructions for Candidate:</strong><br>
                    • Please preserve this registration slip for your records and examination entry.<br>
                    • Bring a valid government photo ID matching your registration details to the examination center.<br>
                    • Electronic devices and study materials are strictly prohibited.
                </div>

                <div class="slip-actions">
                    <button onclick="window.print()" class="btn btn-primary">Print Slip</button>
                    <a href="/my-registrations.html" class="btn btn-secondary">My Registrations</a>
                    <a href="/dashboard.html" class="btn btn-secondary">Back to Dashboard</a>
                </div>
            </div>
        `;
    } catch (err) {
        console.error("Error loading slip:", err);
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

