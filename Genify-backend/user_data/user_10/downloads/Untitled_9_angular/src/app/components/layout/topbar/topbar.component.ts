import {CUSTOM_ELEMENTS_SCHEMA, Component, Inject} from '@angular/core';
import { RouterModule } from '@angular/router';
import { LUCIDE_ICONS, LucideAngularModule, LucideIconProvider, Route, icons } from 'lucide-angular';
import { CommonModule, DOCUMENT } from '@angular/common';
import {TooltipModule, TooltipOptions} from "ng2-tooltip-directive";


@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [LucideAngularModule, RouterModule, CommonModule, TooltipModule],
  templateUrl: './topbar.component.html',
  styleUrl: './topbar.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA,],
  providers: [{ provide: LUCIDE_ICONS, multi: true, useValue: new LucideIconProvider(icons) }],
})
export class TopbarComponent{

  constructor(
      @Inject(DOCUMENT) private document: Document
  ) {}

  myOptions: TooltipOptions = {
    showDelay: 100,
    tooltipClass: 'custom-tooltip'
  };

  windowScroll() {
    var scrollUp = document.documentElement.scrollTop;
    if (scrollUp >= 50) {
      document.getElementById("page-topbar")?.classList.add('is-sticky');
    } else {
      document.getElementById("page-topbar")?.classList.remove('is-sticky');
    }
  }

  changeSidebar() {
    var windowSize = document.documentElement.clientWidth;
    let sidebarOverlay = document.getElementById("sidebar-overlay") as any;

    if (windowSize < 768) {
      this.document.body.classList.add("overflow-hidden");
      // Check if the sidebar overlay is hidden
      if (sidebarOverlay.classList.contains("hidden")) {
        sidebarOverlay.classList.remove("hidden");
        this.document.documentElement.querySelector('.app-menu')?.classList.remove("hidden");
      } else {
        sidebarOverlay.classList.add("hidden");
        this.document.documentElement.querySelector('.app-menu')?.classList.add("hidden");
      }
    }
  }
}
