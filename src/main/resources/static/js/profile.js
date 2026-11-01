// Student Profile handling

document.addEventListener("DOMContentLoaded", async () => {
    const alertBox = document.getElementById("profile-alert");
    const form = document.getElementById("profile-form");
    const urlParams = new URLSearchParams(window.location.search);

    if (urlParams.get("status") === "incomplete" && alertBox) {
        alertBox.className = "alert alert-warning";
        alertBox.textContent = "Please complete your required student profile before continuing to exam registration.";
        alertBox.style.display = "block";
    }

    try {
        const res = await fetch("/api/students/me");
        if (res.status === 401) {
            window.location.href = "/login.html";
            return;
        }

        const data = await res.json();
        if (data.success && data.data) {
            const student = data.data;

            document.getElementById("name").value = student.name || "";
            document.getElementById("email").value = student.email || "";
            document.getElementById("username").value = student.username || "";

            if (student.dateOfBirth) {
                document.getElementById("dateOfBirth").value = student.dateOfBirth;
            }
            if (student.identificationType) {
                document.getElementById("identificationType").value = student.identificationType;
            }
            if (student.identificationNumber) {
                document.getElementById("identificationNumber").value = student.identificationNumber;
            }

            if (student.academicDetails) {
                document.getElementById("institutionType").value = student.academicDetails.institutionType || "";
                document.getElementById("institutionName").value = student.academicDetails.institutionName || "";
                document.getElementById("className").value = student.academicDetails.className || "";
                document.getElementById("stream").value = student.academicDetails.stream || "";
                document.getElementById("department").value = student.academicDetails.department || "";
                document.getElementById("registrationNumber").value = student.academicDetails.registrationNumber || "";
                document.getElementById("rollNumber").value = student.academicDetails.rollNumber || "";
            }

            if (student.address) {
                document.getElementById("address").value = student.address.address || "";
                document.getElementById("city").value = student.address.city || "";
                document.getElementById("state").value = student.address.state || "";
                document.getElementById("pincode").value = student.address.pincode || "";
            }

            const statusBadge = document.getElementById("completion-badge");
            if (statusBadge) {
                if (student.profileCompleted) {
                    statusBadge.className = "badge badge-open";
                    statusBadge.textContent = "Profile Complete";
                } else {
                    statusBadge.className = "badge badge-closed";
                    statusBadge.textContent = "Profile Incomplete";
                }
            }
        }
    } catch (e) {
        console.error("Error loading profile:", e);
    }

    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            alertBox.style.display = "none";

            const payload = {
                name: document.getElementById("name").value.trim(),
                dateOfBirth: document.getElementById("dateOfBirth").value,
                identificationType: document.getElementById("identificationType").value,
                identificationNumber: document.getElementById("identificationNumber").value.trim(),
                academicDetails: {
                    institutionType: document.getElementById("institutionType").value.trim(),
                    institutionName: document.getElementById("institutionName").value.trim(),
                    className: document.getElementById("className").value.trim(),
                    stream: document.getElementById("stream").value.trim(),
                    department: document.getElementById("department").value.trim(),
                    registrationNumber: document.getElementById("registrationNumber").value.trim(),
                    rollNumber: document.getElementById("rollNumber").value.trim()
                },
                address: {
                    address: document.getElementById("address").value.trim(),
                    city: document.getElementById("city").value.trim(),
                    state: document.getElementById("state").value.trim(),
                    pincode: document.getElementById("pincode").value.trim()
                }
            };

            try {
                const updateRes = await fetch("/api/students/me", {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(payload)
                });

                const result = await updateRes.json();

                if (updateRes.ok && result.success) {
                    alertBox.className = "alert alert-success";
                    alertBox.textContent = "Profile updated successfully! Redirecting to dashboard...";
                    alertBox.style.display = "block";
                    setTimeout(() => {
                        window.location.href = "/dashboard.html";
                    }, 1200);
                } else {
                    alertBox.className = "alert alert-danger";
                    alertBox.textContent = result.message || "Failed to update profile. Please verify your details.";
                    alertBox.style.display = "block";
                }
            } catch (err) {
                alertBox.className = "alert alert-danger";
                alertBox.textContent = "An error occurred while saving profile.";
                alertBox.style.display = "block";
            }
        });
    }
});

