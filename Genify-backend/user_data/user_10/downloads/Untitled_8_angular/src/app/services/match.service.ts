import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Match } from '../models/match.model';

@Injectable({
  providedIn: 'root',
})
export class MatchService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/matchs`;

    matchList = signal<Match[]>([]);

    constructor(private http: HttpClient) {
        this.fetchAll();
    }

    fetchAll(): void {
        this.http.get<Match[]>(this.apiUrl).subscribe((data) => {
            this.matchList.set(data);
        });
    }

    add(data: Match): void {
        this.http.post<Match>(this.apiUrl, data).subscribe((newMatch) => {
            this.matchList.update((currentList) => [...currentList, newMatch]);
        });
    }

    update(data: Match): void {
        this.http.put<Match>(`${this.apiUrl}/${data.idmatch}`, data).subscribe((updatedMatch) => {
            this.matchList.update((currentList) =>
                currentList.map((item) => (item.idmatch === updatedMatch.idmatch ? updatedMatch : item))
            );
        });
    }

    delete(id: number): void {
        this.http.delete<void>(`${this.apiUrl}/${id}`).subscribe(() => {
            this.matchList.update((currentList) =>
                currentList.filter((item) => item.idmatch !== id)
            );
        });
    }
}
