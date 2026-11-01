// Authentication logic for login and signup

document.addEventListener("DOMContentLoaded", () => {
    // Check URL query parameters for login page messages
    const urlParams = new URLSearchParams(window.location.search);
    const alertBox = document.getElementById("alert-message");

    if (alertBox) {
        if (urlParams.has("error")) {
            alertBox.className = "alert alert-danger";
            alertBox.textContent = "Invalid username or password. Please try again.";
            alertBox.style.display = "block";
        } else if (urlParams.has("logout")) {
            alertBox.className = "alert alert-success";
            alertBox.textContent = "You have been logged out successfully.";
            alertBox.style.display = "block";
        } else if (urlParams.get("status") === "registered") {
            alertBox.className = "alert alert-success";
            alertBox.textContent = "Registration successful! You can now log in.";
            alertBox.style.display = "block";
        }
    }

    // Handle student signup submission
    const signupForm = document.getElementById("signup-form");
    if (signupForm) {
        signupForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const msgBox = document.getElementById("signup-alert");
            msgBox.style.display = "none";

            const name = document.getElementById("name").value.trim();
            const username = document.getElementById("username").value.trim();
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value;
            const confirmPassword = document.getElementById("confirmPassword").value;

            if (password !== confirmPassword) {
                msgBox.className = "alert alert-danger";
                msgBox.textContent = "Passwords do not match.";
                msgBox.style.display = "block";
                return;
            }

            if (password.length < 6) {
                msgBox.className = "alert alert-danger";
                msgBox.textContent = "Password must be at least 6 characters.";
                msgBox.style.display = "block";
                return;
            }

            try {
                const response = await fetch("/api/auth/register", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ name, username, email, password })
                });

                const data = await response.json();

                if (response.ok && data.success) {
                    window.location.href = "/login.html?status=registered";
                } else {
                    msgBox.className = "alert alert-danger";
                    msgBox.textContent = data.message || "Failed to register. Please check your details.";
                    msgBox.style.display = "block";
                }
            } catch (err) {
                msgBox.className = "alert alert-danger";
                msgBox.textContent = "An error occurred connecting to server. Please try again.";
                msgBox.style.display = "block";
            }
        });
    }
});

