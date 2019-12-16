
<div>
    <script type="text/javascript" src="/js/angular/services/CourseService.js"></script>
    <script type="text/javascript" src="/js/angular/services/IndexService.js"></script>
    <script type="text/javascript" src="/js/angular/services/CategoryService.js"></script>
    <script type="text/javascript" src="/js/angular/services/cookies-service.js"></script>
    <script type="text/javascript" src="/js/angular/services/login-service.js"></script>
    <script type="text/javascript" src="/js/angular/controllers/LoginController.js"></script>
    <script type="text/javascript" src="/js/angular/controllers/HomeController.js"></script>
    <script type="text/javascript" src="/js/angular/controllers/LoginController.js"></script>
    <script type="text/javascript" src="/js/angular/controllers/IndexController.js"></script>
    <script type="text/javascript" src="/js/angular/controllers/CourseController.js"></script>
    <script type="text/javascript" src="/js/angular/controllers/CategoryController.js"></script>
    <script type="text/javascript" src="/js/angular-animate.js"></script>
    <!-- jQuery -->
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <!-- jQuery Easing -->
    <script type="text/javascript" src="/js/jquery.easing.1.3.js"></script>
    <!-- Bootstrap -->
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
    <!-- Waypoints -->
    <script type="text/javascript" src="/js/jquery.waypoints.min.js"></script>
    <!-- Stellar Parallax -->
    <script type="text/javascript" src="/js/jquery.stellar.min.js"></script>
    <!-- Carousel -->
    <script type="text/javascript" src="/js/owl.carousel.min.js"></script>
    <!-- Flexslider -->
    <script type="text/javascript" src="/js/jquery.flexslider-min.js"></script>
    <!-- countTo -->
    <script type="text/javascript" src="/js/jquery.countTo.js"></script>
    <!-- Magnific Popup -->
    <script type="text/javascript" src="/js/jquery.magnific-popup.min.js"></script>
    <script type="text/javascript" src="/js/magnific-popup-options.js"></script>
    <!-- Count Down -->
    <script type="text/javascript" src="/js/simplyCountdown.js"></script>
    <!-- Main -->
    <script src="/js/main.js"></script>
    <script type="text/javascript">
        var d = new Date(new Date().getTime() + 1000 * 120 * 120 * 2000);

        // default example
        simplyCountdown('.simply-countdown-one', {
            year: d.getFullYear(),
            month: d.getMonth() + 1,
            day: d.getDate()
        });

        //jQuery example
        $('#simply-countdown-losange').simplyCountdown({
            year: d.getFullYear(),
            month: d.getMonth() + 1,
            day: d.getDate(),
            enableUtc: false
        });
    </script>
</div>
