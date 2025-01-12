import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { arbitre } from '../models/arbitre.model';

@Injectable({
  providedIn: 'root',
})
export class arbitreService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/arbitres`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<arbitre[]> {
        return this.http.get<arbitre[]>(this.apiUrl);
    }

    getById(id: number): Observable<arbitre> {
        return this.http.get<arbitre>(`${this.apiUrl}/${id}`);
    }

    add(data: arbitre): Observable<arbitre> {
        return this.http.post<arbitre>(this.apiUrl, data);
    }

    update(data: arbitre): Observable<arbitre> {
        return this.http.put<arbitre>(`${this.apiUrl}/${data.id}`, data);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
