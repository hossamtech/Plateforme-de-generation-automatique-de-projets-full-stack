import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Joueur } from '../models/joueur.model';

@Injectable({
  providedIn: 'root',
})
export class JoueurService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/joueurs`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<Joueur[]> {
        return this.http.get<Joueur[]>(this.apiUrl);
    }

    getById(id: number): Observable<Joueur> {
        return this.http.get<Joueur>(`${this.apiUrl}/${id}`);
    }

    add(data: Joueur): Observable<Joueur> {
        return this.http.post<Joueur>(this.apiUrl, data);
    }

    update(data: Joueur): Observable<Joueur> {
        return this.http.put<Joueur>(`${this.apiUrl}/${data.idjoueur}`, data);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
