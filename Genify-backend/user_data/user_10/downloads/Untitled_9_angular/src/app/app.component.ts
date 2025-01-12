import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import {TopbarComponent} from './components/layout/topbar/topbar.component';
import {SidebarComponent} from './components/layout/sidebar/sidebar.component';
import {FooterComponent} from "./components/layout/footer/footer.component";
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLinkActive, RouterLink, TopbarComponent, SidebarComponent, FooterComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Untitled_9_angular';
}
