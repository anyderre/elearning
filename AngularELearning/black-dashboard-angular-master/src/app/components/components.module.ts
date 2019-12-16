import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";

import { FooterComponent } from "./footer/footer.component";
import { NavbarComponent } from "./navbar/navbar.component";
import { SidebarComponent } from "./sidebar/sidebar.component";
import { LoginHandleComponent } from './navbar/login-handle/login-handle.component';

@NgModule({
  imports: [CommonModule, RouterModule, NgbModule,],
  declarations: [FooterComponent, NavbarComponent, SidebarComponent, LoginHandleComponent],
  exports: [FooterComponent, NavbarComponent, SidebarComponent, LoginHandleComponent]
})
export class ComponentsModule {}
