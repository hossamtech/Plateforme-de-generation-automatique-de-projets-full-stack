import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { stade } from '../models/stade.model';

@Injectable({
  providedIn: 'root',
})
export class stadeService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/stades`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<stade[]> {
        return this.http.get<stade[]>(this.apiUrl);
    }

    getById(id: number): Observable<stade> {
        return this.http.get<stade>(`${this.apiUrl}/${id}`);
    }

    add(data: stade): Observable<stade> {
        return this.http.post<stade>(this.apiUrl, data);
    }

    update(data: stade): Observable<stade> {
        return this.http.put<stade>(`${this.apiUrl}/${data.id}`, data);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
