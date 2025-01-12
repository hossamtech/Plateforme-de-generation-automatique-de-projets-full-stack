import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import {LucideAngularModule} from "lucide-angular";
import {TopbarComponent} from "./pages/layouts/topbar/topbar.component";
import {SidebarComponent} from "./pages/layouts/sidebar/sidebar.component";
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLinkActive, RouterLink, LucideAngularModule, TopbarComponent, SidebarComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Untitled_4_angular';
}
