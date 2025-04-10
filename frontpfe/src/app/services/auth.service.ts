import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient, private router: Router) {}

  // ✅ Vérifie si on est bien côté navigateur
  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }

  // ✅ Get Authorization Headers (for authenticated requests)
  public getAuthHeaders(): HttpHeaders {
    const token = this.isBrowser() ? localStorage.getItem('jwtToken') : null;

    if (!token) {
      console.error("⚠️ Token is missing or invalid!");
      return new HttpHeaders();
    }

    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // ✅ Login
  login(credentials: { email: string, password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/authenticate`, credentials).pipe(
      tap((response: any) => {
        console.log('JWT Token Response:', response);

        if (this.isBrowser() && response.jwtToken) {
          localStorage.setItem('jwtToken', response.jwtToken);
          localStorage.setItem('typeUser', response.typeUser);

          const user = {
            id: response.user_id,
            username: response.name
          };
          localStorage.setItem('user', JSON.stringify(user));
          console.log("User Data Stored:", user);

          this.router.navigate(['/main']);
        }
      })
    );
  }

  // ✅ Register
  register(userData: {
    name: string,
    email: string,
    password: string,
    phone: string,
    date: string,
    typeUser: string
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/signup`, userData);
  }

  // ✅ Logout
  logout(): void {
    if (this.isBrowser()) {
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('typeUser');
      localStorage.removeItem('user');
    }
    this.router.navigate(['/login']);
  }

  // ✅ Check if the user is authenticated
  isAuthenticated(): boolean {
    const token = this.isBrowser() ? localStorage.getItem('jwtToken') : null;

    console.log('Token récupéré:', token);  // Log le token récupéré

    if (!token) {
      console.log('❌ Aucun token trouvé dans localStorage.');
      return false;
    }

    const tokenParts = token.split('.');
    console.log('Token split en 3 parties:', tokenParts);  // Log les 3 parties du token

    if (tokenParts.length !== 3) {
      console.error("⚠️ Format de token invalide.");
      return false;
    }

    try {
      const payload = tokenParts[1];
      console.log('Payload récupéré du token:', payload);  // Log le payload brut du token

      const decodedPayload = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
      console.log('Payload décodé:', decodedPayload);  // Log le payload décodé

      const parsedPayload = JSON.parse(decodedPayload);
      console.log('Payload JSON parsé:', parsedPayload);  // Log le payload après le parse

      const expirationDate = parsedPayload.exp * 1000;
      console.log('Date d\'expiration du token:', new Date(expirationDate));  // Log la date d'expiration du token

      const isValid = Date.now() < expirationDate;
      console.log('Token valide:', isValid);  // Log si le token est valide ou non
      return isValid;

    } catch (err) {
      console.error("❌ Erreur lors du décodage du token:", err);
      return false;
    }
  }


  // ✅ Get role (typeUser)
  getUserRole(): string | null {
    return this.isBrowser() ? localStorage.getItem('typeUser') : null;
  }

  // ✅ Is user admin?
  isAdmin(): boolean {
    const role = this.isBrowser() ? localStorage.getItem('typeUser')?.toUpperCase() : null;
    console.log("ℹ️ Rôle récupéré :", role);
    return role === 'ADMIN';
  }

  // ✅ Get token (used internally)
  getAuthToken(): string | null {
    return this.isBrowser() ? localStorage.getItem('jwtToken') : null;
  }

  // ✅ Get all users (admin only)
  getAllUsers(): Observable<any> {
    if (!this.isAdmin()) {
      console.error("🚫 Accès refusé : L'utilisateur n'est pas ADMIN.");
      return new Observable(observer => {
        observer.error("Accès refusé : Vous devez être administrateur.");
      });
    }

    return this.http.get(`${this.apiUrl}/api/user/all`, {
      headers: this.getAuthHeaders()
    });
  }
}
