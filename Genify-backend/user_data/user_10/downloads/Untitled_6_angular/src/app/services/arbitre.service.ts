import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Arbitre } from '../models/arbitre.model';

@Injectable({
  providedIn: 'root',
})
export class ArbitreService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/arbitres`;

    arbitreList = signal<Arbitre[]>([]);

    constructor(private http: HttpClient) {
        this.fetchAll();
    }

    fetchAll(): void {
        this.http.get<Arbitre[]>(this.apiUrl).subscribe((data) => {
            this.arbitreList.set(data);
        });
    }

    add(data: Arbitre): void {
        this.http.post<Arbitre>(this.apiUrl, data).subscribe((newArbitre) => {
            this.arbitreList.update((currentList) => [...currentList, newArbitre]);
        });
    }

    update(data: Arbitre): void {
        this.http.put<Arbitre>(`${this.apiUrl}/${data.idarbitre}`, data).subscribe((updatedArbitre) => {
            this.arbitreList.update((currentList) =>
                currentList.map((item) => (item.idarbitre === updatedArbitre.idarbitre ? updatedArbitre : item))
            );
        });
    }

    delete(id: number): void {
        this.http.delete<void>(`${this.apiUrl}/${id}`).subscribe(() => {
            this.arbitreList.update((currentList) =>
                currentList.filter((item) => item.idarbitre !== id)
            );
        });
    }
}
