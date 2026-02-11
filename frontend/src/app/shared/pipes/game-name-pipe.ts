import { Pipe, PipeTransform } from '@angular/core';
import { Games } from '@core/models';

@Pipe({
  name: 'gameName',
  standalone: true
})
export class GameNamePipe implements PipeTransform {
  private gameNames: Record<Games, string> = {
    [Games.LEAGUE_OF_LEGENDS]: 'League of Legends',
    [Games.CS2]: 'Counter-Strike 2',
    [Games.DOTA2]: 'Dota 2',
    [Games.VALORANT]: 'Valorant',
    [Games.OVERWATCH_2]: 'Overwatch 2',
    [Games.RAINBOW_SIX_SIEGE]: 'Rainbow Six Siege',
    [Games.FIFA]: 'FIFA',
    [Games.STREET_FIGHTERS_6]: 'Street Fighter 6',
    [Games.TEKKEN_8]: 'Tekken 8',
    [Games.FORTNITE]: 'Fortnite',
    [Games.PUBG]: 'PUBG',
    [Games.OUTRO]: 'Outro'
  };

  transform(value: Games): string {
    return this.gameNames[value] || value;
  }
}
