import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { Routes, RouterModule } from '@angular/router';

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './shared/auth/auth-guard.service';
import { AppComponent } from './app.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    component: AdminLayoutComponent,
    canActivate: [ AuthGuard ],
    children: [
      {
        path: '',
        loadChildren: () => import('./layouts/admin-layout/admin-layout.module').then(m => m.AdminLayoutModule)
      },
      {
        path: 'courses',
        loadChildren: () => import('./courses/courses-module').then(m => m.CoursesModule)
      },
      {
        path: 'section',
        loadChildren: () => import('./section-admin/section/section-module').then(m => m.SectionModule)
      },
      {
        path: 'category-admin',
        loadChildren: () => import('./category-admin/category-admin-module').then(m => m.CategoryAdminModule)
      },
      {
        path: 'section-admin',
        loadChildren: () => import('./section-admin/section-admin-module').then(m => m.SectionAdminModule)
      },
      {
        path: 'admin',
        loadChildren: () => import('./admin/admin-module').then(m => m.AdminModule)
      },
    ]
  },

  {
    path: '**',
    redirectTo: 'dashboard'
  }
];

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule.forRoot(routes, {
      useHash: true
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
