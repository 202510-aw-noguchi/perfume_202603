(function () {
  "use strict";

  var menuButton = document.querySelector(".sp-menu-btn");
  var drawer = document.querySelector(".sp-drawer");

  if (menuButton && drawer) {
    function isDrawerEnabled() {
      return window.matchMedia("(max-width: 1280px)").matches;
    }

    function closeDrawer() {
      drawer.classList.remove("is-open");
      menuButton.setAttribute("aria-expanded", "false");
    }

    function toggleDrawer() {
      if (!isDrawerEnabled()) return;
      var open = drawer.classList.toggle("is-open");
      menuButton.setAttribute("aria-expanded", open ? "true" : "false");
    }

    menuButton.addEventListener("click", function (event) {
      event.stopPropagation();
      toggleDrawer();
    });

    document.addEventListener("click", function (event) {
      if (!isDrawerEnabled()) return;
      if (!drawer.contains(event.target) && !menuButton.contains(event.target)) {
        closeDrawer();
      }
    });

    window.addEventListener("resize", function () {
      if (!isDrawerEnabled()) closeDrawer();
    });
  }

  var readMoreButton = document.querySelector(".read-more-btn");
  var articleBody = document.querySelector(".article-body");

  if (!readMoreButton || !articleBody) return;

  readMoreButton.addEventListener("click", function () {
    var opened = articleBody.classList.toggle("is-open");
    readMoreButton.setAttribute("aria-expanded", opened ? "true" : "false");
    readMoreButton.classList.toggle("is-open", opened);
    articleBody.style.maxHeight = opened ? articleBody.scrollHeight + "px" : "0px";
  });

  window.addEventListener("resize", function () {
    if (articleBody.classList.contains("is-open")) {
      articleBody.style.maxHeight = articleBody.scrollHeight + "px";
    }
  });
})();
