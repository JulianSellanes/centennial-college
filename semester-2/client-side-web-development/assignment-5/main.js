const form = document.getElementById("form");
const successMsg = document.getElementById("success-msg");

const regex = {
    phone: /^(\(\d{3}\)\s?|\d{3}[-\s]?)\d{3}[-\s]?\d{4}$/,
    postal: /^[A-Za-z]\d[A-Za-z][ -]?\d[A-Za-z]\d$/
};

function displayError(_input, _msg) {
    const error = _input.parentElement.querySelector(".error");
    error.textContent = _msg;
    error.style.display = "block";
    _input.classList.add("invalid");
}

function clearErrors() {
    document.querySelectorAll(".error").forEach(x => x.style.display = "none");
    document.querySelectorAll(".invalid").forEach(x => x.classList.remove("invalid"));
}

async function handleSubmit(e) {
    e.preventDefault();
    clearErrors();

    let check = true;
    const fullName = form.fullName;
    const email = form.email;
    const phone = form.phone;
    const postal = form.postal;
    const topic = form.topic;
    const experience = form.querySelector("input[name='experience']:checked");

    if(!fullName.value.trim()) {
        displayError(fullName, "Full name is required");
        check = false;
    }

    if(!email.value.trim()) {
        displayError(email, "Email is required");
        check = false;
    }

    if(!regex.phone.test(phone.value)) {
        displayError(phone, "Invalid phone number format");
        check = false;
    }

    if(!regex.postal.test(postal.value)) {
        displayError(postal, "Invalid postal code format");
        check = false;
    }

    if(!topic.value) {
        displayError(topic, "Please select a topic");
        check = false;
    }

    if(!experience) {
        const radioLegend = form.querySelector("fieldset");
        displayError(radioLegend, "Please select an experience level");
        check = false;
    }

    if(!check) return;

    const formData = {
        fullName: fullName.value,
        email: email.value,
        phone: phone.value,
        postal: postal.value,
        topic: topic.value,
        experience: experience.value,
        comment: form.comments.value
    };

    try {
        const response = await fetch("https://jsonplaceholder.typicode.com/posts", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(formData)
        });

        const data = await response.json();
        console.log("Response:", data);

        successMsg.textContent = "Registration successful 👍";
        successMsg.style.color = "green";

        form.reset();
    } catch (error) {
        console.error("Error:", error);
        
        successMsg.textContent = "An error occurred ❌";
        successMsg.style.color = "red";
    }
}

form.addEventListener("submit", handleSubmit);