// comentario: script del header ajustado por mi para manejo de session local y UI
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
    const misReservasLink = document.getElementById("misReservasLink");

    // El enlace "Mis reservas" del dropdown. Como el endpoint que lista las reservas
    // necesita el id interno (Long) y no el de Supabase, al hacer click consultamos
    // primero /auth/profile para traducirlo y despues redirigimos.
    if (misReservasLink) {
      misReservasLink.addEventListener("click", function (e) {
        if (!storedUserId) return; // sin sesion el server devolvera lo que toque
        e.preventDefault();
        fetch("/auth/profile?supabaseUserId=" + encodeURIComponent(storedUserId))
          .then(function (r) { return r.ok ? r.json() : null; })
          .then(function (profile) {
            if (profile && profile.idUsuario) {
              window.location.href = "/reservas/mis-reservas/" + profile.idUsuario;
            } else {
              window.location.href = "/login";
            }
          });
      });
    }

    function toggleSessionUi(logged) {
      // El boton de "Iniciar sesion" y el menu de usuario solo aparecen en escritorio
      // (>=768px). En movil ambos viven dentro del menu hamburguesa, por eso mantenemos
      // siempre d-none y solo togglemos d-md-inline-flex para que aparezcan en escritorio.
      if (loginLink) {
        loginLink.classList.add("d-none");
        if (logged) {
          loginLink.classList.remove("d-md-inline-flex");
        } else {
          loginLink.classList.add("d-md-inline-flex");
        }
      }
      if (userMenu) {
        userMenu.hidden = !logged;
        userMenu.classList.add("d-none");
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

    // Cerrar el menu hamburguesa cuando el usuario pulsa cualquier enlace dentro de el.
    // Sin esto el menu se queda abierto encima del contenido al volver a la pagina.
    const menuMovil = document.getElementById("menuMovil");
    if (menuMovil && window.bootstrap && window.bootstrap.Collapse) {
      menuMovil.querySelectorAll("a").forEach(function (a) {
        a.addEventListener("click", function () {
          const inst = window.bootstrap.Collapse.getInstance(menuMovil);
          if (inst) inst.hide();
        });
      });
    }
  }

  window.initHeaderSession = initHeaderSession;
})();
