import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    const isAuthenticated = this.authService.isAuthenticated();
    
    // Log pour vérifier si l'utilisateur est authentifié ou non
    console.log('AuthGuard: Is user authenticated?', isAuthenticated);  // Affiche l'état d'authentification

    if (isAuthenticated) {
      console.log('AuthGuard: Access granted to route');  // Log si l'utilisateur peut accéder à la route
      return true;
    } else {
      console.log('AuthGuard: Access denied, redirecting to login');  // Log si l'utilisateur n'est pas authentifié
      this.router.navigate(['/login']);  // Redirection vers /login
      return false;
    }
  }
}
