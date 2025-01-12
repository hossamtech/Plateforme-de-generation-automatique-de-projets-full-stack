import { Injectable, Signal, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Match } from '../models/match.model';

@Injectable({
    providedIn: 'root',
})
export class MatchService {
    private baseUrl = 'http://localhost:8085';
    private apiUrl = `${this.baseUrl}/api/matchs`;

    // Signal to store and update the match list
    private matchList = signal<Match[]>([]);

    constructor(private http: HttpClient) {
        // Initialize the signal with data from the backend
        this.fetchAllMatches();
    }

    // Expose the signal to components for read-only access
    get matchList$(): Signal<Match[]> {
        return this.matchList.asReadonly();
    }

    // Fetch all matches and update the signal
    private fetchAllMatches(): void {
        this.http.get<Match[]>(this.apiUrl).subscribe((data) => {
            this.matchList.set(data); // Set the match list in the signal
        });
    }

    // Add a new match and update the signal
    add(match: Match): void {
        this.http.post<Match>(this.apiUrl, match).subscribe((newMatch) => {
            this.matchList.update((currentList) => [...currentList, newMatch]); // Add the new match to the signal
        });
    }

    // Update an existing match and update the signal
    update(match: Match): void {
        this.http.put<Match>(`${this.apiUrl}/${match.idmatch}`, match).subscribe((updatedMatch) => {
            this.matchList.update((currentList) =>
                currentList.map((m) => (m.idmatch === updatedMatch.idmatch ? updatedMatch : m))
            ); // Replace the updated match in the list
        });
    }

    // Delete a match and update the signal
    delete(id: number): void {
        this.http.delete<void>(`${this.apiUrl}/${id}`).subscribe(() => {
            this.matchList.update((currentList) => currentList.filter((match) => match.idmatch !== id)); // Remove the match
        });
    }
}
