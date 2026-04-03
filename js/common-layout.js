(function () {
  "use strict";

  var path = window.location.pathname.replace(/\\/g, "/").toLowerCase();
  var isNestedPage = /\/(magazine|workshop|press)\/[^/]+\.html$/.test(path);
  var isWorkshopSection = /\/workshop\.html$/.test(path) || /\/workshop\/[^/]+\.html$/.test(path);
  var root = isNestedPage ? "../" : "";

  function isCurrent(page) {
    if (page === "index") return /\/index\.html$/.test(path) || path.endsWith("/");
    if (page === "brands") return /\/brands\.html$/.test(path);
    if (page === "workshop") return isWorkshopSection;
    if (page === "magazine") return /\/magazine\.html$/.test(path) || /\/magazine\/\d+\.html$/.test(path);
    if (page === "about") return /\/about\.html$/.test(path);
    if (page === "press") return /\/press\.html$/.test(path) || /\/press\/[^/]+\.html$/.test(path);
    return false;
  }

  function navItem(page, label, href) {
    var currentAttr = isCurrent(page) ? " aria-current=\"page\"" : "";
    return "<li><a href=\"" + href + "\"" + currentAttr + ">" + label + "</a></li>";
  }

  function workshopSubItem(pattern, label, href) {
    var currentAttr = pattern.test(path) ? " aria-current=\"page\"" : "";
    return "<li><a href=\"" + href + "\"" + currentAttr + ">" + label + "</a></li>";
  }

  var workshopSubHtml = isWorkshopSection
    ? "<ul class=\"menu-sub\">" +
      workshopSubItem(/\/workshop\/casual\.html$/, "ショートセッション", root + "workshop/casual.html") +
      workshopSubItem(/\/workshop\/holiday\.html$/, "フレグランス調香体験", root + "workshop/holiday.html") +
      workshopSubItem(/\/workshop\/registration\.html$/, "フレグランス調香体験ご予約", root + "workshop/registration.html") +
      "</ul>"
    : "";

  var workshopMenuHtml = isWorkshopSection
    ? "<li><a href=\"" + root + "workshop.html\" aria-current=\"page\">ワークショップ</a>" + workshopSubHtml + "</li>"
    : "<li><a href=\"" + root + "workshop.html\">ワークショップ</a></li>";

  var navHtml = [
    navItem("brands", "ブランド", root + "brands.html"),
    workshopMenuHtml,
    navItem("magazine", "よみもの", root + "magazine.html"),
    navItem("about", "c-corpについて", root + "about.html"),
    navItem("press", "プレスリリース", root + "press.html")
  ].join("");

  var sideSubHtml =
    "<div class=\"side-sub\">" +
    "<div><a href=\"" + root + "mail.html\">メールマガジン登録</a></div>" +
    "<div>公式Instagram</div>" +
    "<div class=\"side-sub-access\"><a href=\"" + root + "about.html#access-contact\">アクセス・お問い合わせ</a></div>" +
    "</div>";

  var sidebar = document.querySelector(".sidebar");
  if (sidebar) {
    sidebar.innerHTML =
      "<a class=\"logo\" href=\"" + root + "index.html\"><img src=\"" + root + "image/Logo_250_ol.png\" alt=\"ブランドロゴ\"></a>" +
      "<ul class=\"menu\">" + navHtml + "</ul>" +
      sideSubHtml;
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
      sideSubHtml;
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
