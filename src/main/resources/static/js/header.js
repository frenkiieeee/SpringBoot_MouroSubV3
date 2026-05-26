(function () {
  function initHeaderSession() {
    // Gestion simple de UI en modo estatico.
    const storedEmail = localStorage.getItem("supabaseUserEmail") || "";
    const storedUserId = localStorage.getItem("supabaseUserId") || "";
    const isAdmin = localStorage.getItem("supabaseIsAdmin") === "true";
    const isLogged = Boolean(storedEmail || storedUserId);

    const loginLink = document.getElementById("loginLink");
    const userMenu = document.getElementById("userMenu");
    const logoutBtn = document.getElementById("logoutBtn");
    const loginLinkMobile = document.getElementById("loginLinkMobile");
    const logoutBtnMobile = document.getElementById("logoutBtnMobile");
    const accountLinkMobile = document.getElementById("accountLinkMobile");
    const adminLink = document.getElementById("adminLink");
    const adminLinkMobile = document.getElementById("adminLinkMobile");

    function toggleSessionUi(logged) {
      if (loginLink) {
        if (logged) {
          loginLink.classList.add("d-none");
          loginLink.classList.remove("d-md-inline-flex");
        } else {
          loginLink.classList.remove("d-none");
          loginLink.classList.add("d-md-inline-flex");
        }
      }
      if (userMenu) {
        userMenu.hidden = !logged;
        if (logged) {
          userMenu.classList.add("d-md-inline-flex");
        } else {
          userMenu.classList.remove("d-md-inline-flex");
        }
      }
      if (loginLinkMobile) loginLinkMobile.classList.toggle("d-none", logged);
      if (logoutBtnMobile) {
        logoutBtnMobile.hidden = !logged;
        logoutBtnMobile.classList.toggle("d-none", !logged);
      }
      if (accountLinkMobile) {
        accountLinkMobile.hidden = !logged;
        accountLinkMobile.classList.toggle("d-none", !logged);
      }
      if (adminLink) adminLink.classList.toggle("d-none", !isAdmin);
      if (adminLinkMobile) {
        adminLinkMobile.hidden = !isAdmin;
        adminLinkMobile.classList.toggle("d-none", !isAdmin);
      }
    }

    toggleSessionUi(Boolean(isLogged));

    function handleLogout(event) {
      event.preventDefault();
      // Solo limpiamos el estado local en modo estatico.
      localStorage.removeItem("supabaseUserId");
      localStorage.removeItem("supabaseUserEmail");
      localStorage.removeItem("supabaseIsAdmin");
      document.cookie = "sb_access_token=; path=/; max-age=0";
      window.location.href = "/login";
    }

    if (logoutBtn) logoutBtn.addEventListener("click", handleLogout);
    if (logoutBtnMobile) logoutBtnMobile.addEventListener("click", handleLogout);
  }

  window.initHeaderSession = initHeaderSession;
})();
