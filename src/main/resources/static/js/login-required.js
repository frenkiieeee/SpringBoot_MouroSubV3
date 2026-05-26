// Popup reutilizable para avisar al usuario que necesita iniciar sesion.
// Lo carga cualquier pagina que tenga forms protegidos y expone dos funciones globales:
//   - window.isLoggedIn(): true si hay sesion de Supabase en localStorage.
//   - window.requireLogin(): true si hay sesion; si no, abre el modal y devuelve false.
// La idea es que cada form llame a window.requireLogin() en su submit para decidir.
(function () {

  // Crea el modal solo una vez y lo inyecta al final del body.
  function ensureModal() {
    if (document.getElementById("loginRequiredModal")) return;
    const wrapper = document.createElement("div");
    wrapper.innerHTML = `
      <div class="modal fade" id="loginRequiredModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content" style="background:#0b1120; color:#fff; border:1px solid rgba(255,255,255,0.1);">
            <div class="modal-header" style="border-bottom-color: rgba(255,255,255,0.1);">
              <h5 class="modal-title"><i class="bi bi-lock"></i> Necesitas iniciar sesion</h5>
              <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
              <p class="mb-0">Para reservar o enviar formularios primero tienes que iniciar sesion con tu cuenta de MouroSub.</p>
            </div>
            <div class="modal-footer" style="border-top-color: rgba(255,255,255,0.1);">
              <button type="button" class="btn btn-outline-light" data-bs-dismiss="modal">Cancelar</button>
              <a href="/login" class="btn btn-primary"><i class="bi bi-box-arrow-in-right"></i> Iniciar sesion</a>
            </div>
          </div>
        </div>
      </div>`;
    document.body.appendChild(wrapper.firstElementChild);
  }

  window.isLoggedIn = function () {
    return Boolean(localStorage.getItem("supabaseUserId"));
  };

  window.requireLogin = function () {
    if (window.isLoggedIn()) return true;
    ensureModal();
    const modalEl = document.getElementById("loginRequiredModal");
    // Bootstrap.Modal hace falta para mostrarlo; si por lo que sea no esta cargado, redirigimos al login.
    if (window.bootstrap && bootstrap.Modal) {
      bootstrap.Modal.getOrCreateInstance(modalEl).show();
    } else {
      window.location.href = "/login";
    }
    return false;
  };
})();
