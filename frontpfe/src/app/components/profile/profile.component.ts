import { Component, OnInit } from '@angular/core';
import { QuestionService } from '../../services/question.service';
import { HttpClient } from '@angular/common/http';
import { MainComponent } from '../../pages/main/main.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MainNavbarComponent } from '../main-navbar/main-navbar.component';
import { KnowledgeDTO } from './Knowledge.dto';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  standalone: true,
  imports: [
    MainNavbarComponent,
    CommonModule,
    FormsModule
  ],
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  userId: number = 0;
  userProfile: any = {};
  userQuestions: any[] = [];
  pageNumber = 0;
  totalPages = 0;

  constructor(private questionService: QuestionService, private http: HttpClient) {}
  editForm: any = {
    title: '',
    body: '',
    departement: '',
    projet: ''
  };
  showEditModal = false;
  editingQuestionId: number | null = null;
  editProfileForm: any = {
    name: '',
    email: '',
    phone: '',
    departement: '',
    projet: ''
  };
  showProfileEditModal = false;
  
  selectedTab: 'questions' | 'knowledge' = 'questions';
  userKnowledges: any[] = [];
   // Editing
   editingKnowledgeId: number | null = null;
   knowledgeEditForm: any = {
     title: '',
     description: '',
     content: '',
     departement: '',
     projet: ''
   };
   showEditKnowledgeModal = false;
  ngOnInit(): void {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    this.userId = user.id;

    if (this.userId) {
      this.getUserProfile();
      this.getUserQuestions(this.pageNumber);
    }
  }
  switchTab(tab: 'questions' | 'knowledge') {
    this.selectedTab = tab;
    if (tab === 'knowledge') {
      this.getUserKnowledges();
    }
  }
  
  getUserKnowledges() {
    this.http.get<any[]>(`http://localhost:8080/api/knowledge/user/${this.userId}`).subscribe({
      next: (data) => {
        this.userKnowledges = data;
      },
      error: (err) => console.error('Failed to load user knowledges:', err)
    });
  }
  editQuestion(questionId: number) {
    const question = this.userQuestions.find(q => q.id === questionId);
    if (!question) return;
  
    this.editingQuestionId = questionId;
    this.editForm = {
      title: question.title,
      body: question.body,
      departement: question.departement,
      projet: question.projet
    };
    this.showEditModal = true;
  }
  
  closeEditModal() {
    this.showEditModal = false;
    this.editingQuestionId = null;
    this.editForm = { title: '', body: '', departement: '', projet: '' };
  }
  
  submitEditQuestion() {
    if (!this.editingQuestionId) return;
  
    this.questionService.updateQuestion(this.editingQuestionId, this.editForm).subscribe({
      next: (updated) => {
        const index = this.userQuestions.findIndex(q => q.id === this.editingQuestionId);
        if (index > -1) this.userQuestions[index] = { ...this.userQuestions[index], ...updated };
        this.closeEditModal();
      },
      error: (err) => {
        console.error('❌ Failed to update question:', err);
        alert('Update failed.');
      }
    });
  }
  openProfileEditModal() {
    this.editProfileForm = {
      name: this.userProfile.name,
      email: this.userProfile.email,
      phone: this.userProfile.phone,
      departement: this.userProfile.departement,
      projet: this.userProfile.projet
    };
    this.showProfileEditModal = true;
  }
  
  closeProfileEditModal() {
    this.showProfileEditModal = false;
  }
  
  submitProfileEdit() {
    this.http.put(`http://localhost:8080/api/user/update/${this.userId}`, this.editProfileForm).subscribe({
      next: (updatedProfile) => {
        this.userProfile = updatedProfile;
        this.closeProfileEditModal();
      },
      error: (err) => {
        console.error('❌ Failed to update profile:', err);
        alert('Update failed.');
      }
    });
  }
  
  getUserProfile() {
    this.http.get(`http://localhost:8080/api/user/${this.userId}`).subscribe({
      next: (data) => {
        this.userProfile = data;
      },
      error: (err) => console.error('Failed to fetch user profile:', err)
    });
  }

  getUserQuestions(page: number) {
    this.questionService.getAllQuestionsByUser(this.userId, page).subscribe({
      next: (res) => {
        this.userQuestions = res.questionDTOList;
        this.pageNumber = res.pageNumber;
        this.totalPages = res.totalPages;
      },
      error: (err) => console.error('Failed to fetch user questions:', err)
    });
  }

  deleteQuestion(id: number) {
    if (confirm('Are you sure you want to delete this question?')) {
      this.questionService.deleteQuestion(id).subscribe({
        next: () => {
          this.userQuestions = this.userQuestions.filter(q => q.id !== id);
        },
        error: (err) => console.error('Failed to delete question:', err)
      });
    }
  }

  

  previousPage() {
    if (this.pageNumber > 0) {
      this.getUserQuestions(this.pageNumber - 1);
    }
  }

  nextPage() {
    if (this.pageNumber < this.totalPages - 1) {
      this.getUserQuestions(this.pageNumber + 1);
    }
  }
  // Delete
  deleteKnowledge(id: number) {
    if (confirm('Are you sure you want to delete this knowledge?')) {
      this.http.delete(`http://localhost:8080/api/knowledge/delete/${id}?userId=${this.userId}`)
        .subscribe({
          next: () => {
            setTimeout(() => {
              this.userKnowledges = this.userKnowledges.filter(k => k.id !== id);
            }, 100); // small delay ensures DOM cleanup before re-render
                        },
          error: (err) => console.error('❌ Failed to delete knowledge:', err)
        });
    }
  }
  

  // Edit
  editKnowledge(knowledge: KnowledgeDTO) {
    this.editingKnowledgeId = knowledge.id;
    this.knowledgeEditForm = {
      title: knowledge.title,
      description: knowledge.description,
      content: knowledge.content,
      departement: knowledge.departement,
      projet: knowledge.projet
    };
    this.showEditKnowledgeModal = true;
  }
  
  closeKnowledgeModal() {
    this.showEditKnowledgeModal = false;
    this.editingKnowledgeId = null;
    this.knowledgeEditForm = {
      title: '',
      description: '',
      content: '',
      departement: '',
      projet: ''
    };
  }
  
  submitEditKnowledge() {
    if (!this.editingKnowledgeId) return;
  
    const payload = {
      ...this.knowledgeEditForm,
      userId: this.userId
    };
  
    this.http.put<KnowledgeDTO>(`http://localhost:8080/api/knowledge/update/${this.editingKnowledgeId}`, payload)
      .subscribe({
        next: (updated) => {
          const index = this.userKnowledges.findIndex(k => k.id === this.editingKnowledgeId);
          if (index !== -1) this.userKnowledges[index] = updated;
          this.closeKnowledgeModal();
        },
        error: (err) => {
          console.error('❌ Failed to update knowledge:', err);
          alert('Update failed.');
        }
      });
  }
  
}
