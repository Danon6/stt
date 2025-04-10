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
  imageUrl?: string;
  voteCount?: number; // ✅ Ajout pour afficher les votes dans l'UI
}

export interface AnswerVoteDto {
  userId: number;
  answerId: number;
  voteType: 'UPVOTE' | 'DOWNVOTE';
}

export interface AnswerVoteStats {
  upvotes: number;
  downvotes: number;
  score: number;
}

@Injectable({
  providedIn: 'root'
})
export class AnswerService {
  private apiUrl = 'http://localhost:8080/api/answer';

  constructor(private http: HttpClient) {}

  // ✅ JWT Headers
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders({
      Authorization: `Bearer ${token ?? ''}`
    });
  }

  // ✅ Poster une réponse (avec ou sans image)
  postAnswerWithImage(formData: FormData): Observable<AnswerDTO> {
    return this.http.post<AnswerDTO>(this.apiUrl, formData, {
      headers: this.getAuthHeaders()
    });
  }

  // ✅ Récupérer toutes les réponses d'une question
  getAnswersByQuestionId(questionId: number): Observable<AnswerDTO[]> {
    return this.http.get<AnswerDTO[]>(`${this.apiUrl}/question/${questionId}`, {
      headers: this.getAuthHeaders()
    });
  }

  // ✅ Upload image pour une réponse
  uploadImageForAnswer(answerId: number, image: File): Observable<any> {
    const formData = new FormData();
    formData.append('multipartFile', image);

    return this.http.post(`http://localhost:8080/api/image/${answerId}`, formData, {
      headers: this.getAuthHeaders()
    });
  }

  // ✅ Récupérer l'image associée à une réponse
  getImageByAnswerId(answerId: number): Observable<Blob> {
    return this.http.get(`http://localhost:8080/api/image/answer/${answerId}`, {
      headers: this.getAuthHeaders(),
      responseType: 'blob'
    }) as Observable<Blob>;
  }

  // ✅ Voter sur une réponse
  voteAnswer(vote: AnswerVoteDto): Observable<any> {
    return this.http.post(`${this.apiUrl}/vote`, vote, {
      headers: this.getAuthHeaders()
    });
  }

  // ✅ Obtenir les stats de vote d'une réponse
  getAnswerVotes(answerId: number): Observable<AnswerVoteStats> {
    return this.http.get<AnswerVoteStats>(`${this.apiUrl}/${answerId}/votes`, {
      headers: this.getAuthHeaders()
    });
  }
}
