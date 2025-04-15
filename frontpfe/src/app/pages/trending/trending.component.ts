import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MainNavbarComponent } from '../../components/main-navbar/main-navbar.component';
import { QuestionService } from '../../services/question.service';
import { forkJoin, from } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';

@Component({
  selector: 'app-trending',
  templateUrl: './trending.component.html',
  styleUrls: ['./trending.component.scss'],
  standalone: true,
  imports: [MainNavbarComponent, CommonModule,SidebarComponent],
})
export class TrendingComponent implements OnInit {
  questions: any[] = [];
  pageNumber: number = 0;
  totalPages: number = 0;
  showImageModal: boolean = false;
  selectedImageUrl: string = '';

  constructor(
    private router: Router,
    private questionService: QuestionService
  ) {}

  ngOnInit(): void {
    console.log('TrendingComponent loaded!');
    this.loadQuestions(); // Load questions on init
  }

  // Load trending questions from backend
  loadQuestions(page: number = 0, size: number = 5): void {
    this.pageNumber = page;
    const headers = this.questionService.getHeaders();

    this.questionService.getAllQuestionsSorted(this.pageNumber, size, headers).subscribe({
      next: (res) => {
        this.questions = res.questionDTOList;
        this.totalPages = res.totalPages;
        this.pageNumber = res.pageNumber;

        // Load votes and images for each question
        this.loadVotesAndImages().then(() => {
          this.paginateQuestions(); // Keep pagination logic if needed
          console.log('🔎 Final questions after enrich:', this.questions);
        });
      },
      error: (err) => {
        console.error('❌ Error loading questions:', err);
      }
    });
  }

  // Load votes and images for all questions
  loadVotesAndImages(): Promise<void> {
    const voteObservables = this.questions.map((question) =>
      this.questionService.getQuestionVotes(question.id).pipe(
        map((voteStats) => {
          const target = this.questions.find(q => q.id === question.id);
          if (target && voteStats) {
            target.upvotes = voteStats.upvotes;
            target.downvotes = voteStats.downvotes;
            target.score = voteStats.score;
          }
        })
      )
    );

    const imageObservables = this.questions.map((question) =>
      this.questionService.getImageByQuestionId(question.id).pipe(
        switchMap((imgBlob: Blob) =>
          from(
            new Promise<void>((resolve) => {
              const reader = new FileReader();
              reader.onload = () => {
                const target = this.questions.find(q => q.id === question.id);
                if (target) {
                  target.imageUrl = reader.result as string;
                }
                resolve();
              };
              reader.onerror = (error) => {
                console.error(`⚠️ Image load error for question ID ${question.id}:`, error);
                resolve();
              };
              reader.readAsDataURL(imgBlob);
            })
          )
        )
      )
    );

    return forkJoin([...voteObservables, ...imageObservables]).toPromise().then(() => {});
  }

  // Keep pagination logic (can adjust size if needed)
  paginateQuestions(): void {
    const questionsPerPage = 5; // Should match API request size
    const start = this.pageNumber * questionsPerPage;
    const end = start + questionsPerPage;
    this.questions = this.questions.slice(start, end);
  }

  openImageModal(imageUrl: string): void {
    this.selectedImageUrl = imageUrl;
    this.showImageModal = true;
  }

  closeImageModal(): void {
    this.showImageModal = false;
    this.selectedImageUrl = '';
  }

  onQuestionsClick(): void {
    console.log('Navigating to MainComponent...');
    this.router.navigate(['/main']);
  }
  onUsersClick(): void {
    console.log('Navigating to MainComponent...');
    this.router.navigate(['/users']);
  }
  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.loadQuestions(page);
    }
  }
}
