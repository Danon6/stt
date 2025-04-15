import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { QuestionService, QuestionDTO } from '../../services/question.service';
import { MainNavbarComponent } from '../../components/main-navbar/main-navbar.component';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AnswerService, AnswerDTO } from '../../services/answers/answer.service';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Observable, forkJoin, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    MainNavbarComponent,
    SidebarComponent
  ]
})
export class MainComponent implements OnInit {
  showPostButton = false;
  showForm = false;
  isSubmitting = false;
  showQuestions = false;
  showKnowledgeForm = false;
  knowledgeForm!: FormGroup;
  knowledgeImageFile!: File;
  knowledgeFile!: File;
  searchTerm: string = '';
  filteredQuestions: QuestionDTO[] = [];
  filteredKnowledges: any[] = [];

  // Filter-related properties
  departments: string[] = [];
  projects: string[] = [];
  selectedDepartment: string = '';
  selectedProject: string = '';
  allQuestions: QuestionDTO[] = [];
  allKnowledges: any[] = [];
  allTags: string[] = [];
  selectedTag: string = '';
  
  selectedTab: 'all' | 'questions' | 'knowledge' = 'all';
  knowledges: any[] = [];

  tagsInput: string = '';
  imageFile!: File;
  answerImageFile: { [key: number]: File } = {};

  loggedInUsername: string | null = null;

  newQuestion: QuestionDTO = {
    id: 0,
    title: '',
    body: '',
    tags: [],
    userId: 0,
    departement: '',
    projet: '',
    updatedAt: ''
  };

  validateForm!: FormGroup;
  questions: QuestionDTO[] = [];
  pageNumber: number = 0;
  totalPages: number = 0;
  answerFormsVisible: { [key: number]: boolean } = {};
  answersVisible: { [key: number]: boolean } = {};
  answersMap: { [key: number]: AnswerDTO[] } = {};
  newAnswer: { [key: number]: string } = {};

  // Image modal properties
  showImageModal = false;
  selectedImageUrl: string | undefined;

  constructor(
    private router: Router,
    private authService: AuthService,
    private questionService: QuestionService,
    private answerService: AnswerService
  ) {}
  

  ngOnInit() {
    this.validateForm = new FormGroup({
      title: new FormControl(this.newQuestion.title, [Validators.required]),
      body: new FormControl(this.newQuestion.body, [Validators.required]),
      tagsInput: new FormControl(this.tagsInput),
      departement: new FormControl('', [Validators.required]),  
      projet: new FormControl('', [Validators.required])
    });

    if (typeof window !== 'undefined') {
      const loggedInUser = JSON.parse(localStorage.getItem('user') || '{}');
      if (loggedInUser && loggedInUser.id) {
        this.newQuestion.userId = loggedInUser.id;
        this.loggedInUsername = loggedInUser.username;
      } else {
        console.error('❌ No logged-in user found');
      }
    }

    this.switchTab('all'); // default loads both

    this.showPostButton = true;
    this.showQuestions = true;
    this.loadQuestions();
    
    this.knowledgeForm = new FormGroup({
      title: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      content: new FormControl('', Validators.required),
      projet: new FormControl('', Validators.required),
      departement: new FormControl('', Validators.required)
    });
  }

  extractFiltersFromData(): void {
    // Extract unique departments and projects from questions
    const questionDepts = this.allQuestions
      .map(q => q.departement)
      .filter((dept): dept is string => !!dept);
    
    const questionProjects = this.allQuestions
      .map(q => q.projet)
      .filter((proj): proj is string => !!proj);
    const questionTags = this.allQuestions.flatMap(q => q.tags || []).filter(Boolean);

    
    // Extract unique departments and projects from knowledge items
    const knowledgeDepts = this.allKnowledges
      .map(k => k.departement)
      .filter((dept): dept is string => !!dept);
    
    const knowledgeProjects = this.allKnowledges
      .map(k => k.projet)
      .filter((proj): proj is string => !!proj);
    
    // Combine and deduplicate
    this.departments = [...new Set([...questionDepts, ...knowledgeDepts])].sort();
    this.projects = [...new Set([...questionProjects, ...knowledgeProjects])].sort();
    this.allTags = [...new Set(questionTags)].sort();  // ← NEW

  }

  onSearchChange() {
    this.applyFilters();
  }

