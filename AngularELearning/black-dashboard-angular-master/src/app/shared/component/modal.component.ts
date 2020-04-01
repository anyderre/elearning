import { ModalManager, ModalComponent } from 'ngb-modal';
import { Component, ViewChild, Input } from '@angular/core';

@Component({
selector: 'app-modal',
template: `
    <div class="row">
        <modal>
            <modal-header>
                <h1>{{title}}</h1>
            </modal-header>
            <modal-content>
                {{content}}
            </modal-content>
        </modal>
    </div>`
})
export class CustomModalComponent {
    @ViewChild( ModalComponent, {static: false}) modal;
    @Input() content: any;
    @Input() title: string;

}
