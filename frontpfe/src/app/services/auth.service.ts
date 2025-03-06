import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: { email: string, password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/authenticate`, credentials).pipe(
      tap((response: any) => {
        if (response.jwtToken) {
          localStorage.setItem('jwtToken', response.jwtToken);
          localStorage.setItem('typeUser', response.typeUser);
          this.router.navigate(['/main']);
        }
      })
    );
  }

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

  logout(): void {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('typeUser');
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('jwtToken');
  }
}
