import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { equipe } from '../models/equipe.model';

@Injectable({
  providedIn: 'root',
})
export class equipeService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/equipes`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<equipe[]> {
        return this.http.get<equipe[]>(this.apiUrl);
    }

    getById(id: number): Observable<equipe> {
        return this.http.get<equipe>(`${this.apiUrl}/${id}`);
    }

    add(data: equipe): Observable<equipe> {
        return this.http.post<equipe>(this.apiUrl, data);
    }

    update(data: equipe): Observable<equipe> {
        return this.http.put<equipe>(`${this.apiUrl}/${data.id}`, data);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
