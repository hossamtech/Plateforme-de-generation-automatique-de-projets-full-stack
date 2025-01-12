import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { match } from '../models/match.model';

@Injectable({
  providedIn: 'root',
})
export class matchService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/matchs`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<match[]> {
        return this.http.get<match[]>(this.apiUrl);
    }

    getById(id: number): Observable<match> {
        return this.http.get<match>(`${this.apiUrl}/${id}`);
    }

    add(data: match): Observable<match> {
        return this.http.post<match>(this.apiUrl, data);
    }

    update(data: match): Observable<match> {
        return this.http.put<match>(`${this.apiUrl}/${data.id}`, data);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
