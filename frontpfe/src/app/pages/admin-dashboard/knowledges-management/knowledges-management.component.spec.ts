import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KnowledgesManagementComponent } from './knowledges-management.component';

describe('KnowledgesManagementComponent', () => {
  let component: KnowledgesManagementComponent;
  let fixture: ComponentFixture<KnowledgesManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KnowledgesManagementComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KnowledgesManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
