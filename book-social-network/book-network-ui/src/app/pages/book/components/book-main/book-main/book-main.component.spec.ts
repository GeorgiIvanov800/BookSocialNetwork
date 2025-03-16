import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookMainComponent } from './book-main.component';

describe('BookMainComponent', () => {
  let component: BookMainComponent;
  let fixture: ComponentFixture<BookMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookMainComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
