import { Injectable } from '@angular/core';

@Injectable()
export class Helper {
  /** @description get the value of string
    ** @alias fStr 
  */
  public static obtenerValorString(texto: any): string {
    if (texto === undefined || texto === null) {
      return '';
    }

    return `${texto}`;
  }

  /** @description se utiliza retornar un valor numerico en caso de ser nullo o indefinido retorna cero
   ** @alias fNum
  */
  public static obtenerValorNumerico(value): number {
    // tslint:disable-next-line: use-isnan
    if (value === NaN || value === undefined || value === null || isNaN(value)) {
      return 0;
    }

    return +value;
  }
}
