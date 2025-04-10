// ✅ Add to existing imports
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';

const API_URL = 'http://localhost:8080/api';

export interface QuestionDTO {
  id: number;
  title: string;
  body: string;
  tags: string[];
  userId: number;
  tagsString?: string;
  imageUrl?: string;
  name?: string; // ✅ add this
  createdDate?: string; // ✅ add this
  voteCount?: number;
  departement?: string;
  projet?: string;
}

export interface AllQuestionResponseDto {
  questionDTOList: QuestionDTO[];
  totalPages: number;
  pageNumber: number;
}

export interface SingleQuestionDto {
  title: string;
  body: string;
  tags: string[];
  userId: number;
}
export interface QuestionVoteDto {
  userId: number;
  questionId: number;
  voteType: 'UPVOTE' | 'DOWNVOTE';
}

export interface QuestionVoteStats {
  upvotes: number;
  downvotes: number;
  score: number;
}


@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  getAllKnowledges() {
    return this.http.get<any[]>(`${API_URL}/knowledge`);
  }
  
  postKnowledge(payload: any) {
    return this.http.post(`${API_URL}/knowledge`, payload);
  }
  constructor(private http: HttpClient, private authService: AuthService) {}

  public getHeaders(): HttpHeaders {
    return this.authService.getAuthHeaders();
  }

  // ✅ POST a Question
  postQuestion(question: QuestionDTO): Observable<QuestionDTO> {
    const url = `${API_URL}/question`;
    return this.http.post<QuestionDTO>(url, question, { headers: this.getHeaders() }).pipe(
      catchError(error => {
        console.error('❌ Error posting question', error);
        return throwError(() => new Error('Error posting question.'));
      })
    );
  }

  // ✅ UPLOAD IMAGE for a Question
  uploadImageForQuestion(questionId: number, file: File): Observable<any> {
    const url = `${API_URL}/image/question/${questionId}`;
    const formData = new FormData();
    formData.append('multipartFile', file);

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getAuthToken() || ''}`
    });

    return this.http.post(url, formData, { headers }).pipe(
      catchError(err => {
        console.error('❌ Error uploading image:', err);
        return throwError(() => new Error('Image upload failed'));
      })
    );
  }

  
  

  getAllQuestions(pageNumber: number, headers: HttpHeaders): Observable<AllQuestionResponseDto> {
    const url = `${API_URL}/questions/${pageNumber}`;
    return this.http.get<AllQuestionResponseDto>(url, { headers }).pipe(
      catchError(error => {
        console.error('❌ Error fetching all questions', error);
        return throwError(() => new Error('Erreur lors de la récupération des questions.'));
      })
    );
  }

  getAllQuestionsSorted(page: number, size: number, headers: HttpHeaders): Observable<AllQuestionResponseDto> {
    
    // Ensure the page and size parameters are correctly passed in the URL
    const url = `${API_URL}/questions/all/${page}/${size}`;  
    // Making the HTTP GET request to the backend with the appropriate headers
    return this.http.get<AllQuestionResponseDto>(url, { headers }).pipe(
      catchError(error => {
        console.error('❌ Error fetching all questions', error);  // Log the error for debugging
        // Return an observable with an error message if the request fails
        return throwError(() => new Error('Erreur lors de la récupération des questions.'));
      })
    );
  }
  
  
  
  

  getQuestionById(userId: number, questionId: number): Observable<SingleQuestionDto> {
    const url = `${API_URL}/question/${userId}/${questionId}`;
    return this.http.get<SingleQuestionDto>(url, { headers: this.getHeaders() }).pipe(
      catchError(error => {
        console.error(`❌ Error fetching question ${questionId} for user ${userId}`, error);
        return throwError(() => new Error(`Erreur lors de la récupération de la question ID ${questionId}`));
      })
    );
  }

  getQuestionsByUserId(userId: number, pageNumber: number): Observable<AllQuestionResponseDto> {
    const url = `${API_URL}/questions/${userId}/${pageNumber}`;
    return this.http.get<AllQuestionResponseDto>(url, { headers: this.getHeaders() }).pipe(
      catchError(error => {
        console.error(`❌ Error fetching questions for user ${userId}`, error);
        return throwError(() => new Error(`Erreur lors de la récupération des questions de l'utilisateur ID ${userId}`));
      })
    );
  }
  getImageByQuestionId(questionId: number): Observable<Blob> {
    const url = `${API_URL}/image/question/${questionId}`;
    return this.http.get(url, { headers: this.getHeaders(), responseType: 'blob' }).pipe(
      catchError(error => {
        console.error(`❌ Error fetching image for question ${questionId}`, error);
        return throwError(() => new Error('Erreur lors de la récupération de l’image'));
      })
    );
  }
  voteQuestion(vote: QuestionVoteDto): Observable<any> {
    const url = `${API_URL}/question/vote`;
    return this.http.post(url, vote, {
      headers: this.getHeaders()  // ✅ token ici
    }).pipe(
      catchError(error => {
        console.error('❌ Error voting on question', error);
        return throwError(() => new Error('Erreur lors du vote.'));
      })
    );
  }
  
  getQuestionVotes(questionId: number): Observable<QuestionVoteStats> {
    const url = `${API_URL}/question/${questionId}/votes`;
    return this.http.get<QuestionVoteStats>(url, {
      headers: this.getHeaders()  // ✅ token ici aussi
    }).pipe(
      catchError(error => {
        console.error(`❌ Error fetching vote stats for question ${questionId}`, error);
        return throwError(() => new Error('Erreur lors de la récupération des votes'));
      })
    );
  }
  

  // knowledge api 
  // question.service.ts or knowledge.service.ts
voteKnowledge(payload: {
  userId: number;
  knowledgeId: number;
  voteType: 'UPVOTE' | 'DOWNVOTE';
}) {
  return this.http.post(`${API_URL}/knowledge/vote`, payload);
}

getKnowledgeVotes(knowledgeId: number) {
  return this.http.get<{ score: number }>(`${API_URL}/knowledge/${knowledgeId}/votes`);
}

}
  
  
  

