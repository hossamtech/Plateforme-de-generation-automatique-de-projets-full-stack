import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Equipe } from '../models/equipe.model';

@Injectable({
  providedIn: 'root',
})
export class EquipeService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/equipes`;

    equipeList = signal<Equipe[]>([]);

    constructor(private http: HttpClient) {
        this.fetchAll();
    }


    fetchAll(): void {
        this.http.get<Equipe[]>(this.apiUrl).subscribe((data) => {
            this.equipeList.set(data);
        });
    }

    add(data: Equipe): void {
        this.http.post<Equipe>(this.apiUrl, data).subscribe((newEquipe) => {
            this.equipeList.update((currentList) => [...currentList, newEquipe]);
        });
    }

    update(data: Equipe): void {
        this.http.put<Equipe>(this.apiUrl, data).subscribe((updatedEquipe) => {
            this.equipeList.update((currentList) =>
                currentList.map((item) => (item.idequipe === updatedEquipe.idequipe ? updatedEquipe : item))
            );
        });
    }

    delete(id: number): void {
        this.http.delete<void>(`${this.apiUrl}/${id}`).subscribe(() => {
            this.equipeList.update((currentList) =>
                currentList.filter((item) => item.idequipe !== id)
            );
        });
    }
}
