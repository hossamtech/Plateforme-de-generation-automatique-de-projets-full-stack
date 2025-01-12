import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { HttpClientModule } from '@angular/common/http';
import {icons, LUCIDE_ICONS, LucideIconProvider} from "lucide-angular";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    importProvidersFrom(HttpClientModule),
    { provide: LUCIDE_ICONS,
      multi: true,
      useValue: new LucideIconProvider(icons)
    },
  ],

};