  applyFilters(): void {
    const term = this.searchTerm.toLowerCase();
    
    // Filter questions
    this.filteredQuestions = this.allQuestions.filter(q => {
      const matchesSearch = !term || 
        q.title?.toLowerCase().includes(term) || 
        q.body?.toLowerCase().includes(term) || 
        q.tags?.some(t => t.toLowerCase().includes(term));
        
      const matchesDepartment = !this.selectedDepartment || 
        q.departement === this.selectedDepartment;
        
      const matchesProject = !this.selectedProject || 
        q.projet === this.selectedProject;
      const matchesTag = !this.selectedTag || q.tags?.includes(this.selectedTag);

      
      return matchesSearch && matchesDepartment && matchesProject && matchesTag;
    });
    
    // Filter knowledges
    this.filteredKnowledges = this.allKnowledges.filter(k => {
      const matchesSearch = !term || 
        k.title?.toLowerCase().includes(term) || 
        k.description?.toLowerCase().includes(term) || 
        k.content?.toLowerCase().includes(term);
        
      const matchesDepartment = !this.selectedDepartment || 
        k.departement === this.selectedDepartment;
        
      const matchesProject = !this.selectedProject || 
        k.projet === this.selectedProject;
      
      return matchesSearch && matchesDepartment && matchesProject;
    });
  }

  onDepartmentChange(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.selectedDepartment = select.value;
    this.applyFilters();
  }

  onProjectChange(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.selectedProject = select.value;
    this.applyFilters();
  }

  resetFilters(): void {
    this.selectedDepartment = '';
    this.selectedProject = '';
    this.searchTerm = '';
    this.applyFilters();
  }

  switchTab(tab: 'all' | 'questions' | 'knowledge') {
    this.selectedTab = tab;
  
    if (tab === 'questions') {
      this.loadQuestions();
    } else if (tab === 'knowledge') {
      this.knowledges = []; // ⬅️ clear to avoid old data
      this.loadKnowledges();
    } else {
      this.loadQuestions();
      this.loadKnowledges();
    }
  }

  loadKnowledges(): void {
    this.questionService.getAllKnowledges().subscribe({
      next: (data) => {
        this.knowledges = data;
        this.allKnowledges = [...data]; // Store all for filtering
        console.log("KNOWLEDGES 🔍", this.knowledges);

        // Load vote score for each knowledge
        this.knowledges.forEach(k => {
          this.loadVoteForKnowledge(k.id);
        });
        
        this.filteredKnowledges = [...this.knowledges];
        this.extractFiltersFromData(); // Extract filters after loading data
      },
      error: (err) => {
        console.error('❌ Failed to fetch knowledge list:', err);
      }
    });
  }

