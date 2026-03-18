(function () {
  "use strict";

  var path = window.location.pathname.replace(/\\/g, "/").toLowerCase();
  var isNestedPage = /\/(magazine|workshop)\/[^/]+\.html$/.test(path);
  var root = isNestedPage ? "../" : "";

  function isCurrent(page) {
    if (page === "index") return /\/index\.html$/.test(path) || path.endsWith("/");
    if (page === "brands") return /\/brands\.html$/.test(path);
    if (page === "workshop") return /\/workshop\.html$/.test(path);
    if (page === "magazine") return /\/magazine\.html$/.test(path) || /\/magazine\/\d+\.html$/.test(path);
    if (page === "about") return /\/about\.html$/.test(path);
    return false;
  }

  function navItem(page, label, href) {
    if (isCurrent(page)) return "<li>" + label + "</li>";
    return "<li><a href=\"" + href + "\">" + label + "</a></li>";
  }

  var navHtml = [
    navItem("brands", "ブランド", root + "brands.html"),
    navItem("workshop", "ワークショップ", root + "workshop.html"),
    "<li>ショッピング(準備中)</li>",
    navItem("magazine", "よみもの", root + "magazine.html"),
    navItem("about", "c-corpについて", root + "about.html")
  ].join("");

  var sidebar = document.querySelector(".sidebar");
  if (sidebar) {
    sidebar.innerHTML =
      "<a class=\"logo\" href=\"" + root + "index.html\"><img src=\"" + root + "image/Logo_250_ol.png\" alt=\"ブランドロゴ\"></a>" +
      "<ul class=\"menu\">" + navHtml + "</ul>" +
      "<div class=\"side-sub\"><div><a href=\"" + root + "mail.html\">メールマガジン登録</a></div><div>公式Instagram</div></div>";
  }

  var spHeader = document.querySelector(".sp-header");
  if (spHeader) {
    spHeader.innerHTML =
      "<a class=\"sp-logo\" href=\"" + root + "index.html\"><img src=\"" + root + "image/Logo_250_ol.png\" alt=\"ブランドロゴ\"></a>" +
      "<button class=\"sp-menu-btn\" type=\"button\" aria-label=\"メニューを開く\" aria-expanded=\"false\" aria-controls=\"sp-drawer\">" +
      "<img class=\"sp-menu-icon\" src=\"" + root + "image/Menu.png\" alt=\"ハンバーガーメニューアイコン\">" +
      "</button>";
  }

  var spDrawer = document.querySelector(".sp-drawer");
  if (spDrawer) {
    spDrawer.innerHTML =
      "<ul class=\"menu\">" + navHtml + "</ul>" +
      "<div class=\"side-sub\"><div><a href=\"" + root + "mail.html\">メールマガジン登録</a></div><div>公式Instagram</div></div>";
  }

  var menuButton = document.querySelector(".sp-menu-btn");
  if (menuButton && spDrawer) {
    function isDrawerEnabled() {
      return window.matchMedia("(max-width: 1280px)").matches;
    }

    function closeDrawer() {
      spDrawer.classList.remove("is-open");
      menuButton.setAttribute("aria-expanded", "false");
    }

    menuButton.addEventListener("click", function (event) {
      event.stopPropagation();
      if (!isDrawerEnabled()) return;
      var willOpen = !spDrawer.classList.contains("is-open");
      spDrawer.classList.toggle("is-open", willOpen);
      menuButton.setAttribute("aria-expanded", willOpen ? "true" : "false");
    });

    document.addEventListener("click", function (event) {
      if (!isDrawerEnabled()) return;
      if (!spDrawer.classList.contains("is-open")) return;
      if (spDrawer.contains(event.target) || menuButton.contains(event.target)) return;
      closeDrawer();
    });

    window.addEventListener("resize", function () {
      if (!isDrawerEnabled()) closeDrawer();
    });
  }
})();
