(function () {
  "use strict";

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

