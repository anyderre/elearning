import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    FormsModule,
    RouterModule],
  declarations: [],
  exports: [FormsModule]
})
export class SharedModule {}
