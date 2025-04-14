import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-knowledges-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './knowledges-management.component.html',
  styleUrls: ['./knowledges-management.component.scss']
})
export class KnowledgesManagementComponent implements OnInit {
  knowledges: any[] = [];
  editedKnowledge: any = {};
  showEditModal = false;
  API_URL = 'http://localhost:8080/api/knowledge';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadKnowledges();
  }

  loadKnowledges() {
    this.http.get<any[]>(this.API_URL).subscribe({
      next: data => this.knowledges = data,
      error: err => console.error('❌ Failed to load knowledges:', err)
    });
  }

  deleteKnowledge(id: number) {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (!user.id) return;

    if (confirm(`Are you sure you want to delete knowledge ID ${id}?`)) {
      this.http.delete(`${this.API_URL}/delete/${id}?userId=${user.id}`).subscribe({
        next: () => {
          this.knowledges = this.knowledges.filter(k => k.id !== id);
        },
        error: err => console.error('❌ Failed to delete knowledge:', err)
      });
    }
  }

  editKnowledge(k: any) {
    this.editedKnowledge = { ...k };
    this.showEditModal = true;
  }

  submitEdit() {
    if (!this.editedKnowledge.id) return;

    this.http.put(`${this.API_URL}/update/${this.editedKnowledge.id}`, this.editedKnowledge).subscribe({
      next: (updated: any) => {
        const index = this.knowledges.findIndex(k => k.id === updated.id);
        if (index !== -1) this.knowledges[index] = updated;
        this.closeModal();
      },
      error: err => console.error('❌ Failed to update knowledge:', err)
    });
  }

  closeModal() {
    this.showEditModal = false;
    this.editedKnowledge = {};
  }
}
