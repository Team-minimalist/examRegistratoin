// Admin Portal Logic

document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById("admin-stats-container")) {
        loadAdminStats();
    }
    if (document.getElementById("admin-exams-tbody")) {
        loadAdminExams();
    }
    if (document.getElementById("create-exam-form")) {
        initCreateEditExam();
    }
    if (document.getElementById("admin-students-tbody")) {
        loadAdminStudents();
    }
    if (document.getElementById("admin-registrations-tbody")) {
        loadAdminRegistrations();
    }
});

async function loadAdminStats() {
    try {
        const res = await fetch("/api/admin/stats");
        if (res.status === 403 || res.status === 401) {
            window.location.href = "/admin-login.html";
            return;
        }

        const data = await res.json();
        if (res.ok && data.success) {
            const stats = data.data;
            if (document.getElementById("stat-total-students")) {
                document.getElementById("stat-total-students").textContent = stats.totalStudents || 0;
            }
            if (document.getElementById("stat-active-students")) {
                document.getElementById("stat-active-students").textContent = stats.activeStudents || 0;
            }
            if (document.getElementById("stat-total-exams")) {
                document.getElementById("stat-total-exams").textContent = stats.totalExams || 0;
            }
            if (document.getElementById("stat-open-exams")) {
                document.getElementById("stat-open-exams").textContent = stats.activeExams || 0;
            }
            if (document.getElementById("stat-total-registrations")) {
                document.getElementById("stat-total-registrations").textContent = stats.totalRegistrations || 0;
            }
        }
    } catch (err) {
        console.error("Error loading admin stats:", err);
    }
}

async function loadAdminExams() {
    const tbody = document.getElementById("admin-exams-tbody");
    if (!tbody) return;

    try {
        const res = await fetch("/api/admin/exams");
        const data = await res.json();

        if (res.ok && data.success) {
            const exams = data.data || [];
            if (exams.length === 0) {
                tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: var(--gray); padding: 1.5rem;">No exams created yet.</td></tr>`;
                return;
            }

            tbody.innerHTML = exams.map(exam => {
                const examDateVal = exam.examinationDate || exam.examDate || 'N/A';
                const statusClass = (exam.registrationStatus || 'closed').toLowerCase().replace('_', '-');
                return `
                <tr>
                    <td><strong>${escapeHtml(exam.examName)}</strong><br><small style="color:var(--gray);">${escapeHtml(exam.examCode || '')}</small></td>
                    <td>${escapeHtml(exam.conductingOrganization)}</td>
                    <td>${examDateVal}</td>
                    <td>${exam.registrationStartDate} to ${exam.registrationEndDate}</td>
                    <td><span class="badge badge-${statusClass}">${exam.registrationStatus || 'UNKNOWN'}</span></td>
                    <td>
                        <button onclick="toggleExamStatus('${exam.id}')" class="btn btn-sm ${exam.active ? 'btn-secondary' : 'btn-success'}">
                            ${exam.active ? 'Deactivate' : 'Activate'}
                        </button>
                    </td>
                    <td>
                        <a href="/admin/create-exam.html?id=${exam.id}" class="btn btn-secondary btn-sm">Edit</a>
                        <button onclick="deleteExam('${exam.id}')" class="btn btn-danger btn-sm">Delete</button>
                    </td>
                </tr>
            `}).join("");
        }
    } catch (err) {
        console.error("Error loading exams:", err);
    }
}

async function toggleExamStatus(id) {
    if (!confirm("Are you sure you want to change this examination's status?")) return;
    try {
        const res = await fetch(`/api/admin/exams/${id}/status`, { method: "PATCH" });
        const data = await res.json();
        if (res.ok && data.success) {
            loadAdminExams();
        } else {
            alert(data.message || "Failed to update status");
        }
    } catch (err) {
        alert("Error updating status");
    }
}

async function deleteExam(id) {
    if (!confirm("Are you sure you want to delete this examination?")) return;
    try {
        const res = await fetch(`/api/admin/exams/${id}`, { method: "DELETE" });
        const data = await res.json();
        if (res.ok && data.success) {
            loadAdminExams();
        } else {
            alert(data.message || "Failed to delete examination");
        }
    } catch (err) {
        alert("Error deleting examination");
    }
}

