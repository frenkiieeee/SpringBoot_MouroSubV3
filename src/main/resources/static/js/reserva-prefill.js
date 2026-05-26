(function () {
  function normalize(text) {
    return (text || "")
      .toLowerCase()
      .normalize("NFD")
      .replace(/[\u0300-\u036f]/g, "")
      .trim();
  }

  function detectTipoFromHref(href) {
    if (!href) return "";
    if (href.includes("/reservas/inmersiones")) return "inmersiones";
    if (href.includes("/reservas/actividades")) return "actividades";
    if (href.includes("/reservas/cursos")) return "cursos";
    return "";
  }

  function categoriaCursosDesdePathname() {
    const p = (window.location.pathname || "").toLowerCase();
    if (!p.includes("/page/cursos/")) return "";
    if (p.includes("/apnea")) return "Apnea";
    if (p.includes("/deportivos")) return "Buceo";
    if (p.includes("/emergencia")) return "Rescate";
    if (p.includes("/profesionales")) return "Profesional";
    if (p.includes("/tecnicos")) return "Especialidad";
    return "";
  }

  function firstText(container, selectors) {
    for (const sel of selectors) {
      const el = container.querySelector(sel);
      if (el && el.textContent && el.textContent.trim()) {
        return el.textContent.trim();
      }
    }
    return "";
  }

  function fallbackNombreDesdeContexto(link, card) {
    const fromAlt = (card.querySelector("img[alt]") || {}).alt || "";
    if (fromAlt.trim()) return fromAlt.trim();

    const modal = link.closest(".modal-content") || link.closest(".modal");
    if (modal) {
      const modalTitle =
        firstText(modal, [".modal-title", "h1", "h2", "h3", "h4", "h5"]) || "";
      if (modalTitle.trim()) return modalTitle.trim();
      const modalAlt = (modal.querySelector("img[alt]") || {}).alt || "";
      if (modalAlt.trim()) return modalAlt.trim();
    }
    return "";
  }

  function nearestCard(el) {
    return (
      el.closest("[data-reserva-card]") ||
      el.closest(".actividad-card") ||
      el.closest(".card") ||
      el.closest(".modal-content") ||
      el.closest("article") ||
      el.closest(".col-12") ||
      document
    );
  }

  document.addEventListener("click", function (event) {
    const link = event.target.closest("a[href]");
    if (!link) return;

    const href = link.getAttribute("href") || "";
    const tipo = detectTipoFromHref(href);
    if (!tipo) return;

    // Si ya viene con id explícita, no necesitamos deducir nada adicional.
    if (href.includes("?id=") || href.includes("&id=")) return;

    const card = nearestCard(link);
    let nombre = firstText(card, [
      "[data-reserva-nombre]",
      ".actividad-nombre",
      ".card-title",
      ".actividad-info h3",
      "h3",
      "h2",
    ]);
    if (!nombre) {
      nombre = fallbackNombreDesdeContexto(link, card);
    }
    let categoria = firstText(card, [
      "[data-reserva-categoria]",
      ".actividad-tag",
      ".badge",
      ".actividad-tipo",
    ]);
    if (!categoria && tipo === "cursos") {
      categoria = categoriaCursosDesdePathname();
    }

    if (!nombre) return;

    const payload = {
      tipo,
      nombre,
      categoria,
      at: Date.now(),
    };
    localStorage.setItem("reservaPrefill", JSON.stringify(payload));
  });

  // Utilidad global para la vista de reservas.
  window.readReservaPrefill = function () {
    try {
      const raw = localStorage.getItem("reservaPrefill");
      if (!raw) return null;
      const parsed = JSON.parse(raw);
      if (!parsed || !parsed.tipo) return null;
      return {
        tipo: parsed.tipo,
        nombre: parsed.nombre || "",
        categoria: parsed.categoria || "",
        at: parsed.at || 0,
        normalize,
      };
    } catch (_) {
      return null;
    }
  };

  window.clearReservaPrefill = function () {
    localStorage.removeItem("reservaPrefill");
  };
})();
