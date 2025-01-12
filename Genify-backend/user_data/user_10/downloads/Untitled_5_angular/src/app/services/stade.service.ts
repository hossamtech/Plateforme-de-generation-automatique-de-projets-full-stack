import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Stade } from '../models/stade.model';

@Injectable({
  providedIn: 'root',
})
export class StadeService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/stades`;

    stadeList = signal<Stade[]>([]);

    constructor(private http: HttpClient) {
        this.fetchAll();
    }

    fetchAll(): void {
        this.http.get<Stade[]>(this.apiUrl).subscribe((data) => {
            this.stadeList.set(data);
        });
    }

    add(data: Stade): void {
        this.http.post<Stade>(this.apiUrl, data).subscribe((newStade) => {
            this.stadeList.update((currentList) => [...currentList, newStade]);
        });
    }

    update(data: Stade): void {
        this.http.put<Stade>(`${this.apiUrl}/${data.idstade}`, data).subscribe((updatedStade) => {
            this.stadeList.update((currentList) =>
                currentList.map((item) => (item.idstade === updatedStade.idstade ? updatedStade : item))
            );
        });
    }

    delete(id: number): void {
        this.http.delete<void>(`${this.apiUrl}/${id}`).subscribe(() => {
            this.stadeList.update((currentList) =>
                currentList.filter((item) => item.idstade !== id)
            );
        });
    }
}