async function initCreateEditExam() {
    const urlParams = new URLSearchParams(window.location.search);
    const examId = urlParams.get("id");
    const titleHeader = document.getElementById("form-title");
    const form = document.getElementById("create-exam-form");
    const alertBox = document.getElementById("admin-exam-alert");

    if (examId) {
        if (titleHeader) titleHeader.textContent = "Edit Examination";
        try {
            const res = await fetch(`/api/exams/${examId}`);
            const data = await res.json();
            if (res.ok && data.success) {
                const e = data.data;
                document.getElementById("examName").value = e.examName || "";
                if (document.getElementById("examCode")) document.getElementById("examCode").value = e.examCode || "";
                document.getElementById("description").value = e.description || "";
                document.getElementById("conductingOrganization").value = e.conductingOrganization || "";
                const dateVal = e.examinationDate || e.examDate || "";
                if (document.getElementById("examDate")) document.getElementById("examDate").value = dateVal;
                if (document.getElementById("examinationDate")) document.getElementById("examinationDate").value = dateVal;
                document.getElementById("registrationStartDate").value = e.registrationStartDate || "";
                document.getElementById("registrationEndDate").value = e.registrationEndDate || "";
                if (document.getElementById("active")) document.getElementById("active").checked = e.active;
            }
        } catch (err) {
            console.error(err);
        }
    }

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        alertBox.style.display = "none";

        const examDateField = document.getElementById("examinationDate") || document.getElementById("examDate");

        const payload = {
            examName: document.getElementById("examName").value.trim(),
            description: document.getElementById("description").value.trim(),
            conductingOrganization: document.getElementById("conductingOrganization").value.trim(),
            examinationDate: examDateField ? examDateField.value : "",
            registrationStartDate: document.getElementById("registrationStartDate").value,
            registrationEndDate: document.getElementById("registrationEndDate").value,
            active: document.getElementById("active") ? document.getElementById("active").checked : true
        };

        if (document.getElementById("examCode") && document.getElementById("examCode").value.trim()) {
            payload.examCode = document.getElementById("examCode").value.trim();
        }

        const method = examId ? "PUT" : "POST";
        const url = examId ? `/api/admin/exams/${examId}` : "/api/admin/exams";

        try {
            const res = await fetch(url, {
                method: method,
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            const data = await res.json();
            if (res.ok && data.success) {
                alertBox.className = "alert alert-success";
                alertBox.textContent = examId ? "Examination updated successfully!" : "Examination created successfully!";
                alertBox.style.display = "block";
                setTimeout(() => {
                    window.location.href = "/admin/exams.html";
                }, 1000);
            } else {
                alertBox.className = "alert alert-danger";
                alertBox.textContent = data.message || "Failed to save examination.";
                alertBox.style.display = "block";
            }
        } catch (err) {
            alertBox.className = "alert alert-danger";
            alertBox.textContent = "Error saving examination.";
            alertBox.style.display = "block";
        }
    });
}

async function loadAdminStudents() {
    const tbody = document.getElementById("admin-students-tbody");
    if (!tbody) return;

    try {
        const res = await fetch("/api/admin/students");
        const data = await res.json();

        if (res.ok && data.success) {
            const students = data.data || [];
            if (students.length === 0) {
                tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: var(--gray); padding: 1.5rem;">No registered students yet.</td></tr>`;
                return;
            }

            tbody.innerHTML = students.map(s => `
                <tr>
                    <td><strong>${escapeHtml(s.name)}</strong></td>
                    <td>${escapeHtml(s.username)}</td>
                    <td>${escapeHtml(s.email)}</td>
                    <td><span class="badge ${s.profileCompleted ? 'badge-open' : 'badge-not-started'}">${s.profileCompleted ? 'Complete' : 'Incomplete'}</span></td>
                    <td>${s.authProvider || 'LOCAL'}</td>
                    <td><span class="badge ${s.active ? 'badge-open' : 'badge-closed'}">${s.active ? 'Active' : 'Disabled'}</span></td>
                    <td>
                        <button onclick="toggleStudentStatus('${s.id}')" class="btn btn-sm ${s.active ? 'btn-secondary' : 'btn-success'}">
                            ${s.active ? 'Disable' : 'Enable'}
                        </button>
                    </td>
                </tr>
            `).join("");
        }
    } catch (err) {
        console.error("Error loading students:", err);
    }
}

async function toggleStudentStatus(id) {
    if (!confirm("Are you sure you want to toggle this student's account status?")) return;
    try {
        const res = await fetch(`/api/admin/students/${id}/status`, { method: "PATCH" });
        const data = await res.json();
        if (res.ok && data.success) {
            loadAdminStudents();
        } else {
            alert(data.message || "Failed to update status");
        }
    } catch (err) {
        alert("Error updating student status");
    }
}

async function loadAdminRegistrations() {
    const tbody = document.getElementById("admin-registrations-tbody");
    if (!tbody) return;

    try {
        const res = await fetch("/api/admin/registrations");
        const data = await res.json();

        if (res.ok && data.success) {
            const registrations = data.data || [];
            window._allRegistrations = registrations;
            renderAdminRegistrations(registrations);
        }
    } catch (err) {
        console.error("Error loading registrations:", err);
    }
}

function renderAdminRegistrations(list) {
    const tbody = document.getElementById("admin-registrations-tbody");
    if (!tbody) return;

    if (list.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: var(--gray); padding: 1.5rem;">No registrations found.</td></tr>`;
        return;
    }

    tbody.innerHTML = list.map(reg => {
        const dateVal = reg.examinationDate || reg.examDate || 'N/A';
        return `
        <tr>
            <td><strong>${escapeHtml(reg.registrationNumber)}</strong></td>
            <td>${escapeHtml(reg.studentName)}</td>
            <td>${escapeHtml(reg.studentEmail)}</td>
            <td>${escapeHtml(reg.examName)}</td>
            <td>${dateVal}</td>
            <td>${reg.registeredAt ? reg.registeredAt.substring(0, 10) : 'N/A'}</td>
            <td><span class="badge badge-open">${reg.status}</span></td>
        </tr>
    `}).join("");
}

function filterRegistrations() {
    const query = document.getElementById("reg-search").value.toLowerCase();
    if (!window._allRegistrations) return;
    const filtered = window._allRegistrations.filter(r =>
        (r.registrationNumber && r.registrationNumber.toLowerCase().includes(query)) ||
        (r.studentName && r.studentName.toLowerCase().includes(query)) ||
        (r.studentEmail && r.studentEmail.toLowerCase().includes(query)) ||
        (r.examName && r.examName.toLowerCase().includes(query))
    );
    renderAdminRegistrations(filtered);
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
