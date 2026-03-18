(function () {
  "use strict";

  function ensureSliderDots(slider, slideCount, onSelect) {
    var dots = slider.querySelector(".slider-dots");
    if (!dots) {
      dots = document.createElement("div");
      dots.className = "slider-dots";
      slider.appendChild(dots);
    }

    dots.innerHTML = "";
    for (var i = 0; i < slideCount; i += 1) {
      var dot = document.createElement("button");
      dot.type = "button";
      dot.className = "slider-dot";
      dot.setAttribute("aria-label", "スライド" + (i + 1));
      (function (index) {
        dot.addEventListener("click", function () {
          onSelect(index);
        });
      })(i);
      dots.appendChild(dot);
    }

    return dots;
  }

  function updateSliderDots(dots, index, isVisible) {
    if (!dots) return;
    dots.style.display = isVisible ? "flex" : "none";
    for (var i = 0; i < dots.children.length; i += 1) {
      dots.children[i].classList.toggle("is-active", i === index);
    }
  }

  function isSp() {
    return window.matchMedia("(max-width: 900px)").matches;
  }

  window.initWorkshopCardSlider = function (options) {
    var slider = document.querySelector(options.sliderSelector);
    var track = slider ? slider.querySelector(options.trackSelector) : null;
    var slides = track ? track.querySelectorAll(options.slideSelector) : [];
    var navGroup = slider ? slider.querySelector(options.navGroupSelector) : null;
    var prev = slider ? slider.querySelector(options.prevSelector) : null;
    var next = slider ? slider.querySelector(options.nextSelector) : null;
    var index = 0;

    if (!slider || !track || !slides.length || !prev || !next || !navGroup) return;

    var dots = ensureSliderDots(slider, slides.length, function (nextIndex) {
      index = nextIndex;
      updateSlider();
    });

    function updateSlider() {
      if (!isSp()) {
        track.style.transform = "none";
        prev.style.display = "none";
        next.style.display = "none";
        navGroup.style.top = options.desktopTop || "0";
        updateSliderDots(dots, index, false);
        return;
      }

      var slideWidth = slider.clientWidth;
      track.style.transform = "translateX(" + (-slideWidth * index) + "px)";
      prev.style.display = "block";
      next.style.display = "block";

      var currentSlide = slides[index];
      var photo = currentSlide ? currentSlide.querySelector(options.photoSelector || "img") : null;
      if (photo) {
        navGroup.style.top = (photo.offsetTop + (photo.offsetHeight / 2)) + "px";
      }

      updateSliderDots(dots, index, true);
    }

    prev.addEventListener("click", function () {
      index = index === 0 ? slides.length - 1 : index - 1;
      updateSlider();
    });

    next.addEventListener("click", function () {
      index = index === slides.length - 1 ? 0 : index + 1;
      updateSlider();
    });

    var touchStartX = 0;
    var touchEndX = 0;
    slider.addEventListener("touchstart", function (e) {
      touchStartX = e.changedTouches[0].clientX;
    }, { passive: true });

    slider.addEventListener("touchend", function (e) {
      touchEndX = e.changedTouches[0].clientX;
      var diff = touchEndX - touchStartX;
      if (Math.abs(diff) < 40) return;

      if (diff > 0) {
        index = index === 0 ? slides.length - 1 : index - 1;
      } else {
        index = index === slides.length - 1 ? 0 : index + 1;
      }
      updateSlider();
    }, { passive: true });

    window.addEventListener("resize", updateSlider);
    window.addEventListener("load", updateSlider);
    updateSlider();
  };

  window.initWorkshopSceneModal = function () {
    var slider = document.querySelector(".scene-slider");
    var slides = slider ? slider.querySelectorAll(".scene-slide") : [];
    var modal = document.getElementById("scene-modal");
    var modalImage = modal ? modal.querySelector(".scene-modal-image") : null;
    var modalTitle = modal ? modal.querySelector(".scene-modal-title") : null;
    var modalText = modal ? modal.querySelector(".scene-modal-text") : null;
    var modalClose = modal ? modal.querySelector(".scene-modal-close") : null;

    if (!slider || !slides.length || !modal || !modalImage || !modalTitle || !modalText || !modalClose) return;

    function closeModal() {
      modal.classList.remove("is-open");
      modal.hidden = true;
      document.body.style.overflow = "";
    }

    function showDetail(slide) {
      if (isSp()) return;

      var title = slide.getAttribute("data-scene-title") || "";
      var description = slide.getAttribute("data-scene-description") || "";
      var image = slide.querySelector("img");

      modalImage.src = image ? image.getAttribute("src") : "";
      modalImage.alt = title;
      modalTitle.textContent = title;
      modalText.textContent = description;
      modal.hidden = false;
      modal.classList.add("is-open");
      document.body.style.overflow = "hidden";
    }

    slides.forEach(function (slide) {
      var trigger = slide.querySelector(".scene-trigger");
      var title = slide.getAttribute("data-scene-title") || "";
      var description = slide.getAttribute("data-scene-description") || "";
      var detail = slide.querySelector(".scene-slide-detail");

      if (!detail) {
        detail = document.createElement("div");
        detail.className = "scene-slide-detail";
        detail.innerHTML = '<h3 class="scene-detail-title"></h3><p class="scene-detail-text"></p>';
        detail.querySelector(".scene-detail-title").textContent = title;
        detail.querySelector(".scene-detail-text").textContent = description;
        slide.appendChild(detail);
      }

      if (!trigger) return;
      trigger.addEventListener("click", function () {
        showDetail(slide);
      });
    });

    modalClose.addEventListener("click", closeModal);
    modal.addEventListener("click", function (event) {
      if (event.target === modal) closeModal();
    });
    document.addEventListener("keydown", function (event) {
      if (event.key === "Escape") closeModal();
    });
  };
})();
