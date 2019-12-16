import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { BrowserModule } from "@angular/platform-browser";
import { Routes, RouterModule } from "@angular/router";

import { AdminLayoutComponent } from "./layouts/admin-layout/admin-layout.component";
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './shared/auth/auth-guard.service';

const routes: Routes = [
  {
    path: '',
    redirectTo: "dashboard",
    pathMatch: "full", 
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
        loadChildren: () => import('./section/section-module').then(m => m.SectionModule)
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category-module').then(m => m.CategoryModule)
      },
    ]
  },
  {
    path: "**",
    redirectTo: "dashboard"
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
