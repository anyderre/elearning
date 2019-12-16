<!DOCTYPE HTML>
<html ng-app="cabAcademie">
<#include "header.ftl">
<!-- header goes here -->
<body ng-cloak class="ng-cloak">
<div id="page">
    <!--nav  bar goes here-->
<#include "nav.ftl">
    <!-- aside goes here  -->
    <div ng-controller="LoginController">
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <style>
            /* style the container */
            .container {
                position: relative;
                border-radius: 5px;
                padding: 20px 0 30px 0;
            }

            /* style inputs and link buttons */
            input,
            .btn {
                width: 100%;
                padding: 12px;
                border: none;
                border-radius: 4px;
                margin: 5px 0;
                opacity: 0.85;
                display: inline-block;
                font-size: 17px;
                line-height: 20px;
                text-decoration: none; /* remove underline from anchors */
            }

            input:hover,
            .btn:hover {
                opacity: 1;
            }

            /* add appropriate colors to fb, twitter and google buttons */
            .fb {
                background-color: #3B5998;
                color: white;
            }

            .twitter {
                background-color: #55ACEE;
                color: white;
            }

            .google {
                background-color: #dd4b39;
                color: white;
            }

            /* style the submit button */
            input[type=submit] {
                background-color: #4CAF50;
                color: white;
                cursor: pointer;
            }

            input[type=submit]:hover {
                background-color: #45a049;
            }

            /* Two-column layout */
            .col {
                float: left;
                width: 50%;
                margin: auto;
                padding: 0 50px;
                margin-top: 6px;
            }

            /* Clear floats after the columns */
            .row:after {
                content: "";
                display: table;
                clear: both;
            }

            /* vertical line */
            .vl {
                position: absolute;
                left: 50%;
                transform: translate(-50%);
                border: 2px solid #ddd;
                height: 175px;
            }

            /* text inside the vertical line */
            .vl-innertext {
                position: absolute;
                top: 50%;
                transform: translate(-50%, -50%);
                background-color: #f1f1f1;
                border: 1px solid #ccc;
                border-radius: 50%;
                padding: 8px 10px;
            }

            /* hide some text on medium and large screens */
            .hide-md-lg {
                display: none;
            }

            /* bottom container */
            .bottom-container {
                text-align: center;
                background-color: #666;
                margin-right: 9%;
                margin-left: 9%;
                margin-bottom: 4%;
            }

            /* Responsive layout - when the screen is less than 650px wide, make the two columns stack on top of each other instead of next to each other */
            @media screen and (max-width: 650px) {
                .col {
                    width: 100%;
                    margin-top: 0;
                }

                /* hide the vertical line */
                .vl {
                    display: none;
                }

                /* show the hidden text on small screens */
                .hide-md-lg {
                    display: block;
                    text-align: center;
                }
            }

        </style>
        <div class="container">
            <form name="loginForm" role="form" action="/login" method="POST" class="form-signin">
                <div class="row">
                    <h2 style="text-align:center">Bienvenue Sur CabAcademie</h2>

                    <div class="vl">
                        <span class="vl-innertext">Ou</span>
                    </div>

                    <div class="col">
                        <a href="#" class="fb btn">
                            <i class="fa fa-facebook fa-fw"></i>Se connecter avec Fb
                        </a>
                        <a href="#" class="twitter btn">
                            <i class="fa fa-twitter fa-fw"></i> Se connecter avec Twitter
                        </a>
                        <a href="#" class="google btn"><i class="fa fa-google fa-fw">
                        </i> Se connecter avec Google
                        </a>
                    </div>

                    <div class="col">
                        <div class="hide-md-lg">
                            <p>Ou se connecter manuellement:</p>
                        </div>
                        <input type="text" id="username" name="username" ng-minlength="2" ng-maxlength="20"
                               placeholder="utilisateur" required>
                        <span style="color:red"
                              ng-if="loginForm.username.$dirty && (loginForm.username.$error.minlength || loginForm.username.$error.required)">Nom d'utilisateur incorrect.</span>
                        <input type="password" id="password" name="password" ng-minlength="6" ng-maxlength="20"
                               placeholder="mot de passe" required>
                        <span style="color:red"
                              ng-if="loginForm.password.$dirty && (loginForm.password.$error.minlength || loginForm.password.$error.required)">Mot de passe incorrect.</span>
                    <#if error??>
                        <span>${error}</span>
                    </#if>
                    <#if msg??>
                        <span>${msg}</span>
                    </#if>
                        <input type="submit" value="Login" ng-disabled="loginForm.$invalid">
                    </div>

                </div>
            </form>
        </div>

        <div class="bottom-container">
            <div class="row">
                <div class="col">
                    <a class="pb-modalreglog-submit btn" style="color:white" data-toggle="modal"
                       data-target="#myModal2">S'inscrire
                    </a>
                </div>
                <div class="col">
                    <a href="#" style="color:paleturquoise" class="btn">Forgot password?</a>
                </div>
            </div>
        </div>


        <div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog modal-md" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel">Registration form</h4>
                    </div>
                    <div class="modal-body">
                        <form class="pb-modalreglog-form-reg">
                            <div class="form-group">
                                <div id="pb-modalreglog-progressbar"></div>
                            </div>
                            <div class="form-group">
                                <label for="email">Email address</label>
                                <div class="input-group pb-modalreglog-input-group">
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-user"></span></span>
                                    <input type="email" email="true" ng-model="credentials.userName"
                                           class="form-control" id="inputEmail" placeholder="Email">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="password">Password</label>
                                <div class="input-group pb-modalreglog-input-group">
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-lock"></span></span>
                                    <input type="password" name="password" class="form-control"
                                           ng-model="credentials.password" placeholder="Nom d'utilisateur">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="confirmpassword">Confirm password</label>
                                <div class="input-group pb-modalreglog-input-group">
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-lock"></span></span>
                                    <input type="password" class="form-control" id="inputConfirmPws"
                                           placeholder="Confirm Password">
                                </div>
                            </div>

                            <div class="form-group">
                                <input type="checkbox" name="credentials.conditions" id="conditions" name="conditions">
                                I agree with <a href="#">terms and conditions.</a>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary">S'inscrire</button>
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