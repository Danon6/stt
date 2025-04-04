import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/user'; // ✅ Base API correcte

  constructor(private http: HttpClient, private authService: AuthService) {}

  // ✅ Récupérer les headers d'authentification
  private getAuthHeaders(): HttpHeaders {
    return this.authService.getAuthHeaders();
  }

  // ✅ Récupérer tous les utilisateurs
  getAllUsers(): Observable<any> {
    return this.http.get(`${this.apiUrl}/all`, { headers: this.getAuthHeaders() })
      .pipe(
        map((response: any) => {
          // Extracting user ID and username from each user in the response
          return response.map((user: { id: number; username: string }) => ({
            id: user.id,
            username: user.username
          }));
        }),
        catchError(error => {
          console.error("❌ Erreur lors de la récupération des utilisateurs", error);
          return throwError(() => new Error("Erreur serveur lors de la récupération des utilisateurs."));
        })
      );
  }

  // ✅ Récupérer un utilisateur par ID
  getUserById(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${userId}`, { headers: this.getAuthHeaders() })
      .pipe(
        map((response: any) => {
          // Extracting user ID and username from the response
          return {
            id: response.id,
            username: response.username
          };
        }),
        catchError(error => {
          console.error(`❌ Erreur lors de la récupération de l'utilisateur ID ${userId}`, error);
          return throwError(() => new Error(`Erreur serveur lors de la récupération de l'utilisateur ID ${userId}.`));
        })
      );
  }

  // ✅ Mettre à jour un utilisateur
  updateUser(userId: number, userData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/update/${userId}`, userData, { headers: this.getAuthHeaders() })
      .pipe(
        catchError(error => {
          console.error(`❌ Erreur lors de la mise à jour de l'utilisateur ID ${userId}`, error);
          return throwError(() => new Error(`Erreur serveur lors de la mise à jour de l'utilisateur ID ${userId}.`));
        })
      );
  }

  // ✅ Supprimer un utilisateur
  deleteUser(userId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/delete/${userId}`, { headers: this.getAuthHeaders() })
      .pipe(
        catchError(error => {
          console.error(`❌ Erreur serveur lors de la suppression de l'utilisateur ID ${userId}`, error);
          alert("⚠ Erreur lors de la suppression. Vérifiez si l'utilisateur existe !");
          return throwError(() => new Error("Erreur serveur lors de la suppression."));
        })
      );
  }

  getLoggedInUser() {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    return user;  // This will return the user object with id, username, etc.
  }
  
}
