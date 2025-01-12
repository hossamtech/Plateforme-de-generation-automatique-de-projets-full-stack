import { CommonModule } from '@angular/common';
import {Component, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from "@angular/router";
import {icons, LUCIDE_ICONS, LucideAngularModule, LucideIconProvider} from "lucide-angular";
import {SimplebarAngularModule} from "simplebar-angular";
import {MENU} from "./menu";


@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, LucideAngularModule, SimplebarAngularModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [{ provide: LUCIDE_ICONS, multi: true, useValue: new LucideIconProvider(icons) }]
})
export class SidebarComponent{
  menuItems = MENU;

  hideSidebar() {
    let sidebarOverlay = document.getElementById("sidebar-overlay") as any;
    sidebarOverlay.classList.add("hidden");
    document.documentElement.querySelector('.app-menu')?.classList.add("hidden");
    document.body.classList.remove("overflow-hidden");
  }

}
