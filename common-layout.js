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
    "<li>ショッピング</li>",
    navItem("magazine", "よみもの", root + "magazine.html"),
    navItem("about", "c-corpについて", root + "about.html")
  ].join("");

  var sidebar = document.querySelector(".sidebar");
  if (sidebar) {
    sidebar.innerHTML =
      "<a class=\"logo\" href=\"" + root + "index.html\"><img src=\"" + root + "image/Logo_250_ol.png\" alt=\"ブランドロゴ\"></a>" +
      "<ul class=\"menu\">" + navHtml + "</ul>" +
      "<div class=\"side-sub\"><div>メールマガジン登録</div><div>公式Instagram</div></div>";
  }

  var spHeader = document.querySelector(".sp-header");
  if (spHeader) {
    spHeader.innerHTML =
      "<div class=\"sp-logo\"><img src=\"" + root + "image/Logo_250_ol.png\" alt=\"ブランドロゴ\"></div>" +
      "<button class=\"sp-menu-btn\" type=\"button\" aria-label=\"メニューを開く\" aria-expanded=\"false\" aria-controls=\"sp-drawer\">" +
      "<img class=\"sp-menu-icon\" src=\"" + root + "image/Menu.png\" alt=\"ハンバーガーメニューアイコン\">" +
      "</button>";
  }

  var spDrawer = document.querySelector(".sp-drawer");
  if (spDrawer) {
    spDrawer.innerHTML =
      "<ul class=\"menu\">" + navHtml + "</ul>" +
      "<div class=\"side-sub\"><div>メールマガジン登録</div><div>公式Instagram</div></div>";
  }
})();
