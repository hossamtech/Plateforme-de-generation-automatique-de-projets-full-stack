import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Joueur } from '../models/joueur.model';

@Injectable({
  providedIn: 'root',
})
export class JoueurService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/joueurs`;

    joueurList = signal<Joueur[]>([]);

    constructor(private http: HttpClient) {
        this.fetchAll();
    }

    fetchAll(): void {
        this.http.get<Joueur[]>(this.apiUrl).subscribe((data) => {
            console.log(data)
            this.joueurList.set(data);
        });
    }

    add(data: Joueur): void {
        console.log(data);
        this.http.post<Joueur>(this.apiUrl, data).subscribe((newJoueur) => {
            this.joueurList.update((currentList) => [...currentList, newJoueur]);
        });
    }

    update(data: Joueur): void {
        this.http.put<Joueur>(this.apiUrl, data).subscribe((updatedJoueur) => {
            this.joueurList.update((currentList) =>
                currentList.map((item) => (item.idjoueur === updatedJoueur.idjoueur ? updatedJoueur : item))
            );
        });
    }

    delete(id: number): void {
        this.http.delete<void>(`${this.apiUrl}/${id}`).subscribe(() => {
            this.joueurList.update((currentList) =>
                currentList.filter((item) => item.idjoueur !== id)
            );
        });
    }
}
