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

  // ✅ Fonction pour récupérer le token JWT
  private getAuthHeaders(): HttpHeaders {
    let token = localStorage.getItem('jwtToken');
    console.log("ℹ️ Token récupéré :", token); // ✅ Ajout du log
    
    if (!token) {
      console.error("⚠️ Aucun token JWT trouvé !");
      return new HttpHeaders();
    }

    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // ✅ LOGIN
  login(credentials: { email: string, password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/authenticate`, credentials).pipe(
      tap((response: any) => {
        if (response.jwtToken) {
          if (typeof window !== 'undefined' && window.localStorage) {
            localStorage.setItem('jwtToken', response.jwtToken);
            localStorage.setItem('typeUser', response.typeUser);
          }
          this.router.navigate(['/main']);
        }
      })
    );
  }

  // ✅ REGISTER
  register(userData: { 
    name: string, 
    email: string, 
    password: string, 
    phone: string, 
    date: string, // Birthday in YYYY-MM-DD format
    typeUser: string 
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/signup`, userData);
  }

  // ✅ LOGOUT
  logout(): void {
    if (typeof window !== 'undefined' && window.localStorage) {
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('typeUser');
    }
    this.router.navigate(['/login']);
  }

  // ✅ CHECK AUTHENTICATION
  isAuthenticated(): boolean {
    if (typeof window !== 'undefined' && window.localStorage) {
      return !!localStorage.getItem('jwtToken');
    }
    return false;
  }

  // ✅ GET USER ROLE
  getUserRole(): string | null {
    if (typeof window !== 'undefined' && window.localStorage) {
      return localStorage.getItem('typeUser');
    }
    return null;
  }

  // ✅ CHECK IF ADMIN
  isAdmin(): boolean {
    if (typeof window !== 'undefined' && window.localStorage) {
      const role = localStorage.getItem('typeUser')?.toUpperCase();
      console.log("ℹ️ Rôle récupéré :", role); // ✅ Ajout du log
      return role === 'ADMIN';
    }
    return false;
  }

  // ✅ GET ALL USERS (réservé aux admins)
  getAllUsers(): Observable<any> {
    if (!this.isAdmin()) {
      console.error("🚫 Accès refusé : L'utilisateur n'est pas ADMIN.");
      return new Observable(observer => {
        observer.error("Accès refusé : Vous devez être administrateur.");
      });
    }
    return this.http.get(`${this.apiUrl}/api/user/all`, { headers: this.getAuthHeaders() });
  }

  // ✅ GET USER BY ID (réservé aux admins)
  getUserById(userId: number): Observable<any> {
    if (!this.isAdmin()) {
      console.error("🚫 Accès refusé : L'utilisateur n'est pas ADMIN.");
      return new Observable(observer => {
        observer.error("Accès refusé : Vous devez être administrateur.");
      });
    }
    return this.http.get(`${this.apiUrl}/api/user/${userId}`, { headers: this.getAuthHeaders() });
  }

  // ✅ UPDATE USER (réservé aux admins)
  updateUser(userId: number, userData: any): Observable<any> {
    if (!this.isAdmin()) {
      console.error("🚫 Accès refusé : L'utilisateur n'est pas ADMIN.");
      return new Observable(observer => {
        observer.error("Accès refusé : Vous devez être administrateur.");
      });
    }
    return this.http.put(`${this.apiUrl}/api/user/update/${userId}`, userData, { headers: this.getAuthHeaders() });
  }

  // ✅ DELETE USER (réservé aux admins)
  deleteUser(userId: number): Observable<any> {
    if (!this.isAdmin()) {
      console.error("🚫 Accès refusé : L'utilisateur n'est pas ADMIN.");
      return new Observable(observer => {
        observer.error("Accès refusé : Vous devez être administrateur.");
      });
    }
    return this.http.delete(`${this.apiUrl}/api/user/delete/${userId}`, { headers: this.getAuthHeaders() });
  }
}