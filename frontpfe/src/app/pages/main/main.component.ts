import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { QuestionService, QuestionDTO } from '../../services/question.service';
import { MainNavbarComponent } from '../../components/main-navbar/main-navbar.component';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AnswerService, AnswerDTO } from '../../services/answers/answer.service';

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
  ]
})
export class MainComponent implements OnInit {
  showPostButton = false;
  showForm = false;
  isSubmitting = false;
  showQuestions = false;

  tagsInput: string = '';
  imageFile!: File;
  answerImageFile: { [key: number]: File } = {};

  loggedInUsername: string | null = null;

  newQuestion: QuestionDTO = {
    id: 0,
    title: '',
    body: '',
    tags: [],
    userId: 0
  };

  validateForm!: FormGroup;
  questions: QuestionDTO[] = [];
  pageNumber: number = 0;
  totalPages: number = 0;
  answerFormsVisible: { [key: number]: boolean } = {};
  answersVisible: { [key: number]: boolean } = {};
  answersMap: { [key: number]: AnswerDTO[] } = {};
  newAnswer: { [key: number]: string } = {};

  constructor(
    private questionService: QuestionService,
    private answerService: AnswerService
  ) {}

  ngOnInit() {
    this.validateForm = new FormGroup({
      title: new FormControl(this.newQuestion.title, [Validators.required]),
      body: new FormControl(this.newQuestion.body, [Validators.required]),
      tagsInput: new FormControl(this.tagsInput)
    });

    const loggedInUser = JSON.parse(localStorage.getItem('user') || '{}');
    if (loggedInUser && loggedInUser.id) {
      this.newQuestion.userId = loggedInUser.id;
      this.loggedInUsername = loggedInUser.username;
    } else {
      console.error('❌ No logged-in user found');
    }
  }

  loadQuestions(page: number = 0): void {
    this.questionService.getAllQuestions(page).subscribe({
      next: (res) => {
        this.questions = res.questionDTOList;
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
        });
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
    this.showQuestions = true;
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
          createdDate: new Date()
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
                  };
                  reader.readAsDataURL(imgBlob);
                },
                error: () => {
                  this.questions.unshift(completeQuestion);
                }
              });
            },
            error: () => {
              this.questions.unshift(completeQuestion);
            }
          });
        } else {
          this.questions.unshift(completeQuestion);
        }

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
          createdDate: ''
        };
        this.validateForm.reset();
      },
      error: (err) => {
        console.error('❌ Failed to save question:', err);
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
      },
      error: (err) => {
        console.error('❌ Failed to post answer:', err);
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
  
        // ✅ Pour chaque réponse récupérée, charger son image si elle existe
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

  showImageModal = false;
  selectedImageUrl: string | undefined;

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
  
  
  

  
}
