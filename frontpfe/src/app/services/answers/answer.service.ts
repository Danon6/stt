import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AnswerDTO {
  id?: number;
  body: string;
  createdDate?: Date;
  questionId: number;
  userId: number;
  username?: string;
  imageUrl?: string; // URL vers l'image de l'answer
}

@Injectable({
  providedIn: 'root'
})
export class AnswerService {
  private apiUrl = 'http://localhost:8080/api/answer';

  constructor(private http: HttpClient) {}

  // ✅ Crée les headers avec le token JWT
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders({
      Authorization: `Bearer ${token ?? ''}`
    });
  }

  // ✅ Envoie une réponse avec ou sans image (via FormData)
  postAnswerWithImage(formData: FormData): Observable<AnswerDTO> {
    return this.http.post<AnswerDTO>(this.apiUrl, formData, {
      headers: this.getAuthHeaders()
    });
  }

  // ✅ Récupère les réponses d’une question
  getAnswersByQuestionId(questionId: number): Observable<AnswerDTO[]> {
    return this.http.get<AnswerDTO[]>(`${this.apiUrl}/question/${questionId}`, {
      headers: this.getAuthHeaders()
    });
  }

  // ✅ Upload image seule pour une réponse (optionnel)
  uploadImageForAnswer(answerId: number, image: File): Observable<any> {
    const formData = new FormData();
    formData.append('multipartFile', image);

    return this.http.post(`http://localhost:8080/api/image/${answerId}`, formData, {
      headers: this.getAuthHeaders()
    });
  }

  // ✅ Get image for answer (➡️ pour affichage sans reload)
  getImageByAnswerId(answerId: number): Observable<Blob> {
    return this.http.get(`http://localhost:8080/api/image/answer/${answerId}`, {
      headers: this.getAuthHeaders(),
      responseType: 'blob'
    }) as Observable<Blob>;
  }
  
}
