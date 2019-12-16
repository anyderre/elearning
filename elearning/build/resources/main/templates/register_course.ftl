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
    <div class="container" ng-controller="CourseController as rc_ctrl">
        <div class="row">
            <div class="col-md-8 col-xs-12">
                <md-card md-theme="{{ showDarkTheme ? 'dark-grey' : 'default' }}" md-theme-watch="">
                    <md-content class="md-padding" layout="column" layout-xs="column">
                        <md-card-title>
                            <md-card-title-text>
                                <h5 class="md-headline">Cours</h5>
                            </md-card-title-text>
                        </md-card-title>
                        <hr>
                        <div class="row">
                            <div class="col-md-2 form-group">
                                <select ng-model="rc_ctrl.itemsPerPage" class="form-control" ng-change="rc_ctrl.paginate()" name="cant" id="cant">
                                    <option ng-selected="true" value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select>
                            </div>
                            <div class="col-md-4 form-group">
                                <input type="text"
                                       name="filter"
                                       class="form-control input-sm"
                                       ng-change="rc_ctrl.filterElements()"
                                       ng-model-options='{ debounce: 1000 }'
                                       ng-model="rc_ctrl.filter"
                                       placeholder="filtrer..."
                                       title="Filtrer les elements de la table">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                            <#--<div style="text-align:center">-->
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                        <tr>
                                            <th style="width: 5%%;">#</th>
                                            <th style="width: 20%;">Titre</th>
                                            <th style="width: 10%;">Prix($)</th>
                                            <th style="width: 10%;">Catégorie</th>
                                            <th style="width: 20%;">Début du cours</th>
                                            <th style="width: 5%;">Duration</th>
                                            <th style="width: 5%;">Payé</th>
                                            <th colspan="2"></th>

                                        </tr>
                                        </thead>
                                        <tbody>
                                            <tr dir-paginate="cou in rc_ctrl.courses|itemsPerPage:rc_ctrl.itemsPerPage" total-items="totalCount">
                                                <td style="width: 5%;">{{rc_ctrl.start+$index+1}}</td>
                                                <td style="width: 20%;">{{cou.title}}</td>
                                                <td style="width: 10%;">{{cou.price | number }}</td>
                                                <td style="width: 10%;">{{cou.category.name}}</td>
                                                <td style="width: 20%;">{{cou.startDate | date: 'dd-MM-yyyy'}}</td>
                                                <td style="width: 5%;">{{cou.duration | number }}</td>
                                                <td style="width: 5%;"><span ng-bind="cou.premium ?'Oui' : 'Non'" class="label label-{{cou.premium ? 'success' : 'danger'}}"></span></td>
                                                <td>
                                                    <button class="btn btn-warning btn-xs"  ng-click="rc_ctrl.edit(cou.id)" title="Modifier">
                                                        <span class="fa fa-pencil"></span>
                                                    </button>
                                                </td>
                                                <td>
                                                    <button class="btn btn-danger btn-xs" ng-click="rc_ctrl.delete(cou.id)" title="Eliminer">
                                                        <span class="fa fa-trash"></span>
                                                    </button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <center>
                                        <dir-pagination-controls
                                                max-size="100"
                                                direction-links="true"
                                                boundary-links="true"
                                                on-page-change="pageChange(newPageNumber)">
                                        </dir-pagination-controls>
                                    </center>
                                </div>
                            </div>
                        </div>
                    </md-content>
                </md-card>
            </div>
            <div class="col-md-4 col-xs-12">
                <md-card md-theme="{{ showDarkTheme ? 'dark-grey' : 'default' }}" md-theme-watch="">
                    <md-content class="md-padding" layout="column" layout-xs="row">
                        <div class="alert alert-danger" role="alert" ng-show="message">{{message}}</div>
                        <form name="courseForm">
                            <md-card-title>
                                <md-card-title-text style="text-align: center">
                                    <h2 class="md-headline">Ajouter un nouveau cours</h2>
                                </md-card-title-text>
                            </md-card-title>
                            <hr>
                            <input type="hidden" ng-model="rc_ctrl.course.id">
                            <div class="row form-group">
                                <div class="col-md-12">
                                    <label class="control-label" for="title">Titre</label>
                                    <input type="text"
                                           submit-required="true"
                                           id="title"
                                           class="form-control"
                                           name="title"
                                           title="Entrez Le titre du cours"
                                           ng-minlength="2"
                                           ng-maxlength="100"
                                           ng-model="rc_ctrl.course.title"
                                           placeholder="Le titre du cours"
                                           required>
                                    <span style="color: red;"
                                          ng-if="courseForm.title.$error.maxlength && (courseForm.title.$dirty ||  courseForm.title.$touched)">Titre trop long!</span>
                                    <span style="color: red;"
                                          ng-if="courseForm.title.$invalid && (courseForm.title.$dirty ||  courseForm.title.$touched)">Titre Invalide</span>
                                    <span style="color: red;"
                                          ng-if="courseForm.title.$error.required &&  (courseForm.title.$dirty ||  courseForm.title.$touched)">Titre obligatoire!</span>
                                    <span style="color: red;"
                                          ng-if="courseForm.title.$error.minlength && (courseForm.title.$dirty ||  courseForm.title.$touched)">Titre trop court!</span>
                                </div>
                                <div class="col-md-12">
                                    <label class="control-label" for="price">Prix</label>
                                    <input type="text"
                                           ng-decimal="15"
                                           submit-required="true"
                                           id="price"
                                           class="form-control"
                                           name="price"
                                           title="Entrez Le prix du cours"
                                           ng-minlength="1"
                                           ng-model="rc_ctrl.course.price"
                                           placeholder="Le prix du cours en dolar"
                                           pattern="\d+(\.\d{1,2})?"
                                           required>
                                    <span style="color: red;"
                                          ng-if="courseForm.price.$invalid && (courseForm.price.$dirty ||  courseForm.price.$touched)">Prix Invalide!! </span>
                                    <span style="color: red;"
                                          ng-if="courseForm.price.$error.number && (courseForm.price.$dirty ||  courseForm.price.$touched)">Prix error!! </span>
                                    <span style="color: red;"
                                          ng-if="courseForm.price.$error.required && (courseForm.price.$dirty ||  courseForm.price.$touched) ">Prix obligatoire!!  </span>
                                </div>
                            </div>

                            <div class="row form-group">
                                <div class="col-md-12">
                                    <label class="control-label" for="category">Catégorie</label>
                                    <select class="form-control"
                                            title="Choisit une catégorie"
                                            name="category"
                                            ng-model="rc_ctrl.course.category"
                                            ng-options="category.name for category in rc_ctrl.categories track by category.id"
                                            id="category"
                                            required>
                                        <option value="">--Choisit une catégorie--</option>
                                    </select>
                                    <span style="color: red;"
                                          ng-show="courseForm.category.$error.required && (courseForm.category.$dirty || courseForm.category.$touched)">Catégory requis!</span>
                                    <span style="color: red;"
                                          ng-show="courseForm.category.$invalid && (courseForm.category.$dirty || courseForm.category.$touched)">Catégory invalide!</span>
                                </div>
                                <div class="col-md-12 col-xs-12">
                                    <label class="control-label" for="startDate">Début du cours</label>
                                    <input type="date" submit-required="true" data-date-format="dd/MMMM/yyyy"
                                           class="form-control" id="startDate" ng-model="rc_ctrl.course.startDate"
                                           placeholder="Debut du Cours" title="Choisit la dâte du debut" required>
                                    <span style="color: red;"
                                          ng-show="myForm.startDate.$error.required && (courseForm.startDate.$dirty ||  courseForm.startDate.$touched)">Début du cours requis</span>
                                    <span style="color: red;"
                                          ng-show="myForm.startDate.$invalid && (courseForm.startDate.$dirty ||  courseForm.startDate.$touched)">Date Invalide</span>
                                </div>
                            </div>

                            <div class="row form-group">
                                <div class="col-md-12">
                                    <label class="control-label" for="duration">Duration</label>
                                    <input type="text"
                                           numbers-only
                                           submit-required="true"
                                           id="duration"
                                           class="form-control"
                                           name="duration"
                                           title="Entrez la durée du cours"
                                           ng-minlength="1"
                                           ng-maxlength="100"
                                           ng-model="rc_ctrl.course.duration"
                                           placeholder="La durée du cours en semaine"
                                           pattern="[0-9]+"
                                           required>
                                    <span style="color: red;"
                                          ng-if="courseForm.duration.$invalid && (courseForm.duration.$dirty ||  courseForm.duration.$touched)">Duration Invalide! <br></span>
                                    <span style="color: red;"
                                          ng-if="courseForm.duration.$error.required && (courseForm.duration.$dirty ||  courseForm.duration.$touched) ">Duration obligatoire! <br></span>
                                </div>
                                <div class="col-md-6 col-xs-12">
                                    <label for="premium" class="control-label">Est premium</label>
                                    <md-switch class="md-primary" name="premium" ng-model="rc_ctrl.course.premium">
                                        Est premium.
                                    </md-switch>
                                </div>
                            </div>
                            <h4>Syllabus</h4>
                            <div class="row form-group">
                                <div class="col-md-12">
                                    <label class="control-label" for="about">A propos du cours</label>
                                    <textarea name="about"
                                              submit-required="true"
                                              id="about"
                                              cols="40"
                                              rows="10"
                                              class="form-control"
                                              title="Definir le syllabus du cours"
                                              ng-model="rc_ctrl.course.syllabus.about"
                                              placeholder="Syllabus du cours"
                                              required></textarea>
                                    <span style="color: red;"
                                          ng-if="courseForm.about.$error.maxlength && (courseForm.about.$dirty ||  courseForm.about.$touched)">Syllabus trop long!</span>
                                    <span style="color: red;"
                                          ng-if="courseForm.about.$invalid && (courseForm.about.$dirty ||  courseForm.about.$touched)">Syllabus Invalide</span>
                                    <span style="color: red;"
                                          ng-if="courseForm.about.$error.required &&  (courseForm.about.$dirty ||  courseForm.about.$touched)">Syllabus obligatoire!</span>
                                    <span style="color: red;"
                                          ng-if="courseForm.about.$error.minlength && (courseForm.about.$dirty ||  courseForm.about.$touched)">Syllabus trop court!</span>
                                </div>
                            </div>
                            <md-card-actions layout="row" layout-align="end center">
                                <md-button type="reset" class="md-raised md-warn" ng-click="rc_ctrl.reset()">Annuler</md-button>
                                <md-button type="submit" class="md-raised md-primary" ng-disabled="courseForm.$invalid"
                                           ng-click="rc_ctrl.register()">Enregistrer
                                </md-button>
                            </md-card-actions>
                        </form>
                    </md-content>
                </md-card>
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
