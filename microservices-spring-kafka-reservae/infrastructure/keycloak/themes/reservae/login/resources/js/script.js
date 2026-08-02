document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("#kc-form-login");
    const passwordInput = document.querySelector("#password");
    const toggleButton = document.querySelector("[data-password-toggle]");
    const visibleIcon = document.querySelector("[data-eye-visible]");
    const hiddenIcon = document.querySelector("[data-eye-hidden]");
    const submitButton = document.querySelector("#kc-login");

    if (passwordInput && toggleButton) {
        toggleButton.addEventListener("click", () => {
            const passwordIsVisible = passwordInput.type === "text";

            passwordInput.type = passwordIsVisible ? "password" : "text";

            toggleButton.setAttribute(
                "aria-label",
                passwordIsVisible ? "Mostrar senha" : "Ocultar senha"
            );

            visibleIcon?.classList.toggle("is-hidden", !passwordIsVisible);
            hiddenIcon?.classList.toggle("is-hidden", passwordIsVisible);

            passwordInput.focus();
        });
    }

    if (form && submitButton) {
        form.addEventListener("submit", () => {
            submitButton.disabled = true;
            submitButton.textContent = "Entrando...";
        });
    }
});