  onKnowledgeFileSelected(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      this.knowledgeFile = event.target.files[0];
    }
  }
  
  loadQuestions(page: number = 0): void {
    const headers = this.questionService.getHeaders();  // Utilise la méthode qui récupère les headers

    this.questionService.getAllQuestions(page, headers).subscribe({
      next: (res) => {
        this.questions = res.questionDTOList;
        this.allQuestions = [...this.questions]; // Store all for filtering
        this.totalPages = res.totalPages;
        this.pageNumber = res.pageNumber;

        this.questions.forEach((q: any, index: number) => {
          this.questionService.getImageByQuestionId(q.id).subscribe({
            next: (imgBlob) => {
              const reader = new FileReader();
              reader.onload = () => {
                this.questions[index].imageUrl = reader.result as string;
              };
              reader.readAsDataURL(imgBlob);
            },
            error: () => {
              console.warn(`⚠️ Aucune image pour la question ID ${q.id}`);
            }
          });

          this.questionService.getQuestionVotes(q.id).subscribe({
            next: (stats) => {
              this.questions[index].voteCount = stats.score;
            },
            error: () => {
              console.warn(`⚠️ Aucune info de vote pour la question ID ${q.id}`);
            }
          });
        });
        
        this.filteredQuestions = [...this.questions];
        this.extractFiltersFromData(); // Extract filters after loading data
      },
      error: (err) => {
        console.error('❌ Failed to load questions:', err);
      }
    });
  }

  add(event: any): void {
    event.preventDefault();
    const inputValue = this.tagsInput.trim();
    if (inputValue) {
      const tags = inputValue.split(',').map(tag => tag.trim()).filter(tag => tag.length > 0);
      tags.forEach(tag => {
        if (!this.newQuestion.tags.includes(tag)) {
          this.newQuestion.tags.push(tag);
        }
      });
      this.tagsInput = '';
    }
  }

  remove(tag: string): void {
    const index = this.newQuestion.tags.indexOf(tag);
    if (index >= 0) this.newQuestion.tags.splice(index, 1);
  }

  onQuestionsClick(event: MouseEvent) {
    event.preventDefault();
    // this.showQuestions = true;
    this.showPostButton = true;
    this.loadQuestions();
  }

  onPostClick() {
    this.showForm = !this.showForm;
  }

  onImageSelected(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      this.imageFile = event.target.files[0];
    }
  }

  onAnswerImageSelected(event: any, questionId: number) {
    if (event.target.files && event.target.files.length > 0) {
      this.answerImageFile[questionId] = event.target.files[0];
    }
  }

  submitQuestion() {
    if (this.tagsInput.trim()) {
      const tags = this.tagsInput.split(',').map(tag => tag.trim()).filter(tag => tag.length > 0);
      tags.forEach(tag => {
        if (!this.newQuestion.tags.includes(tag)) {
          this.newQuestion.tags.push(tag);
        }
      });
      this.tagsInput = '';
    }

    if (this.validateForm.invalid || this.newQuestion.tags.length === 0) {
      alert('Form invalid or missing tags.');
      return;
    }

    this.isSubmitting = true;
    this.newQuestion.title = this.validateForm.value.title;
    this.newQuestion.body = this.validateForm.value.body;
    this.newQuestion.tagsString = this.newQuestion.tags.join(',');
    this.newQuestion.departement = this.validateForm.value.departement;
    this.newQuestion.projet = this.validateForm.value.projet;


    this.questionService.postQuestion(this.newQuestion).subscribe({
      next: (question) => {
        const questionId = (question as any).id;
        const completeQuestion: any = {
          id: questionId,
          title: this.newQuestion.title,
          body: this.newQuestion.body,
          tags: [...this.newQuestion.tags],
          userId: this.newQuestion.userId,
          voteCount: 0,
          voted: 0,
          name: this.loggedInUsername,
          createdDate: new Date(),
          departement: this.newQuestion.departement, 
          projet: this.newQuestion.projet             
        };

        if (this.imageFile && questionId) {
          this.questionService.uploadImageForQuestion(questionId, this.imageFile).subscribe({
            next: () => {
              this.questionService.getImageByQuestionId(questionId).subscribe({
                next: (imgBlob) => {
                  const reader = new FileReader();
                  reader.onload = () => {
                    completeQuestion.imageUrl = reader.result as string;
                    this.questions.unshift(completeQuestion);
                    this.allQuestions.unshift(completeQuestion); // Add to all questions for filtering
                    this.applyFilters(); // Reapply filters
                  };
                  reader.readAsDataURL(imgBlob);
                },
                error: () => {
                  this.questions.unshift(completeQuestion);
                  this.allQuestions.unshift(completeQuestion);
                  this.applyFilters();
                }
              });
            },
            error: () => {
              this.questions.unshift(completeQuestion);
              this.allQuestions.unshift(completeQuestion);
              this.applyFilters();
            }
          });
        } else {
          this.questions.unshift(completeQuestion);
          this.allQuestions.unshift(completeQuestion);
          this.applyFilters();
        }

        // Update filter options
        this.extractFiltersFromData();

        this.isSubmitting = false;
        this.showForm = false;
        this.newQuestion = {
          id: 0,
          title: '',
          body: '',
          tags: [],
          userId: this.newQuestion.userId,
          tagsString: '',
          imageUrl: '',
          name: '',
          createdDate: '',
          departement: '',
          projet: '',
          updatedAt: ''
        };
        this.validateForm.reset();
      },
      error: (err) => {
        if (err.status === 403) {
          alert('⚠️ Your account is inactive. You cannot post questions.');
        } else {
          console.error('❌ Failed to save question:', err);
          alert('An error occurred while posting the question.');
        }
        this.isSubmitting = false;
      }
      
    });
  }

  submitAnswer(questionId: number) {
    const body = this.newAnswer[questionId]?.trim();
    if (!body) return;

    const user = JSON.parse(localStorage.getItem('user') || '{}');

    const formData = new FormData();
    formData.append('answer', JSON.stringify({
      body,
      questionId,
      userId: user.id,
      username: user.username
    }));

    if (this.answerImageFile[questionId]) {
      formData.append('image', this.answerImageFile[questionId]);
    }

    this.answerService.postAnswerWithImage(formData).subscribe({
      next: (res) => {
        const newAnswer = { ...res };
        if (res.imageUrl) {
          this.loadImageForAnswer(res.id!, (imageUrl) => {
            newAnswer.imageUrl = imageUrl;
            this.addAnswerToMap(questionId, newAnswer);
          });
        } else {
          this.addAnswerToMap(questionId, newAnswer);
        }

        this.newAnswer[questionId] = '';
        this.answerFormsVisible[questionId] = false;
        this.loadAnswerVotes(res.id!);
      },
      error: (err) => {
        if (err.status === 403) {
          alert('⚠️ Your account is inactive. You cannot post answers.');
        } else {
          console.error('❌ Failed to post answer:', err);
          alert('Failed to submit answer.');
        }
      }
      
    });
  }

  loadImageForAnswer(answerId: number, callback: (url: string) => void) {
    this.answerService.getImageByAnswerId(answerId).subscribe({
      next: (imgBlob) => {
        const reader = new FileReader();
        reader.onload = () => {
          callback(reader.result as string);
        };
        reader.readAsDataURL(imgBlob);
      },
      error: () => {
        console.warn(`⚠️ No image found for answer ID ${answerId}`);
        callback('');
      }
    });
  }

  loadAnswerVotes(answerId: number) {
    this.answerService.getAnswerVotes(answerId).subscribe({
      next: (stats) => {
        for (const key in this.answersMap) {
          const answer = this.answersMap[key]?.find(a => a.id === answerId);
          if (answer) {
            (answer as any).voteCount = stats.score;
          }
        }
      },
      error: (err) => {
        console.warn(`⚠️ Failed to load votes for answer ID ${answerId}`, err);
      }
    });
  }

  voteOnAnswer(answerId: number, type: 'UPVOTE' | 'DOWNVOTE') {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (!user.id) {
      alert('❌ Vous devez être connecté pour voter.');
      return;
    }

    const votePayload = {
      userId: user.id,
      answerId,
      voteType: type
    };

    this.answerService.voteAnswer(votePayload).subscribe({
      next: () => this.loadAnswerVotes(answerId),
      error: (err) => {
        console.error('❌ Failed to vote on answer:', err);
      }
    });
  }

  addAnswerToMap(questionId: number, answer: AnswerDTO) {
    if (!this.answersMap[questionId]) this.answersMap[questionId] = [];
    this.answersMap[questionId].push(answer);
  }

  toggleAnswerForm(questionId: number) {
    this.answerFormsVisible[questionId] = !this.answerFormsVisible[questionId];
  }

  toggleAnswers(questionId: number) {
    this.answersVisible[questionId] = !this.answersVisible[questionId];
    if (this.answersVisible[questionId] && !this.answersMap[questionId]) {
      this.loadAnswers(questionId);
    }
  }

  loadAnswers(questionId: number) {
    this.answerService.getAnswersByQuestionId(questionId).subscribe({
      next: (answers) => {
        this.answersMap[questionId] = answers;

        answers.forEach((answer, index) => {
          if (answer.id) {
            this.answerService.getImageByAnswerId(answer.id).subscribe({
              next: (blob) => {
                const reader = new FileReader();
                reader.onload = () => {
                  this.answersMap[questionId][index].imageUrl = reader.result as string;
                };
                reader.readAsDataURL(blob);
              },
              error: () => {
                console.warn(`⚠️ Aucune image pour l'answer ID ${answer.id}`);
              }
            });

            this.loadAnswerVotes(answer.id);
          }
        });
      },
      error: (err) => {
        console.error(`❌ Failed to load answers for question ${questionId}`, err);
      }
    });
  }

  hasAnswers(questionId: number): boolean {
    return !!this.answersMap[questionId]?.length;
  }

  openImageModal(url: string) {
    this.selectedImageUrl = url;
    this.showImageModal = true;
    document.body.classList.add('modal-open');
  }
  
  closeImageModal() {
    this.showImageModal = false;
    this.selectedImageUrl = '';
    document.body.classList.remove('modal-open');
  }

  voteOnQuestion(questionId: number, type: 'UPVOTE' | 'DOWNVOTE') {
    const user = JSON.parse(localStorage.getItem('user') || '{}');

    if (!user.id) {
      alert('❌ Vous devez être connecté pour voter.');
      return;
    }

    const votePayload = {
      userId: user.id,
      questionId,
      voteType: type
    };

    this.questionService.voteQuestion(votePayload).subscribe({
      next: () => {
        this.loadVotesForQuestion(questionId);
      },
      error: (err) => {
        console.error('❌ Failed to vote:', err);
      }
    });
  }

  loadVotesForQuestion(questionId: number) {
    this.questionService.getQuestionVotes(questionId).subscribe({
      next: (stats) => {
        const question = this.questions.find(q => q.id === questionId);
        if (question) {
          question.voteCount = stats.score;
        }
      },
      error: (err) => {
        console.error(`❌ Failed to load votes for question ${questionId}`, err);
      }
    });
  }
  
  onTrendingClick(event: MouseEvent) {
    event.preventDefault();
    console.log('Navigating to Trending...');
    this.router.navigate(['/trending']);
  }
  
  // Knowledge 
  onPostKnowledgeClick() {
    this.showKnowledgeForm = !this.showKnowledgeForm;
  }
  
  onKnowledgeImageSelected(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      this.knowledgeImageFile = event.target.files[0];
    }
  }
  
  submitKnowledge() {
    if (this.knowledgeForm.invalid) {
      alert('Please fill in all required fields.');
      return;
    }
  
    const user = JSON.parse(localStorage.getItem('user') || '{}');
  
    // Create promises to read image and file as base64
    const imagePromise = this.knowledgeImageFile
      ? this.readFileAsBase64(this.knowledgeImageFile)
      : Promise.resolve('');
  
    const filePromise = this.knowledgeFile
      ? this.readFileAsBase64(this.knowledgeFile)
      : Promise.resolve('');
  
    Promise.all([imagePromise, filePromise]).then(([imageBase64, fileBase64]) => {
      const knowledgePayload = {
        ...this.knowledgeForm.value,
        userId: user.id,
        userName: user.username,
        imageBase64,
        imageType: this.knowledgeImageFile?.type || '',
        fileBase64,
        fileType: this.knowledgeFile?.type || '',
        createdDate: new Date()
      };
  
      this.questionService.postKnowledge(knowledgePayload).subscribe({
        next: (response) => {
          alert('✅ Knowledge posted!');
          
          // Add to local arrays
          this.knowledges.unshift(knowledgePayload);
          this.allKnowledges.unshift(knowledgePayload);
          
          // Update filter options with new knowledge data
          this.extractFiltersFromData();
          
          // Reapply filters
          this.applyFilters();
          
          this.showKnowledgeForm = false;
          this.knowledgeForm.reset();
        },
        error: (err) => {
          if (err.status === 403) {
            alert('⚠️ Your account is inactive. You cannot post knowledge.');
          } else {
            console.error('❌ Failed to post knowledge:', err);
            alert('An error occurred while posting knowledge.');
          }
        }
        
      });
    });
  }
  
  // Utility to convert file to base64
  readFileAsBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  }
  
  // Vote on knowledge functions
  voteOnKnowledge(knowledgeId: number, type: 'UPVOTE' | 'DOWNVOTE') {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (!user?.id) {
      alert('You must be logged in to vote.');
      return;
    }
  
    const payload = {
      userId: user.id,
      knowledgeId,
      voteType: type
    };
  
    this.questionService.voteKnowledge(payload).subscribe({
      next: () => this.loadVoteForKnowledge(knowledgeId),
      error: (err) => {
        if (err.status === 403) {
          alert('⚠️ Your account is inactive. You cannot vote on knowledge.');
        } else {
          console.error('❌ Voting on knowledge failed', err);
          alert('Failed to vote on knowledge.');
        }
      }
          });
  }
  
  loadVoteForKnowledge(knowledgeId: number) {
    this.questionService.getKnowledgeVotes(knowledgeId).subscribe({
      next: (res) => {
        const knowledge = this.knowledges.find(k => k.id === knowledgeId);
        if (knowledge) knowledge.voteCount = res.score;
      },
      error: (err) => console.error('❌ Failed to load knowledge vote count', err)
    });
  }
}