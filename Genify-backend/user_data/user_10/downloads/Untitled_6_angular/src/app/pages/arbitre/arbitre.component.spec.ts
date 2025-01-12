import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArbitreComponent } from './arbitre.component';

describe('ArbitreComponent', () => {
  let component: ArbitreComponent;
  let fixture: ComponentFixture<ArbitreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArbitreComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ArbitreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
