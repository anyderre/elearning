import { NgModule } from '@angular/core';
import { UserRoutingModule} from './user.routing.module';
import { UserComponent } from './user.component';
import { UserFormComponent } from './user-form/user-form.component';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';
import { UserInfoComponent } from './user-info/user-info.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    UserRoutingModule,
  ],
  declarations: [
    UserComponent,
    UserFormComponent,
    UserInfoComponent
  ],
})
export class UserModule {
}
