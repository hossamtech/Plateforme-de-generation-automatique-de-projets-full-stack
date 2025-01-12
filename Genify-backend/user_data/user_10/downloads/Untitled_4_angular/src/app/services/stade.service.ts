import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Stade } from '../models/stade.model';

@Injectable({
  providedIn: 'root',
})
export class StadeService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/stades`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<Stade[]> {
        return this.http.get<Stade[]>(this.apiUrl);
    }

    getById(id: number): Observable<Stade> {
        return this.http.get<Stade>(`${this.apiUrl}/${id}`);
    }

    add(data: Stade): Observable<Stade> {
        return this.http.post<Stade>(this.apiUrl, data);
    }

    update(data: Stade): Observable<Stade> {
        return this.http.put<Stade>(`${this.apiUrl}/${data.idstade}`, data);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
