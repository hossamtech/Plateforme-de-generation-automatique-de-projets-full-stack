import { Injectable, Signal, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Equipe } from '../models/equipe.model';

@Injectable({
    providedIn: 'root',
})
export class EquipeService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/equipes`;

    // Signal to store the equipe list
    private equipeList = signal<Equipe[]>([]);

    constructor(private http: HttpClient) {
        // Fetch the equipe data on service initialization
        this.fetchAllEquipes();
    }

    // Expose the equipe list signal for components to subscribe
    get equipes(): Signal<Equipe[]> {
        return this.equipeList.asReadonly();
    }

    // Fetch all equipes and update the signal
    private fetchAllEquipes(): void {
        this.http.get<Equipe[]>(this.apiUrl).subscribe((data) => {
            this.equipeList.set(data); // Set the equipe list to the signal
        });
    }

    // Add a new equipe and update the signal
    add(data: Equipe): void {
        this.http.post<Equipe>(this.apiUrl, data).subscribe((newEquipe) => {
            this.equipeList.update((currentList) => [...currentList, newEquipe]); // Add the new equipe to the signal
        });
    }

    // Update an existing equipe and update the signal
    update(data: Equipe): void {
        this.http.put<Equipe>(`${this.apiUrl}/${data.idequipe}`, data).subscribe((updatedEquipe) => {
            this.equipeList.update((currentList) =>
                currentList.map((equipe) => (equipe.idequipe === updatedEquipe.idequipe ? updatedEquipe : equipe))
            ); // Replace the updated equipe in the list
        });
    }

    // Delete an equipe and update the signal
    delete(id: number): void {
        this.http.delete<void>(`${this.apiUrl}/${id}`).subscribe(() => {
            this.equipeList.update((currentList) => currentList.filter((equipe) => equipe.idequipe !== id)); // Remove the deleted equipe
        });
    }
}
