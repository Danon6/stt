import { Component, OnInit } from '@angular/core';
import { QuestionService, QuestionDTO } from '../../../services/question.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-questions-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './questions-management.component.html',
  styleUrls: ['./questions-management.component.scss']
})
export class QuestionsManagementComponent implements OnInit {
  questions: QuestionDTO[] = [];
  editedQuestion: Partial<QuestionDTO> = {};
  showEditModal: boolean = false;
  page: number = 0;
  pageSize: number = 5;
  totalPages: number = 0;

  constructor(private questionService: QuestionService) {}

  ngOnInit(): void {
    this.loadQuestions();
  }

  loadQuestions() {
    const headers = this.questionService.getHeaders();
    this.questionService.getAllQuestionsSorted(this.page, this.pageSize, headers).subscribe({
      next: (res) => {
        this.questions = res.questionDTOList;
        this.totalPages = res.totalPages;
      },
      error: (err) => console.error('❌ Failed to load questions:', err)
    });
  }

  deleteQuestion(id: number) {
    if (confirm(`❗ Are you sure you want to delete question ID ${id}?`)) {
      this.questionService.deleteQuestion(id).subscribe({
        next: () => {
          this.questions = this.questions.filter(q => q.id !== id); 
        },
        error: (err) => console.error('❌ Failed to delete:', err)
      });
    }
  }

  editQuestion(question: QuestionDTO) {
    this.editedQuestion = { ...question };
    this.showEditModal = true;
  }

  submitEdit() {
    if (!this.editedQuestion.id) return;
    this.questionService.updateQuestion(this.editedQuestion.id, this.editedQuestion).subscribe({
      next: (updated) => {
        const idx = this.questions.findIndex(q => q.id === updated.id);
        if (idx !== -1) this.questions[idx] = updated;
        this.closeModal();
      },
      error: (err) => console.error('❌ Update failed:', err)
    });
  }

  closeModal() {
    this.showEditModal = false;
    this.editedQuestion = {};
  }

  nextPage() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadQuestions();
    }
  }

  previousPage() {
    if (this.page > 0) {
      this.page--;
      this.loadQuestions();
    }
  }
}
