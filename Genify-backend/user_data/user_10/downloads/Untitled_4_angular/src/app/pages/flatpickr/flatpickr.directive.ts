import { Directive, ElementRef, Input, OnInit } from '@angular/core';
import { Options } from 'flatpickr/dist/types/options';
import flatpickr from "flatpickr";


@Directive({
  selector: '[mwlFlatpickr]'
})
export class FlatpickrDirective implements OnInit {
  @Input() flatpickrOptions: Options = {};

  constructor(private el: ElementRef) {}

  ngOnInit() {
    this.initFlatpickr();
  }

  private initFlatpickr() {
    // @ts-ignore
    flatpickr(this.el.nativeElement, this.flatpickrOptions);
  }
}
