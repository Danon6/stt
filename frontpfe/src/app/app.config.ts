import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { routes } from './app.routes';
import { provideHttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router'; // ✅ Ajout de RouterModule


export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    importProvidersFrom(HttpClientModule, FormsModule,RouterModule),
    provideHttpClient() // ✅ Ensures HTTP client is available
    // ✅ Provide HttpClientModule globally
  ]
};
