<!DOCTYPE HTML>
<html ng-app="cabAcademie"
      xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<!-- header goes here -->
<#include "header.ftl">
<body ng-cloak class="ng-cloak">
<div id="page">
    <!--nav  bar goes here-->
<#include "nav.ftl">
    <div id="fh5co-course"  class="container" ng-controller="CourseController as c_ctrl" ng-show="couController.courses" >
        <div class="container">
            <div class="row animate-box">
                <div class="col-md-6 col-md-offset-3 text-center fh5co-heading">
                    <h2>Our Course</h2>
                    <p>Dignissimos asperiores vitae velit veniam totam fuga molestias accusamus alias autem provident.
                        Odit
                        ab aliquam dolor eius.</p>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 animate-box">
                    <div class="course">
                        <a href="#" class="course-img" style="background-image: url(images/project-1.jpg);">
                        </a>
                        <div class="desc">
                            <h3><a href="#">Web Master</a></h3>
                            <p>Dignissimos asperiores vitae velit veniam totam fuga molestias accusamus alias autem
                                provident. Odit ab aliquam dolor eius molestias accusamus alias autem provident. Odit ab
                                aliquam dolor eius.</p>
                            <span><a href="#" class="btn btn-primary btn-sm btn-course">Take A Course</a></span>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 animate-box">
                    <div class="course">
                        <a href="#" class="course-img" style="background-image: url(images/project-2.jpg);">
                        </a>
                        <div class="desc">
                            <h3><a href="#">Business &amp; Accounting</a></h3>
                            <p>Dignissimos asperiores vitae velit veniam totam fuga molestias accusamus alias autem
                                provident. Odit ab aliquam dolor eius molestias accusamus alias autem provident. Odit ab
                                aliquam dolor eius.</p>
                            <span><a href="#" class="btn btn-primary btn-sm btn-course">Take A Course</a></span>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 animate-box">
                    <div class="course">
                        <a href="#" class="course-img" style="background-image: url(images/project-3.jpg);">
                        </a>
                        <div class="desc">
                            <h3><a href="#">Science &amp; Technology</a></h3>
                            <p>Dignissimos asperiores vitae velit veniam totam fuga molestias accusamus alias autem
                                provident. Odit ab aliquam dolor eius molestias accusamus alias autem provident. Odit ab
                                aliquam dolor eius.</p>
                            <span><a href="#" class="btn btn-primary btn-sm btn-course">Take A Course</a></span>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 animate-box">
                    <div class="course">
                        <a href="#" class="course-img" style="background-image: url(images/project-4.jpg);">
                        </a>
                        <div class="desc">
                            <h3><a href="#">Health &amp; Psychology</a></h3>
                            <p>Dignissimos asperiores vitae velit veniam totam fuga molestias accusamus alias autem
                                provident. Odit ab aliquam dolor eius molestias accusamus alias autem provident. Odit ab
                                aliquam dolor eius.</p>
                            <span><a href="#" class="btn btn-primary btn-sm btn-course">Take A Course</a></span>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 animate-box">
                    <div class="course">
                        <a href="#" class="course-img" style="background-image: url(images/project-5.jpg);">
                        </a>
                        <div class="desc">
                            <h3><a href="#">Science &amp; Technology</a></h3>
                            <p>Dignissimos asperiores vitae velit veniam totam fuga molestias accusamus alias autem
                                provident. Odit ab aliquam dolor eius molestias accusamus alias autem provident. Odit ab
                                aliquam dolor eius.</p>
                            <span><a href="#" class="btn btn-primary btn-sm btn-course">Take A Course</a></span>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 animate-box">
                    <div class="course">
                        <a href="#" class="course-img" style="background-image: url(images/project-6.jpg);">
                        </a>
                        <div class="desc">
                            <h3><a href="#">Health &amp; Psychology</a></h3>
                            <p>Dignissimos asperiores vitae velit veniam totam fuga molestias accusamus alias autem
                                provident. Odit ab aliquam dolor eius molestias accusamus alias autem provident. Odit ab
                                aliquam dolor eius.</p>
                            <span><a href="#" class="btn btn-primary btn-sm btn-course">Take A Course</a></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="fh5co-register" style="background-image: url(images/img_bg_2.jpg);">
        <div class="overlay"></div>
        <div class="row">
            <div class="col-md-8 col-md-offset-2 animate-box">
                <div class="date-counter text-center">
                    <h2>Get 400 of Online Courses for Free</h2>
                    <h3>By Mike Smith</h3>
                    <div class="simply-countdown simply-countdown-one"></div>
                    <p><strong>Limited Offer, Hurry Up!</strong></p>
                    <p><a href="#" class="btn btn-primary btn-lg btn-reg">Register Now!</a></p>
                </div>
            </div>
        </div>
    </div>


<!-- footer goes here -->
<#include "footer.ftl">

</div>
<div class="gototop js-top">
    <a href="#" class="js-gotop"><i class="icon-arrow-up"></i></a>
</div>
<!--scripts-->
<#include "scripts.ftl">
</body>
</html>
