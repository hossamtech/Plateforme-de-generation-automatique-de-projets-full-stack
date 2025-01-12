import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { joueur } from '../models/joueur.model';

@Injectable({
  providedIn: 'root',
})
export class joueurService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/joueurs`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<joueur[]> {
        return this.http.get<joueur[]>(this.apiUrl);
    }

    getById(id: number): Observable<joueur> {
        return this.http.get<joueur>(`${this.apiUrl}/${id}`);
    }

    add(data: joueur): Observable<joueur> {
        return this.http.post<joueur>(this.apiUrl, data);
    }

    update(data: joueur): Observable<joueur> {
        return this.http.put<joueur>(`${this.apiUrl}/${data.id}`, data);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
