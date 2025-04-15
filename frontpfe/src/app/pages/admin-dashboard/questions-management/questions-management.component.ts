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
  editedImageFile: File | null = null;
  previewImageUrl: string | null = null;
  
  constructor(private questionService: QuestionService) {}

  ngOnInit(): void {
    this.loadQuestions();
  }

  onEditImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.editedImageFile = input.files[0];
  
      const reader = new FileReader();
      reader.onload = () => {
        this.previewImageUrl = reader.result as string;
      };
      reader.readAsDataURL(this.editedImageFile);
    }
  }
  
  loadQuestions() {
    const headers = this.questionService.getHeaders();
    this.questionService.getAllQuestionsSorted(this.page, this.pageSize, headers).subscribe({
      next: (res) => {
        this.questions = res.questionDTOList;
        this.totalPages = res.totalPages;
  
        // Fetch image for each question
        this.questions.forEach((q, index) => {
          this.questionService.getImageByQuestionId(q.id).subscribe({
            next: (blob) => {
              const reader = new FileReader();
              reader.onload = () => {
                // Assign base64 string to imageUrl for display
                this.questions[index].imageUrl = reader.result as string;
                console.log(`✅ Image loaded for question ${q.id}`);
              };
              reader.readAsDataURL(blob);
            },
            error: () => {
              console.warn(`⚠️ No image found for question ${q.id}`);
            }
          });
        });
      },
      error: (err) => {
        console.error('❌ Failed to load questions:', err);
      }
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
      next: (updatedQuestion) => {
        if (this.editedImageFile) {
          // ✅ Use the new imageService method to update the image
          const formData = new FormData();
          formData.append('multipartFile', this.editedImageFile);
  
          this.questionService.updateQuestionImage(formData, this.editedQuestion.id!).subscribe({
            next: () => {
              console.log('✅ Image updated via new imageService');
              this.loadQuestions(); // Refresh to show new image
              this.closeModal();
              alert('✅ Question updated with new image!');
            },
            error: (err) => {
              console.error('❌ Failed to update image:', err);
              alert('Question updated, but image update failed.');
              this.loadQuestions();
              this.closeModal();
            }
          });
  
        } else {
          this.loadQuestions();
          this.closeModal();
          alert('✅ Question updated successfully!');
        }
      },
      error: (err) => {
        console.error('❌ Failed to update question:', err);
        alert('Error while updating the question.');
      }
    });
  }
  
  

  closeModal() {
    this.showEditModal = false;
    this.editedQuestion = {};
    this.editedImageFile = null;
    this.previewImageUrl = null;
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
