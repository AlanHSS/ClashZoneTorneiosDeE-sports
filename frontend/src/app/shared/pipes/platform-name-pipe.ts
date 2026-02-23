import { Pipe, PipeTransform } from '@angular/core';
import { Plataforma } from '@core/models';

@Pipe({
  name: 'platformName',
  standalone: true
})
export class PlatformNamePipe implements PipeTransform {
  private platformNames: Record<Plataforma, string> = {
    [Plataforma.PC]: 'PC',
    [Plataforma.PLAYSTATION]: 'PlayStation',
    [Plataforma.XBOX]: 'Xbox',
    [Plataforma.NINTENDO_SWITCH]: 'Nintendo Switch',
    [Plataforma.MOBILE]: 'Mobile',
    [Plataforma.CROSS_PLATAFORM]: 'Cross-Platform',
    [Plataforma.OUTRA]: 'Outra'
  };

  transform(value: Plataforma): string {
    return this.platformNames[value] || value;
  }
}
